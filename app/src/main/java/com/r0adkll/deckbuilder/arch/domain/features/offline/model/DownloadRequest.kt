package com.r0adkll.deckbuilder.arch.domain.features.offline.model

import android.os.Parcelable
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.Expansion
import kotlinx.android.parcel.Parcelize


sealed class DownloadRequest : Parcelable{

    /**
     * Download card data for offline use
     * @param expansion the expansion to download, or null for all cards
     */
    @Parcelize
    data class Cards(val expansion: List<Expansion>?, val downloadImages: Boolean = false) : DownloadRequest()

    /**
     * Download the card image data for offline use. Can only request this by the expansion
     * @param expansion the expansion of the card images to download.
     */
    @Parcelize
    data class Images(val expansion: Expansion) : DownloadRequest()
}