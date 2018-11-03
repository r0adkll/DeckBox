package com.r0adkll.deckbuilder.util.extensions


import android.content.Context
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import android.widget.Toast


fun Context.toast(message: String) = Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
fun Context.toast(@StringRes resId: Int) = Toast.makeText(this, resId, Toast.LENGTH_SHORT).show()

fun androidx.fragment.app.Fragment.toast(message: String) = Toast.makeText(this.activity, message, Toast.LENGTH_SHORT).show()
fun androidx.fragment.app.Fragment.toast(@StringRes resId: Int) = Toast.makeText(this.activity, resId, Toast.LENGTH_SHORT).show()