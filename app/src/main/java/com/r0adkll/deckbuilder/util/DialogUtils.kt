package com.r0adkll.deckbuilder.util

import android.content.Context
import android.text.InputType
import android.widget.EditText
import android.widget.FrameLayout
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import com.ftinc.kit.extensions.dip
import io.reactivex.Observable

object DialogUtils {

    fun confirmDialog(context: Context,
                      title: DialogText,
                      message: DialogText?,
                      @StringRes positiveActionText: Int,
                      @StringRes negativeActionText: Int) : Observable<Boolean> {
        return Observable.create<Boolean> { s ->
            val builder = AlertDialog.Builder(context)
                    .setTitle(title.toText(context))
                    .setPositiveButton(positiveActionText) { dialog, _ ->
                        s.onNext(true)
                        s.onComplete()
                        dialog.dismiss()
                    }
                    .setNegativeButton(negativeActionText) { dialog, _ ->
                        s.onNext(false)
                        s.onComplete()
                        dialog.dismiss()
                    }

            message?.let { builder.setMessage(it.toText(context)) }

            val dialog = builder.show()

            s.setCancellable {
                dialog.dismiss()
            }
        }
    }

    fun inputDialog(context: Context,
                    @StringRes title: Int,
                    @StringRes message: Int?,
                    @StringRes hint: Int,
                    @StringRes positiveActionText: Int): Observable<String> {
        return Observable.create<String> { s ->
            val view = FrameLayout(context)
            val input = EditText(context)
            input.setHint(hint)
            input.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_CAP_SENTENCES
            val lp = FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT)
            lp.marginStart = context.dip(20f)
            lp.marginEnd = context.dip(20f)
            view.addView(input, lp)

            val builder = AlertDialog.Builder(context)
                    .setTitle(title)
                    .setView(view)
                    .setPositiveButton(positiveActionText) { dialog, _ ->
                        val text = input.text.toString()
                        s.onNext(text)
                        s.onComplete()
                        dialog.dismiss()
                    }
                    .setNegativeButton(android.R.string.cancel) { dialog, _ ->
                        s.onComplete()
                        dialog.dismiss()
                    }

            message?.let { builder.setMessage(it) }

            val dialog = builder.show()

            s.setCancellable {
                dialog.dismiss()
            }
        }
    }

    sealed class DialogText {

        abstract fun toText(context: Context): CharSequence?

        class Literal(val text: CharSequence?) : DialogText() {
            override fun toText(context: Context): CharSequence? = text
        }

        class Resource(@StringRes val resId: Int, vararg args: Any) : DialogText() {

            private val arguments: Array<out Any> = args

            override fun toText(context: Context): CharSequence? {
                return if (arguments.isNotEmpty()) {
                    context.getString(resId, *arguments)
                } else {
                    context.getString(resId)
                }
            }
        }
    }
}
