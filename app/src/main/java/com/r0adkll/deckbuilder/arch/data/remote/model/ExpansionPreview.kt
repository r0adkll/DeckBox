package com.r0adkll.deckbuilder.arch.data.remote.model

import paperparcel.PaperParcel
import paperparcel.PaperParcelable


@PaperParcel
data class ExpansionPreview(
        val version: Int,
        val expiresAt: String,
        val code: String,
        val preview: PreviewSpec
) : PaperParcelable {

    @PaperParcel
    data class PreviewSpec(
            val logoUrl: String,
            val title: String,
            val description: String,
            val textColor: String,
            val background: List<DrawableSpec>,
            val foreground: DrawableSpec?
    ) : PaperParcelable {

        @PaperParcel
        data class DrawableSpec(
                val type: String,
                val data: String
        ) : PaperParcelable {
            companion object {
                @JvmField val CREATOR = PaperParcelExpansionPreview_PreviewSpec_DrawableSpec.CREATOR
            }
        }

        companion object {
            @JvmField val CREATOR = PaperParcelExpansionPreview_PreviewSpec.CREATOR
        }
    }

    companion object {
        @JvmField val CREATOR = PaperParcelExpansionPreview.CREATOR
    }
}