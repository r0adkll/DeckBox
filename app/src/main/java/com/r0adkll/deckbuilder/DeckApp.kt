package com.r0adkll.deckbuilder


import android.app.Application
import com.bumptech.glide.request.target.ViewTarget
import com.r0adkll.deckbuilder.internal.AppDelegate
import com.r0adkll.deckbuilder.internal.di.AppComponent
import com.r0adkll.deckbuilder.internal.di.AppModule
import com.r0adkll.deckbuilder.internal.di.DaggerAppComponent
import com.squareup.leakcanary.LeakCanary
import com.squareup.leakcanary.RefWatcher
import javax.inject.Inject
import com.crashlytics.android.Crashlytics
import io.fabric.sdk.android.Fabric


class DeckApp : Application() {

    companion object {
        @JvmStatic lateinit var component: AppComponent
        @JvmStatic lateinit var refWatcher: RefWatcher
    }

    @Inject lateinit var delegates: Set<@JvmSuppressWildcards AppDelegate>

    override fun onCreate() {
        super.onCreate()
        installLeakCanary()
        installFabric()
        installDagger().inject(this)
        installDelegates()

        ViewTarget.setTagId(R.id.glide_tag_id)
    }


    fun installDelegates() {
        delegates.forEach { it.onCreate(this) }
    }


    fun installLeakCanary() {
        refWatcher = LeakCanary.install(this)
    }


    fun installFabric() {
        val fabric = Fabric.Builder(this)
                .kits(Crashlytics())
//                .debuggable(BuildConfig.DEBUG)
                .build()
        Fabric.with(fabric)
    }


    open fun installDagger(): AppComponent {
        component = DaggerAppComponent.builder()
                .appModule(AppModule(this))
                .build()
        return component
    }
}