@file:Suppress("TooGenericExceptionCaught")

package com.r0adkll.deckbuilder.arch.data.features.offline.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.annotation.DrawableRes
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.TaskStackBuilder
import com.ftinc.kit.extensions.color
import com.ftinc.kit.util.Stopwatch
import com.r0adkll.deckbuilder.DeckApp
import com.r0adkll.deckbuilder.R
import com.r0adkll.deckbuilder.arch.data.AppPreferences
import com.r0adkll.deckbuilder.arch.data.features.offline.cache.ExpansionCacheLoader
import com.r0adkll.deckbuilder.arch.data.features.offline.repository.OfflineStatusConsumer
import com.r0adkll.deckbuilder.arch.domain.features.expansions.model.Expansion
import com.r0adkll.deckbuilder.arch.domain.features.offline.model.CacheStatus
import com.r0adkll.deckbuilder.arch.domain.features.offline.model.DownloadRequest
import com.r0adkll.deckbuilder.arch.ui.RouteActivity
import com.r0adkll.deckbuilder.arch.ui.features.settings.cache.ManageCacheActivity
import com.r0adkll.deckbuilder.util.extensions.bytes
import com.r0adkll.deckbuilder.util.extensions.readablePercentage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.*
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicInteger
import javax.inject.Inject

class CacheService : Service() {

    @Inject lateinit var preferences: AppPreferences
    @Inject lateinit var offlineStatusConsumer: OfflineStatusConsumer
    @Inject lateinit var cacheLoader: ExpansionCacheLoader

    private val scope = CoroutineScope(Dispatchers.IO)
    private val notificationManager by lazy { NotificationManagerCompat.from(this) }
    private val cacheQueue = ArrayDeque<DownloadRequest>()
    private var cacheJob: Job? = null
    private var sessionIndex = AtomicInteger(0)
    private var sessionCount = AtomicInteger(0)

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onCreate() {
        super.onCreate()
        DeckApp.component.inject(this)
        Timber.i("Cache Service started")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val request = intent?.getParcelableExtra<DownloadRequest>(EXTRA_REQUEST)
        if (request != null) {
            Timber.i("Start download ($request)")
            sessionCount.addAndGet(request.expansion.size)
            cacheQueue.push(request)
            cacheCardData()
            return START_STICKY
        }

        stopSelf()
        return START_NOT_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        scope.cancel()
        Timber.i("Shutting down CacheService")
    }

    private fun updateCacheStatus(value: Pair<Expansion, CacheStatus>) {
        offlineStatusConsumer.status = offlineStatusConsumer.status.set(value)
    }

    private fun cacheCardData() {
        Timber.d("Attempting to start card cache job: ${cacheJob?.isCompleted}")
        if (cacheJob?.isCompleted != false) {
            Timber.v("Launching cache coroutine")
            cacheJob = scope.launch {
                var nextRequest = cacheQueue.poll()
                while (nextRequest != null) {
                    Timber.d("Starting cache for $nextRequest")
                    val (expansions, _) = nextRequest
                    for (expansion in expansions) {
                        sessionIndex.incrementAndGet()
                        val progressCount = { "${sessionIndex.get()} of ${sessionCount.get()}" }

                        // Update cache status and notification
                        updateCacheStatus(expansion to CacheStatus.Downloading())
                        showExpansionNotification(expansion, progressCount(), CacheStatus.Downloading())

                        val throttle = Stopwatch.createStarted()
                        val result = cacheLoader.load(expansion, nextRequest) { progress ->
                            scope.launch {
                                updateCacheStatus(expansion to CacheStatus.Downloading(progress))

                                // Throttle notification calls or the system will start filtering us
                                if (throttle.elapsed(TimeUnit.MILLISECONDS) > NOTIFICATION_THROTTLE_MS) {
                                    showExpansionNotification(expansion, progressCount(), CacheStatus.Downloading(progress))
                                    throttle.reset().start()
                                }
                            }
                        }
                        throttle.stop()

                        if (result.isSuccess) {
                            val resultStatus = result.getOrThrow()

                            // Update preferences
                            val prefs = preferences.offlineExpansions.get().toMutableSet()
                            prefs.add(expansion.code)
                            preferences.offlineExpansions.set(prefs)

                            // Notify UI and Notification
                            updateCacheStatus(expansion to resultStatus)
                            showExpansionNotification(expansion, progressCount(), resultStatus)
                        } else {
                            showExpansionNotification(expansion, null, null)
                            updateCacheStatus(expansion to CacheStatus.Empty)
                        }
                    }

                    nextRequest = cacheQueue.poll()
                }
            }
            cacheJob?.invokeOnCompletion {
                stopSelf()
            }
        }
    }

