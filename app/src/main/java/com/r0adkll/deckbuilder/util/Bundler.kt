package com.r0adkll.deckbuilder.util

import android.os.Bundle
import android.os.Parcelable


class Bundler {

    private val bundle = Bundle()

    fun int(key: String, value: Int) = bundle.putInt(key, value)
    fun string(key: String, value: String) = bundle.putString(key, value)
    fun bool(key: String, value: Boolean) = bundle.putBoolean(key, value)
    fun long(key: String, value: Long) = bundle.putLong(key, value)
    fun float(key: String, value: Float) = bundle.putFloat(key, value)
    fun double(key: String, value: Double) = bundle.putDouble(key, value)
    fun <T: Parcelable> parcel(key: String, value: T) = bundle.putParcelable(key, value)

    internal fun build(): Bundle = bundle
}


fun bundle(init: Bundler.() -> Unit): Bundle {
    val bundler = Bundler()
    bundler.init()
    return bundler.build()
}