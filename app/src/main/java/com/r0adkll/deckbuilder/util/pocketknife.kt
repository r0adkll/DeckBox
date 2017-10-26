package com.r0adkll.deckbuilder.util


import android.app.Activity
import android.os.Bundle
import android.os.Parcelable
import android.support.v4.app.Fragment
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KClass


fun <P : Parcelable> Fragment.bindParcelable(key: String): ReadOnlyProperty<Fragment, P> = Lazy { fragment, _ ->
    fragment.arguments!!.getParcelable(key)
}


inline fun <reified E : Enum<E>> Activity.bindEnum(key: String): ReadOnlyProperty<Activity, E> = Lazy { activity, _ ->
    val name = activity.intent?.getStringExtra(key)
    java.lang.Enum.valueOf(E::class.java, name)
}


inline fun <reified E : Enum<E>> Activity.findEnum(key: String, savedInstanceState: Bundle? = null): E? {
    var saved = savedInstanceState?.getSerializable(key)?.let { it as E }
    if (saved == null) {
        saved = this.intent?.getSerializableExtra(key)?.let { it as E }
    }
    return saved
}