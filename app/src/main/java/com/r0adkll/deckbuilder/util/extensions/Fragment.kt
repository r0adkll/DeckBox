package com.r0adkll.deckbuilder.util.extensions

import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar

fun Fragment.makeSnackbar(text: CharSequence, duration: Int = Snackbar.LENGTH_SHORT): Snackbar {
    return Snackbar.make(requireView(), text, duration)
}

fun Fragment.makeSnackbar(@StringRes resId: Int, duration: Int = Snackbar.LENGTH_SHORT): Snackbar {
    return Snackbar.make(requireView(), resId, duration)
}

fun Snackbar.showAndReturn(): Snackbar = this.apply {
    show()
}
