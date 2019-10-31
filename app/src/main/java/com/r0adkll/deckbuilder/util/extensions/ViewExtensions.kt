package com.r0adkll.deckbuilder.util.extensions

import androidx.annotation.DrawableRes
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.r0adkll.deckbuilder.arch.domain.features.remote.model.ExpansionPreview
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

fun View.isVisible(): Boolean = this.visibility == View.VISIBLE

fun EditText.moveCursorToEnd() {
    this.setSelection(this.text.length)
}

/*
 * TextView
 */

fun TextView.drawableStart(@DrawableRes resId: Int) {
    this.setCompoundDrawablesRelativeWithIntrinsicBounds(resId, 0, 0, 0)
}

/*
 * LayoutParams
 */

fun View.layoutWidth(width: Int) {
    val lp = this.layoutParams
    lp.width = width
    this.layoutParams = lp
}

fun View.layoutHeight(height: Int) {
    val lp = this.layoutParams
    lp.height = height
    this.layoutParams = lp
}

fun View.addLayoutHeight(height: Int) {
    val lp = this.layoutParams
    lp.height = lp.height + height
    this.layoutParams = lp
}

fun View.margins(left: Int? = null,
                 top: Int? = null,
                 right: Int? = null,
                 bottom: Int? = null) {
    val lp = this.layoutParams as? ViewGroup.MarginLayoutParams
    lp?.let { params ->
        left?.let { params.leftMargin = it }
        top?.let { params.topMargin = it }
        right?.let { params.rightMargin = it }
        bottom?.let { params.bottomMargin = it }
        this.layoutParams = lp
    }
}

fun View.marginsRelative(start: Int? = null,
                         top: Int? = null,
                         end: Int? = null,
                         bottom: Int? = null) {
    val lp = this.layoutParams as? ViewGroup.MarginLayoutParams
    lp?.let { params ->
        start?.let { params.marginStart = it }
        top?.let { params.topMargin = it }
        end?.let { params.marginEnd = it }
        bottom?.let { params.bottomMargin = it }
        this.layoutParams = lp
    }
}

fun View.margins(margins: ExpansionPreview.PreviewSpec.Margins?) {
    val lp = this.layoutParams as? ViewGroup.MarginLayoutParams
    lp?.let { params ->
        margins?.start?.let { params.marginStart = it }
        margins?.top?.let { params.topMargin = it }
        margins?.end?.let { params.marginEnd = it }
        margins?.bottom?.let { params.bottomMargin = it }
        this.layoutParams = lp
    }
}

/*
 * Recycler based extensions
 */

fun <Field : Any> notifyingField(initialValue: Field): NotifyDataSetChanged<Field> = NotifyDataSetChanged(initialValue)

class NotifyDataSetChanged<Field : Any>(initialValue: Field) : ReadWriteProperty<RecyclerView.Adapter<*>, Field> {

    private var backingField: Field = initialValue

    override fun getValue(thisRef: RecyclerView.Adapter<*>, property: KProperty<*>): Field {
        return backingField
    }

    override fun setValue(thisRef: RecyclerView.Adapter<*>, property: KProperty<*>, value: Field) {
        backingField = value
        thisRef.notifyDataSetChanged()
    }
}
