package com.r0adkll.deckbuilder.arch.data.remote.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import paperparcel.PaperParcel
import paperparcel.PaperParcelable


@Parcelize
data class ExpansionPreview(
        val version: Int,
        val expiresAt: String,
        val code: String,
        val preview: PreviewSpec
) : Parcelable {

    @Parcelize
    data class PreviewSpec(
            val logoUrl: String,
            val title: String,
            val description: String,
            val textColor: String,
            val background: List<DrawableSpec>,
            val foreground: DrawableSpec?
    ) : Parcelable {

        @Parcelize
        data class DrawableSpec(
                val type: String,
                val data: String
        ) : Parcelable
    }
}