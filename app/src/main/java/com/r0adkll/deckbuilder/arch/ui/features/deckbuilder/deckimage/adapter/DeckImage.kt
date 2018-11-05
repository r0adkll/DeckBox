package com.r0adkll.deckbuilder.arch.ui.features.deckbuilder.deckimage.adapter


import android.net.Uri
import android.os.Parcelable
import com.r0adkll.deckbuilder.R
import com.r0adkll.deckbuilder.arch.ui.components.RecyclerItem
import com.r0adkll.deckbuilder.util.compact
import com.r0adkll.deckbuilder.util.type
import kotlinx.android.parcel.Parcelize


sealed class DeckImage : Parcelable, RecyclerItem {

    abstract val uri: Uri


    @Parcelize
    data class Pokemon(val imageUrl: String) : DeckImage() {

        override val layoutId: Int get() = R.layout.item_deck_image_pokemon
        override val uri: Uri
            get() = Uri.parse(imageUrl)

        override fun isItemSame(new: RecyclerItem): Boolean = when(new) {
            is Pokemon -> new.imageUrl == imageUrl
            else -> false
        }


        override fun isContentSame(new: RecyclerItem): Boolean = when(new) {
            is Pokemon -> new.imageUrl == imageUrl
            else -> false
        }
    }


    @Parcelize
    data class Type(val type1: io.pokemontcg.model.Type, val type2: io.pokemontcg.model.Type?) : DeckImage() {

        override val layoutId: Int get() = R.layout.item_deck_image_types
        override val uri: Uri
            get() {
                val builder = Uri.Builder()
                        .scheme("type")
                        .appendPath(type1.compact())

                if (type2 != null) {
                    builder.appendPath(type2.compact())
                }

                return builder.build()
            }

        override fun isItemSame(new: RecyclerItem): Boolean = when(new) {
            is Type -> type1 == new.type1 && type2 == new.type2
            else -> false
        }


        override fun isContentSame(new: RecyclerItem): Boolean = when(new) {
            is Type -> type1 == new.type1 && type2 == new.type2
            else -> false
        }
    }


    companion object {

        fun from(uri: Uri): DeckImage? = when(uri.scheme) {
            "type" -> {
                val parts = uri.pathSegments
                if (parts.isNotEmpty()) {
                    val type1 = parts[0].type()
                    var type2: io.pokemontcg.model.Type? = null

                    if (parts.size > 1) {
                        type2 = parts[1].type()
                    }

                    DeckImage.Type(type1, type2)
                } else {
                    null
                }
            }
            else -> DeckImage.Pokemon(uri.toString())
        }
    }
}