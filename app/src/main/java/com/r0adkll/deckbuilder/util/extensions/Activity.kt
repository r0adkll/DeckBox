package com.r0adkll.deckbuilder.util.extensions

import android.app.Activity
import android.os.Bundle

inline fun <reified E : Enum<E>> Activity.findEnum(key: String, savedInstanceState: Bundle? = null): E? {
    var saved = savedInstanceState?.getSerializable(key)?.let { it as E }
    if (saved == null) {
        saved = this.intent?.getSerializableExtra(key)?.let { it as E }
    }
    return saved
}