    private fun createChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.notification_channel_offline_name)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance)
            channel.description = getString(R.string.notification_channel_offline_description)
            channel.enableVibration(false)
            channel.setSound(null, null)

            val notifMan = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notifMan.createNotificationChannel(channel)
        }
    }

    private fun showExpansionNotification(expansion: Expansion, progressCount: String?, status: CacheStatus?) {
        createChannel()

        val title = getTitle(status)
        val text = getText(expansion, status)
        val intent = RouteActivity.createIntent(this)
        val pending = PendingIntent.getActivity(this, 0, intent, 0)

        val isOngoing = status != null && status !is CacheStatus.Cached
        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle(title)
            .setContentText(text)
            .setContentIntent(pending)
            .setColor(color(R.color.primaryColor))
            .setOngoing(isOngoing)
            .setOnlyAlertOnce(true)
            .setSound(null)
            .setSmallIcon(getSmallIcon(status))
            .setCategory(NotificationCompat.CATEGORY_PROGRESS)
            .apply {
                progressCount?.let {
                    setSubText(it)
                }

                if (isOngoing) {
                    priority = NotificationCompat.PRIORITY_LOW
                } else {
                    priority = NotificationCompat.PRIORITY_HIGH

                    val manageIntent = TaskStackBuilder.create(this@CacheService)
                        .addParentStack(ManageCacheActivity::class.java)
                        .addNextIntent(ManageCacheActivity.createIntent(this@CacheService))
                        .getPendingIntent(RC_PI_CACHE_MANAGEMENT, PendingIntent.FLAG_UPDATE_CURRENT)

                    addAction(
                        NotificationCompat.Action.Builder(null, getString(R.string.action_manage), manageIntent)
                            .setShowsUserInterface(true)
                            .build()
                    )
                }
            }
            .setProgress(status)

        notificationManager.notify(NOTIFICATION_ID, builder.build())
    }

    private fun getTitle(status: CacheStatus?): String {
        return when (status) {
            CacheStatus.Empty -> getString(R.string.notification_caching_title_start)
            CacheStatus.Queued -> getString(R.string.notification_caching_queued_title)
            is CacheStatus.Downloading -> getString(R.string.notification_caching_title)
            is CacheStatus.Cached -> getString(R.string.notification_caching_title_finished)
            else -> getString(R.string.notification_caching_title_error)
        }
    }

    private fun getText(expansion: Expansion, status: CacheStatus?): String {
        return when (status) {
            null -> getString(R.string.notification_caching_text_error)
            CacheStatus.Empty -> getString(R.string.notification_caching_text)
            is CacheStatus.Cached -> getString(R.string.notification_expansion_cached_text_format, expansion.name)
            else -> expansion.name
        }
    }

    @DrawableRes
    private fun getSmallIcon(status: CacheStatus?): Int {
        return if (status is CacheStatus.Downloading) {
            android.R.drawable.stat_sys_download
        } else {
            android.R.drawable.stat_sys_download_done
        }
    }

    private fun NotificationCompat.Builder.setProgress(status: CacheStatus?): NotificationCompat.Builder {
        return if (status != null && status is CacheStatus.Downloading) {
            val progress = status.progress?.readablePercentage ?: 0
            setProgress(MAX_PROGRESS, progress, progress == 0)
        } else {
            setProgress(0, 0, false)
        }
    }

    companion object {
        private const val EXTRA_REQUEST = "CacheService.Request"
        private const val CHANNEL_ID = "deckbox-offline-downloads"
        private const val RC_PI_CACHE_MANAGEMENT = 15
        private const val NOTIFICATION_ID = 100
        private const val NOTIFICATION_THROTTLE_MS = 500L
        private const val MAX_PROGRESS = 100

        fun start(context: Context, request: DownloadRequest) {
            val intent = Intent(context, CacheService::class.java)
            intent.putExtra(EXTRA_REQUEST, request)
            Timber.i("Starting download request of ${intent.bytes()} bytes")
            context.startService(intent)
        }
    }
}
