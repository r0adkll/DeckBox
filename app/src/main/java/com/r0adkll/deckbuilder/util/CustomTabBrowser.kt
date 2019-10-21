package com.r0adkll.deckbuilder.util

import android.content.ComponentName
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.customtabs.CustomTabsClient
import androidx.browser.customtabs.CustomTabsIntent
import androidx.browser.customtabs.CustomTabsServiceConnection
import androidx.browser.customtabs.CustomTabsSession
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.ftinc.kit.kotlin.extensions.color
import com.r0adkll.deckbuilder.R
import io.reactivex.disposables.CompositeDisposable
import timber.log.Timber

class CustomTabBrowser(private val context: AppCompatActivity) : CustomTabsServiceConnection() {

    private val disposables = CompositeDisposable()
    private val session = MutableLiveData<CustomTabsSession>()
    private var prepareObserver: Observer<CustomTabsSession>? = null
    private val preparedUris = HashMap<Uri, Boolean>()

    init {
        CustomTabsClient.bindCustomTabsService(context, "com.android.chrome", this)
        context.lifecycle.addObserver(object : LifecycleEventObserver {
            override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
                if (event == Lifecycle.Event.ON_DESTROY) {
                    context.lifecycle.removeObserver(this)
                    destroy()
                }
            }
        })
    }

    override fun onCustomTabsServiceConnected(name: ComponentName, client: CustomTabsClient) {
        client.warmup(0L)
        session.value = client.newSession(null)
        Timber.i("Custom tab browser is warmed up and session is ready...")
    }

    override fun onServiceDisconnected(name: ComponentName) {
        Timber.w("Custom tab browser is disconnected...")
        disposables.clear()
        prepareObserver?.let {
            session.removeObserver(it)
            prepareObserver = null
        }
        session.value = null
    }

    fun destroy() {
        context.unbindService(this)
    }

    fun prepare(uri: Uri) {
        if (preparedUris[uri] != true) {
            prepareObserver = Observer {
                val result = it?.mayLaunchUrl(uri, null, null)
                Timber.i("Uri($uri) has been prepared to launch(result=$result)")
                if (result == true) {
                    preparedUris[uri] = true
                }
            }
            session.observeForever(prepareObserver!!)
        }
    }

    fun launch(uri: Uri) {
        return CustomTabsIntent.Builder(session.value)
                .setToolbarColor(context.color(R.color.primaryColor))
                .build()
                .launchUrl(context, uri)
    }
}
