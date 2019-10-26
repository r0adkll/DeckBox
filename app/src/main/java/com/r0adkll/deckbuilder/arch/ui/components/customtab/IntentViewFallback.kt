package com.r0adkll.deckbuilder.arch.ui.components.customtab

import android.app.Activity
import android.content.Intent
import android.net.Uri

class IntentViewFallback : CustomTabBrowser.Fallback {

    override fun openUri(activity: Activity, uri: Uri) {
        val intent = Intent(Intent.ACTION_VIEW, uri)
        activity.startActivity(intent)
    }
}
