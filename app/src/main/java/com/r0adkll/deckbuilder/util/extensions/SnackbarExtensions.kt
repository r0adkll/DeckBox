package com.r0adkll.deckbuilder.util.extensions


import android.app.Activity
import androidx.annotation.IdRes
import androidx.annotation.StringRes
import com.google.android.material.snackbar.Snackbar
import androidx.preference.PreferenceFragment
import androidx.fragment.app.Fragment
import android.view.View


fun Activity.find(@IdRes resId: Int) = this.findViewById<View>(resId)

fun Activity.snackbar(message: String) = snackbar(find(android.R.id.content), message)

fun Activity.snackbar(@StringRes message: Int) = snackbar(find(android.R.id.content), message)

fun Activity.snackbar(view: View, message: String) = com.google.android.material.snackbar.Snackbar.make(view, message, com.google.android.material.snackbar.Snackbar.LENGTH_SHORT).show()

fun Activity.snackbar(view: View, @StringRes message: Int) = com.google.android.material.snackbar.Snackbar.make(view, message, com.google.android.material.snackbar.Snackbar.LENGTH_SHORT).show()

fun androidx.fragment.app.Fragment.snackbar(message: String) = view?.let { snackbar(it, message) }

fun androidx.fragment.app.Fragment.snackbar(@StringRes message: Int) = view?.let { snackbar(it, message) }

fun androidx.fragment.app.Fragment.snackbar(view: View, message: String) = snackbar(view, message, com.google.android.material.snackbar.Snackbar.LENGTH_SHORT)

fun androidx.fragment.app.Fragment.snackbar(view: View, @StringRes message: Int) = snackbar(view, message, com.google.android.material.snackbar.Snackbar.LENGTH_SHORT)

fun androidx.fragment.app.Fragment.snackbar(view: View, message: String, duration: Int) = com.google.android.material.snackbar.Snackbar.make(view, message, duration).show()

fun androidx.fragment.app.Fragment.snackbar(view: View, @StringRes message: Int, duration: Int) = com.google.android.material.snackbar.Snackbar.make(view, message, duration).show()

fun PreferenceFragment.snackbar(message: String) = view?.let { snackbar(it, message) }

fun PreferenceFragment.snackbar(@StringRes message: Int) = view?.let { snackbar(it, message) }

fun PreferenceFragment.snackbar(message: String, duration: Int) = view?.let { snackbar(it, message, duration) }

fun PreferenceFragment.snackbar(@StringRes message: Int, duration: Int) = view?.let { snackbar(it, message, duration) }

fun PreferenceFragment.snackbar(view: View, message: String) = snackbar(view, message, com.google.android.material.snackbar.Snackbar.LENGTH_SHORT)

fun PreferenceFragment.snackbar(view: View, @StringRes message: Int) = snackbar(view, message, com.google.android.material.snackbar.Snackbar.LENGTH_SHORT)

fun PreferenceFragment.snackbar(view: View, message: String, duration: Int) = com.google.android.material.snackbar.Snackbar.make(view, message, duration).show()

fun PreferenceFragment.snackbar(view: View, @StringRes message: Int, duration: Int) = com.google.android.material.snackbar.Snackbar.make(view, message, duration).show()