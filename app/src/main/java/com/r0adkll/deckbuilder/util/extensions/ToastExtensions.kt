package com.r0adkll.deckbuilder.util.extensions


import android.content.Context
import android.support.annotation.StringRes
import android.widget.Toast


fun Context.toast(message: String) = Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
fun Context.toast(@StringRes resId: Int) = Toast.makeText(this, resId, Toast.LENGTH_SHORT).show()