package com.r0adkll.deckbuilder.util


import android.os.Build.VERSION_CODES.JELLY_BEAN_MR2
import android.os.Build.VERSION_CODES.LOLLIPOP
import android.os.Bundle
import android.os.IBinder
import android.os.Parcelable
import android.support.annotation.RequiresApi
import android.util.Size
import android.util.SizeF
import android.util.SparseArray
import java.io.Serializable


class Bundler {

    private val bundle = Bundle()

    fun bundle(key: String, value: Bundle) = bundle.putBundle(key, value);
    fun bool(key: String, value: Boolean) = bundle.putBoolean(key, value)
    fun byte(key: String, value: Byte) = bundle.putByte(key, value)
    fun char(key: String, value: Char) = bundle.putChar(key, value)
    fun short(key: String, value: Short) = bundle.putShort(key, value)
    fun int(key: String, value: Int) = bundle.putInt(key, value)
    fun long(key: String, value: Long) = bundle.putLong(key, value)
    fun float(key: String, value: Float) = bundle.putFloat(key, value)
    fun double(key: String, value: Double) = bundle.putDouble(key, value)
    fun string(key: String, value: String) = bundle.putString(key, value)
    fun charSequence(key: String, value: CharSequence) = bundle.putCharSequence(key, value)

    @RequiresApi(LOLLIPOP) fun size(key: String, value: Size) = bundle.putSize(key, value)
    @RequiresApi(LOLLIPOP) fun sizeF(key: String, value: SizeF) = bundle.putSizeF(key, value)

    fun intList(key: String, value: ArrayList<Int>) = bundle.putIntegerArrayList(key, value)
    fun stringList(key: String, value: ArrayList<String>) = bundle.putStringArrayList(key, value)
    fun charSequenceList(key: String, value: ArrayList<CharSequence>) = bundle.putCharSequenceArrayList(key, value)

    fun byteArray(key: String, value: ByteArray) = bundle.putByteArray(key, value)
    fun shortArray(key: String, value: ShortArray) = bundle.putShortArray(key, value)
    fun charArray(key: String, value: CharArray) = bundle.putCharArray(key, value)
    fun floatArray(key: String, value: FloatArray) = bundle.putFloatArray(key, value)
    fun doubleArray(key: String, value: DoubleArray) = bundle.putDoubleArray(key, value)
    fun charSequenceArray(key: String, value: Array<CharSequence>) = bundle.putCharSequenceArray(key, value)
    fun stringArray(key: String, value: Array<String>) = bundle.putStringArray(key, value)

    @RequiresApi(JELLY_BEAN_MR2) fun binder(key: String, value: IBinder) = bundle.putBinder(key, value)

    fun <T: Parcelable> parcel(key: String, value: T) = bundle.putParcelable(key, value)
    fun <T: Parcelable> parcelList(key: String, value: ArrayList<T>) = bundle.putParcelableArrayList(key, value)
    fun <T: Parcelable> parcelArray(key: String, value: Array<T>) = bundle.putParcelableArray(key, value)
    fun <T: Parcelable> sparseParcelArray(key: String, value: SparseArray<T>) = bundle.putSparseParcelableArray(key, value)

    fun <T: Serializable> serial(key: String, value: T) = bundle.putSerializable(key, value)
    fun <E: Enum<E>> enum(key: String, value: E) = bundle.putString(key, value.name)


    infix fun String.to(value: Bundle) = bundle(this, value)
    infix fun String.to(value: Byte) = byte(this, value)
    infix fun String.to(value: Char) = char(this, value)
    infix fun String.to(value: Short) = short(this, value)
    infix fun String.to(value: Boolean) = bool(this, value)
    infix fun String.to(value: Int) = int(this, value)
    infix fun String.to(value: Long) = long(this, value)
    infix fun String.to(value: Float) = float(this, value)
    infix fun String.to(value: Double) = double(this, value)
    infix fun String.to(value: String) = string(this, value)
    infix fun String.to(value: CharSequence) = charSequence(this, value)
    @RequiresApi(LOLLIPOP) infix fun String.to(value: Size) = size(this, value)
    @RequiresApi(LOLLIPOP) infix fun String.to(value: SizeF) = sizeF(this, value)
    infix fun String.to(value: ByteArray) = byteArray(this, value)
    infix fun String.to(value: ShortArray) = shortArray(this, value)
    infix fun String.to(value: CharArray) = charArray(this, value)
    infix fun String.to(value: FloatArray) = floatArray(this, value)
    infix fun String.to(value: DoubleArray) = doubleArray(this, value)
    @RequiresApi(JELLY_BEAN_MR2) infix fun String.to(value: IBinder) = binder(this, value)
    infix fun <T : Parcelable> String.to(value: T) = parcel(this, value)
    infix fun <T : Serializable> String.to(value: T) = serial(this, value)
    infix fun <T : Enum<T>> String.to(value: T) = enum(this, value)

    internal fun build(): Bundle = bundle
}


fun bundle(init: Bundler.() -> Unit): Bundle {
    val bundler = Bundler()
    bundler.init()
    return bundler.build()
}