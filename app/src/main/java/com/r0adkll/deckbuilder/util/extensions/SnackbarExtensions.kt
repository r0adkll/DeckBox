package com.r0adkll.deckbuilder.util.extensions


import android.app.Activity
import android.support.annotation.IdRes
import android.support.annotation.StringRes
import android.support.design.widget.Snackbar
import android.view.View


fun Activity.find(@IdRes resId: Int) = this.findViewById<View>(resId)

fun Activity.snackbar(message: String) = snackbar(find(android.R.id.content), message)

fun Activity.snackbar(@StringRes message: Int) = snackbar(find(android.R.id.content), message)

fun Activity.snackbar(view: View, message: String) = Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show()

fun Activity.snackbar(view: View, @StringRes message: Int) = Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show()
