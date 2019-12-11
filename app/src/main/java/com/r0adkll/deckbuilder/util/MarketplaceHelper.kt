package com.r0adkll.deckbuilder.util

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.r0adkll.deckbuilder.BuildConfig
import com.r0adkll.deckbuilder.R
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.PokemonCard
import com.r0adkll.deckbuilder.arch.domain.features.marketplace.model.Product

object MarketplaceHelper {

    private const val TCG_PLAYER_PRICING_INFO_URL = "https://help.tcgplayer.com/hc/en-us/articles/222376867"
    private const val MASS_ENTRY_URL = "http://store.tcgplayer.com/massentry"

    fun openLink(context: Context, uri: Uri) {
        val intent = Intent(Intent.ACTION_VIEW, uri)
        val chooserIntent = Intent.createChooser(intent, context.getString(R.string.share_marketplace_link))
        context.startActivity(chooserIntent)
    }

    fun buildAffiliateLink(product: Product): Uri {
        return Uri.parse(product.url)
            .buildUpon()
            .appendQueryParameter("partner", BuildConfig.AFFILIATE_CODE)
            .appendQueryParameter("utm_campaign", "affiliate")
            .appendQueryParameter("utm_medium", BuildConfig.AFFILIATE_CODE)
            .appendQueryParameter("utm_source", BuildConfig.AFFILIATE_CODE)
            .build()
    }

    fun buildAffiliateLink(cards: List<PokemonCard>, products: Map<String, Product>): Uri {
        val productNames = cards.stack()
            .map {
                it.count to products[it.card.id]?.productName
            }
            .filter { it.second != null }
            .joinToString("||") { "${it.first} ${it.second}" }
        return Uri.parse(MASS_ENTRY_URL)
            .buildUpon()
            .appendQueryParameter("utm_campaign", "affiliate")
            .appendQueryParameter("utm_medium", BuildConfig.AFFILIATE_CODE)
            .appendQueryParameter("utm_source", BuildConfig.AFFILIATE_CODE)
            .appendQueryParameter("c", productNames)
            .build()
    }

    fun showMarketPriceExplanationDialog(activity: AppCompatActivity): AlertDialog {
        return AlertDialog.Builder(activity)
            .setTitle(R.string.dialog_market_price_title)
            .setMessage(R.string.dialog_market_price_message)
            .setPositiveButton(android.R.string.ok) { dialog, _ -> dialog.dismiss() }
            .setNeutralButton(R.string.action_view_more) { dialog, _ ->
                openLink(activity, Uri.parse(TCG_PLAYER_PRICING_INFO_URL))
                dialog.dismiss()
            }
            .show()
    }
}
