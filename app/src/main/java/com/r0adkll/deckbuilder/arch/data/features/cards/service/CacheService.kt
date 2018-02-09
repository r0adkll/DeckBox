package com.r0adkll.deckbuilder.arch.data.features.cards.service

import android.annotation.TargetApi
import android.app.IntentService
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationManagerCompat
import com.ftinc.kit.kotlin.extensions.color
import com.r0adkll.deckbuilder.DeckApp
import com.r0adkll.deckbuilder.R
import com.r0adkll.deckbuilder.arch.data.AppPreferences
import com.r0adkll.deckbuilder.arch.data.features.cards.cache.CardCache
import com.r0adkll.deckbuilder.arch.domain.features.cards.CacheManager
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.CacheStatus
import com.r0adkll.deckbuilder.arch.ui.RouteActivity
import com.r0adkll.deckbuilder.util.extensions.retryWithBackoff
import io.pokemontcg.Pokemon
import io.pokemontcg.model.Card
import timber.log.Timber
import javax.inject.Inject


class CacheService : IntentService("DeckBox-Cache-Service") {

    @Inject lateinit var api: Pokemon
    @Inject lateinit var preferences: AppPreferences
    @Inject lateinit var cacheManager: CacheManager
    @Inject lateinit var cardCache: CardCache

    private val notificationManager by lazy { NotificationManagerCompat.from(this) }


    override fun onCreate() {
        super.onCreate()
        DeckApp.component.inject(this)
    }


    override fun onHandleIntent(intent: Intent?) {
        Timber.i("onHandleIntent(${preferences.offlineEnabled})")
        if (preferences.offlineEnabled) {
            deleteCardData()
        } else {
            cacheCardData()
        }
    }


    private fun deleteCardData() {
        cacheManager.updateCacheStatus(CacheStatus.Deleting)
        cardCache.clear()
        preferences.offlineEnabled = false
        cacheManager.updateCacheStatus(CacheStatus.Empty)
    }


    private fun cacheCardData() {
        try {
            var page = 1
            var count = 0

            showNotification(0, false, false)
            cacheManager.updateCacheStatus(CacheStatus.Downloading(0))

            do {
                val cardModels = getPage(page)

                // Map to DB entities
                cardCache.putCards(cardModels)
                count += cardModels.size

                Timber.i("Page of cards inserted into database: ${cardModels.size} cards")
                // Get next page
                page++

                showNotification(count, true, false)
                cacheManager.updateCacheStatus(CacheStatus.Downloading(count))

            } while (cardModels.size == PAGE_SIZE)

            Timber.i("$count cards over $page pages inserted into database")

            preferences.offlineEnabled = true
            cacheManager.updateCacheStatus(CacheStatus.Cached)
            showNotification(count, false, true)

        } catch(e: Exception) {
            Timber.e(e, "Something went terribly wrong when caching card data")
            showNotification(-1, false, true)
            cacheManager.updateCacheStatus(CacheStatus.Empty)
        }
    }


    private fun getPage(pageNumber: Int = 1): List<Card> {
        return api.card()
                .where {
                    page = pageNumber
                    pageSize = PAGE_SIZE
                }
                .observeAll()
                .retryWithBackoff()
                .blockingSingle()
    }


    @TargetApi(Build.VERSION_CODES.O)
    private fun createChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.notification_channel_name)
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(CHANNEL_ID, name, importance)
            channel.description = getString(R.string.notification_channel_description)
            channel.enableLights(true)
            channel.lightColor = Color.BLUE

            val notifMan = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notifMan.createNotificationChannel(channel)
        }
    }


    private fun showNotification(count: Int, isDownloading: Boolean, isFinished: Boolean) {
        createChannel()

        val title = when {
            count >= 0 && !isDownloading && !isFinished -> getString(R.string.notification_caching_title_start)
            count >= 0 && isDownloading && !isFinished -> getString(R.string.notification_caching_title)
            count == -1 && isFinished -> getString(R.string.notification_caching_title_error)
            else -> getString(R.string.notification_caching_title_finished)
        }

        val text = when (count) {
            -1 -> getString(R.string.notification_caching_text_error)
            0 -> getString(R.string.notification_caching_text)
            else -> getString(R.string.notification_caching_text_format, count)
        }

        val intent = RouteActivity.createIntent(this)
        val pending = PendingIntent.getActivity(this, 0, intent, 0)

        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle(title)
                .setContentText(text)
                .setContentIntent(pending)
                .setColor(color(R.color.primaryColor))
                .setOngoing(isDownloading && count == -1)
                .setSmallIcon(when(isDownloading){
                    true -> android.R.drawable.stat_sys_download
                    else -> android.R.drawable.stat_sys_download_done
                })
                .setCategory(NotificationCompat.CATEGORY_PROGRESS)
                .build()

        notificationManager.notify(NOTIFICATION_ID, notification)
    }


    companion object {
        private const val NOTIFICATION_ID = 100
        private const val CHANNEL_ID = "deckbox-notifications"
        private const val PAGE_SIZE = 1000

        fun start(context: Context) {
            val intent = Intent(context, CacheService::class.java)
            context.startService(intent)
        }
    }
}