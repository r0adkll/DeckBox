package com.r0adkll.deckbuilder.arch.data.features.offline.service

import android.annotation.TargetApi
import android.app.IntentService
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.ftinc.kit.arch.util.retryWithBackoff
import com.ftinc.kit.extensions.color
import com.ftinc.kit.util.Stopwatch
import com.r0adkll.deckbuilder.DeckApp
import com.r0adkll.deckbuilder.GlideApp
import com.r0adkll.deckbuilder.R
import com.r0adkll.deckbuilder.arch.data.AppPreferences
import com.r0adkll.deckbuilder.arch.data.features.cards.cache.CardCache
import com.r0adkll.deckbuilder.arch.data.features.offline.repository.OfflineStatusConsumer
import com.r0adkll.deckbuilder.arch.domain.features.expansions.model.Expansion
import com.r0adkll.deckbuilder.arch.domain.features.offline.model.CacheStatus
import com.r0adkll.deckbuilder.arch.domain.features.offline.model.DownloadRequest
import com.r0adkll.deckbuilder.arch.ui.RouteActivity
import com.r0adkll.deckbuilder.util.extensions.bytes
import io.pokemontcg.Pokemon
import io.pokemontcg.model.Card
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class CacheService : IntentService("DeckBox-Cache-Service") {

    @Inject
    lateinit var api: Pokemon
    @Inject
    lateinit var preferences: AppPreferences
    @Inject
    lateinit var offlineStatusConsumer: OfflineStatusConsumer
    @Inject
    lateinit var cardCache: CardCache

    private val notificationManager by lazy { NotificationManagerCompat.from(this) }

    override fun onCreate() {
        super.onCreate()
        DeckApp.component.inject(this)
    }

    override fun onHandleIntent(intent: Intent?) {
        val request = intent?.getParcelableExtra<DownloadRequest>(EXTRA_REQUEST)
        if (request != null) {
            Timber.i("onHandleIntent($request)")
            cacheCardData(request.expansion, request.downloadImages)
        }
    }

    private fun updateCacheStatus(value: Pair<String, CacheStatus>) {
        offlineStatusConsumer.status = offlineStatusConsumer.status.set(value)
    }

    private fun cacheCardData(expansions: List<Expansion>, downloadImages: Boolean) {
        // Update initial state of all expansions
        for (expansion in expansions) {
            // Update cache status and notification
            updateCacheStatus(expansion.code to CacheStatus.Downloading())
            showExpansionNotification(expansion, CacheStatus.Downloading())

            try {
                val cardModels = getExpansion(expansion)

                // Store into database
                cardCache.putCards(cardModels)
                Timber.i("Expansion(${expansion.code}) inserted into database")

                if (downloadImages) {
                    cacheCardImages(expansion, cardModels)
                }

                // Update preferences
                val prefs = preferences.offlineExpansions.get().toMutableSet()
                prefs.add(expansion.code)
                preferences.offlineExpansions.set(prefs)

                // Notify UI and Notification
                updateCacheStatus(expansion.code to CacheStatus.Cached)
                showExpansionNotification(expansion, CacheStatus.Cached)
            } catch (e: Exception) {
                Timber.e("Something went wrong when trying to cache ${expansion.name} card data")
                showExpansionNotification(expansion, null)
                updateCacheStatus(expansion.code to CacheStatus.Empty)
            }
        }
    }

    private fun getExpansion(expansion: Expansion): List<Card> {
        return api.card()
            .where {
                setCode = expansion.code
                pageSize = PAGE_SIZE
            }
            .observeAll()
            .retryWithBackoff()
            .blockingSingle()
    }

    private fun cacheCardImages(expansion: Expansion, cards: List<Card>) {
        val targets = cards.map {
            GlideApp.with(this)
                .downloadOnly()
                .load(it.imageUrl)
                .submit()
        }

        val throttle = Stopwatch.createStarted()
        targets.forEachIndexed { i, target ->
            try {
                val result = target.get()
                Timber.i("Image preloaded (${result.name})")
            } catch (e: Exception) {
                Timber.e(e, "Error caching card image")
            }

            val progress = i.toFloat() / cards.size.toFloat()
            updateCacheStatus(expansion.code to CacheStatus.Downloading(progress))

            // Throttle notification calls or the system will start filtering us
            if (throttle.elapsed(TimeUnit.MILLISECONDS) > 200L) {
                showExpansionNotification(expansion, CacheStatus.Downloading(progress))
                throttle.reset().start()
            }
        }
        throttle.stop()
    }

    @TargetApi(Build.VERSION_CODES.O)
    private fun createChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.notification_channel_name)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance)
            channel.description = getString(R.string.notification_channel_description)
            channel.enableVibration(false)
            channel.setSound(null, null)

            val notifMan = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notifMan.createNotificationChannel(channel)
        }
    }

    private fun showExpansionNotification(expansion: Expansion, status: CacheStatus?) {
        createChannel()

        val title = when (status) {
            CacheStatus.Empty -> getString(R.string.notification_caching_title_start)
            CacheStatus.Queued -> getString(R.string.notification_caching_queued_title)
            is CacheStatus.Downloading -> getString(R.string.notification_caching_title)
            CacheStatus.Cached -> getString(R.string.notification_caching_title_finished)
            else -> getString(R.string.notification_caching_title_error)
        }

        val text = when (status) {
            null -> getString(R.string.notification_caching_text_error)
            CacheStatus.Empty -> getString(R.string.notification_caching_text)
            CacheStatus.Cached -> getString(R.string.notification_expansion_cached_text_format, expansion.name)
            else -> expansion.name
        }

        val intent = RouteActivity.createIntent(this)
        val pending = PendingIntent.getActivity(this, 0, intent, 0)

        val isOngoing = status != null && (status != CacheStatus.Cached)
        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle(title)
            .setContentText(text)
            .setContentIntent(pending)
            .setColor(color(R.color.primaryColor))
            .setOngoing(isOngoing)
            .setOnlyAlertOnce(true)
            .setSound(null)
            .setSmallIcon(when (status is CacheStatus.Downloading) {
                true -> android.R.drawable.stat_sys_download
                else -> android.R.drawable.stat_sys_download_done
            })
            .setCategory(NotificationCompat.CATEGORY_PROGRESS)
            .setPriority(NotificationCompat.PRIORITY_LOW)

        if (status != null && status is CacheStatus.Downloading) {
            val progress = status.progress?.times(100f)?.toInt() ?: 0
            builder.setProgress(100, progress, progress == 0)
        } else {
            builder.setProgress(0, 0, false)
        }

        notificationManager.notify(NOTIFICATION_ID, builder.build())
    }

    companion object {
        private const val EXTRA_REQUEST = "CacheService.Request"
        private const val CHANNEL_ID = "deckbox-notifications"
        private const val NOTIFICATION_ID = 100
        private const val PAGE_SIZE = 1000

        fun start(context: Context, request: DownloadRequest) {
            val intent = Intent(context, CacheService::class.java)
            intent.putExtra(EXTRA_REQUEST, request)
            Timber.i("Starting download request of ${intent.bytes()} bytes")
            context.startService(intent)
        }
    }
}
