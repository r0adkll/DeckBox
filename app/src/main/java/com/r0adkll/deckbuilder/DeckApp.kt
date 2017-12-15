package com.r0adkll.deckbuilder


import android.app.Application
import com.bumptech.glide.request.target.ViewTarget
import com.r0adkll.deckbuilder.internal.AppDelegate
import com.r0adkll.deckbuilder.internal.analytics.Analytics
import com.r0adkll.deckbuilder.internal.analytics.LoggingAnalyticInterface
import com.r0adkll.deckbuilder.internal.di.AppComponent
import com.r0adkll.deckbuilder.internal.di.AppModule
import com.r0adkll.deckbuilder.internal.di.DaggerAppComponent
import com.squareup.leakcanary.LeakCanary
import com.squareup.leakcanary.RefWatcher
import javax.inject.Inject


class DeckApp : Application() {

    companion object {
        @JvmStatic lateinit var component: AppComponent
        @JvmStatic lateinit var refWatcher: RefWatcher
    }

    @Inject lateinit var delegates: Set<@JvmSuppressWildcards AppDelegate>

    override fun onCreate() {
        super.onCreate()
        installLeakCanary()
        installDagger().inject(this)
        installAnalytics()
        installDelegates()

        // Setup Glide to allow for custom tag id's so we can set tags to images for our own purpose
        ViewTarget.setTagId(R.id.glide_tag_id)
    }


    fun installAnalytics() {
        Analytics.add(LoggingAnalyticInterface())
    }


    fun installDelegates() {
        delegates.forEach { it.onCreate(this) }
    }


    fun installLeakCanary() {
        refWatcher = LeakCanary.install(this)
    }


    @Suppress("NON_FINAL_MEMBER_IN_FINAL_CLASS")
    open fun installDagger(): AppComponent {
        component = DaggerAppComponent.builder()
                .appModule(AppModule(this))
                .build()
        return component
    }
}