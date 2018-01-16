package com.r0adkll.deckbuilder.util


import android.app.Activity
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.support.v4.app.Fragment
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KClass


fun <P : Parcelable> Fragment.bindOptionalParcelable(key: String): ReadOnlyProperty<Fragment, P> = Lazy { fragment, _ ->
    fragment.arguments!!.getParcelable(key)
}


fun <P : Parcelable> Activity.bindOptionalParcelable(key: String): ReadOnlyProperty<Activity, P?> = Lazy { activity, _ ->
    activity.intent.getParcelableExtra(key)
}


fun <P : Parcelable> Activity.bindOptionalParcelableList(key: String): ReadOnlyProperty<Activity, ArrayList<P>?> = Lazy { activity, _ ->
    activity.intent.getParcelableArrayListExtra(key)
}


fun <P : Parcelable> Activity.bindParcelable(key: String): ReadOnlyProperty<Activity, P> = Lazy { activity, _ ->
    activity.intent.getParcelableExtra(key)
}


fun Activity.bindBoolean(key: String, defaultValue: Boolean = false): ReadOnlyProperty<Activity, Boolean> = Lazy { activity, _ ->
    activity.intent.getBooleanExtra(key, defaultValue)
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


fun <P : Parcelable> Activity.findArrayList(key: String, savedInstanceState: Bundle? = null): ArrayList<P>? {
    var saved = savedInstanceState?.getParcelableArrayList<P>(key)
    if (saved == null) {
        saved = this.intent?.getParcelableArrayListExtra(key)
    }
    return saved
}