package com.r0adkll.deckbuilder.util.extensions


import android.app.Activity
import androidx.annotation.IdRes
import androidx.annotation.StringRes
import com.google.android.material.snackbar.Snackbar
import androidx.fragment.app.Fragment
import android.view.View
import androidx.preference.PreferenceFragmentCompat


fun Activity.find(@IdRes resId: Int) = this.findViewById<View>(resId)

fun Activity.snackbar(message: String) = snackbar(find(android.R.id.content), message)

fun Activity.snackbar(@StringRes message: Int) = snackbar(find(android.R.id.content), message)

fun Activity.snackbar(view: View, message: String) = Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show()

fun Activity.snackbar(view: View, @StringRes message: Int) = Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show()

fun Fragment.snackbar(message: String) = view?.let { snackbar(it, message) }

fun Fragment.snackbar(@StringRes message: Int) = view?.let { snackbar(it, message) }

fun Fragment.snackbar(view: View, message: String) = snackbar(view, message, Snackbar.LENGTH_SHORT)

fun Fragment.snackbar(view: View, @StringRes message: Int) = snackbar(view, message, Snackbar.LENGTH_SHORT)

fun Fragment.snackbar(view: View, message: String, duration: Int) = Snackbar.make(view, message, duration).show()

fun Fragment.snackbar(view: View, @StringRes message: Int, duration: Int) = Snackbar.make(view, message, duration).show()

fun PreferenceFragmentCompat.snackbar(message: String) = view?.let { snackbar(it, message) }

fun PreferenceFragmentCompat.snackbar(@StringRes message: Int) = view?.let { snackbar(it, message) }

fun PreferenceFragmentCompat.snackbar(message: String, duration: Int) = view?.let { snackbar(it, message, duration) }

fun PreferenceFragmentCompat.snackbar(@StringRes message: Int, duration: Int) = view?.let { snackbar(it, message, duration) }

fun PreferenceFragmentCompat.snackbar(view: View, message: String) = snackbar(view, message, Snackbar.LENGTH_SHORT)

fun PreferenceFragmentCompat.snackbar(view: View, @StringRes message: Int) = snackbar(view, message, Snackbar.LENGTH_SHORT)

fun PreferenceFragmentCompat.snackbar(view: View, message: String, duration: Int) = Snackbar.make(view, message, duration).show()

fun PreferenceFragmentCompat.snackbar(view: View, @StringRes message: Int, duration: Int) = Snackbar.make(view, message, duration).show()