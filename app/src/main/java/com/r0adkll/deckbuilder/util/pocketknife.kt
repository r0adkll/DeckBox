package com.r0adkll.deckbuilder.util


import android.os.Parcelable
import android.support.v4.app.Fragment
import kotlin.properties.ReadOnlyProperty


fun <P : Parcelable> Fragment.bindParcelable(key: String): ReadOnlyProperty<Fragment, P> = Lazy { fragment, _ ->
    fragment.arguments.getParcelable(key)
}