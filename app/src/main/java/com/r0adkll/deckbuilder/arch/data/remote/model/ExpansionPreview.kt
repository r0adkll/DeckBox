package com.r0adkll.deckbuilder.arch.data.remote.model

import android.graphics.Shader
import android.os.Parcelable
import androidx.room.Ignore
import kotlinx.android.parcel.IgnoredOnParcel
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
            val logo: LogoSpec,
            val title: String,
            val description: String,
            val textColor: String,
            val background: List<DrawableSpec>,
            val foreground: DrawableSpec?
    ) : Parcelable {

        @Parcelize
        data class LogoSpec(
                val url: String,
                val margins: Margins?
        ) : Parcelable

        @Parcelize
        data class DrawableSpec(
                val source: Source,
                val tile: Tile?,
                val margins: Margins?,
                val aspectRatio: Boolean?,
                val alpha: Float?
        ) : Parcelable {

            @Parcelize
            data class Source(
                    val type: String,
                    val value: String,
                    val density: Float?
            ) : Parcelable

            @Parcelize
            data class Tile(
                    val x: String,
                    val y: String
            ) : Parcelable {

                val tileModeX: Shader.TileMode get() = tileMode(x)
                val tileModeY: Shader.TileMode get() = tileMode(y)

                private fun tileMode(value: String): Shader.TileMode = when(value) {
                    "repeat" -> Shader.TileMode.REPEAT
                    "mirror" -> Shader.TileMode.MIRROR
                    "clamp" -> Shader.TileMode.CLAMP
                    else -> Shader.TileMode.REPEAT
                }
            }
        }


        @Parcelize
        data class Margins(
                val start: Int?,
                val top: Int?,
                val end: Int?,
                val bottom: Int?
        ) : Parcelable
    }
}