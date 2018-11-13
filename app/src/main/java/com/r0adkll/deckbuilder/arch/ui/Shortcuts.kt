package com.r0adkll.deckbuilder.arch.ui


import android.content.Context
import android.content.pm.ShortcutInfo
import android.content.pm.ShortcutManager
import android.graphics.*
import android.graphics.drawable.Icon
import android.os.Build
import androidx.annotation.RequiresApi
import android.view.View
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.ftinc.kit.kotlin.extensions.dipToPx
import com.r0adkll.deckbuilder.GlideApp
import com.r0adkll.deckbuilder.R
import com.r0adkll.deckbuilder.arch.domain.features.decks.model.Deck
import com.r0adkll.deckbuilder.arch.ui.features.deckbuilder.deckimage.adapter.DeckImage
import com.r0adkll.deckbuilder.arch.ui.widgets.DeckImageView
import timber.log.Timber


/**
 * Helper class for creating and managing shortcuts in the home launcher using [ShortcutManager]
 */
object Shortcuts {

    const val CREATE_DECK_ID = "create-new-deck"


    /**
     * Report the usage of a shortcut by it's Id
     */
    fun reportUsage(context: Context, shortcutId: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
            context.shortcutManager().reportShortcutUsed(shortcutId)
        }
    }


    /**
     * Add a deck shortcut to the list of shortcuts so user's can quickly access the edit screen
     * for their decks
     */
    fun addDeckShortcut(context: Context, deck: Deck) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
            val shortcutManager = context.shortcutManager()

            val shortLabel = if (deck.name.isNotEmpty()) deck.name.take(10) else "Deck"
            val longLabel = if(deck.name.isNotEmpty()) deck.name.take(25) else "Deck with no name"

            val shortcut = ShortcutInfo.Builder(context, deck.id)
                    .setShortLabel(shortLabel)
                    .setLongLabel(longLabel)
                    .setIcon(Icon.createWithResource(context, R.drawable.ic_cards_variant))
                    .setIntent(ShortcutActivity.createOpenDeckIntent(context, deck.id))
                    .setRank(1)
                    .build()

            // 1) Determine if we already have a shortcut for this deck then float it's rank to 1
            val existing = shortcutManager.dynamicShortcuts.find { it.id == deck.id }
            if (existing != null) {
                shortcutManager.reportShortcutUsed(deck.id)
                shortcutManager.updateShortcuts(listOf(shortcut))
                generateDeckImage(context, deck)
            } else {

                // Determine if we need to drop a shortcut
                if (shortcutManager.dynamicShortcuts.size + shortcutManager.manifestShortcuts.size >= shortcutManager.maxShortcutCountPerActivity) {
                    // Remove the last dynamic shortcut
                    shortcutManager.dynamicShortcuts.lastOrNull()?.let {
                        shortcutManager.removeDynamicShortcuts(listOf(it.id))
                    }
                }

                shortcutManager.addDynamicShortcuts(listOf(shortcut))
                generateDeckImage(context, deck)
            }
        }
    }


    fun balanceShortcuts(context: Context, decks: List<Deck>) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
            val shortcutManager = context.shortcutManager()

            // Find any shortcuts that don't exist as decks
            val deadShortcuts = shortcutManager.dynamicShortcuts
                    .filter { shortcut -> decks.none { it.id == shortcut.id } }
                    .map { it.id }
            shortcutManager.removeDynamicShortcuts(deadShortcuts)

            // Update existing deck shortcuts
            val aliveShortcuts = decks.filter { deck -> shortcutManager.dynamicShortcuts.any { it.id == deck.id} }
            aliveShortcuts.forEach { addDeckShortcut(context, it) }
        }
    }


    /**
     * Clear all shortcuts out
     */
    fun clearShortcuts(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
            val shortcutManager = context.shortcutManager()
            if (shortcutManager.dynamicShortcuts.size + shortcutManager.manifestShortcuts.size > 0) {
                shortcutManager.removeAllDynamicShortcuts()
            }
        }
    }


    /**
     * Asynchronously generate the deck image and update the shortcut with it
     */
    @RequiresApi(Build.VERSION_CODES.N_MR1)
    private fun generateDeckImage(context: Context, deck: Deck) {
        val size = context.dipToPx(44f)

        deck.image?.let { image ->
            when(image) {
                is DeckImage.Pokemon -> {
                    GlideApp.with(context)
                            .asBitmap()
                            .load(image.imageUrl)
                            .circleCrop()
                            .into(object : SimpleTarget<Bitmap>(size, size) {
                                override fun onResourceReady(resource: Bitmap?, transition: Transition<in Bitmap>?) {
                                    if (resource != null) {
                                        Timber.i("Pokemon Deck Image loaded")
                                        updateDeckShortcutIcon(context, deck, resource)
                                    }
                                }
                            })
                }
                is DeckImage.Type -> {
                    val view = DeckImageView(context)
                    view.primaryType = image.type1
                    view.secondaryType = image.type2

                    val viewSize = context.dipToPx(128f)
                    val measureWidth = View.MeasureSpec.makeMeasureSpec(viewSize, View.MeasureSpec.EXACTLY)
                    val measuredHeight = View.MeasureSpec.makeMeasureSpec(viewSize, View.MeasureSpec.EXACTLY)

                    view.measure(measureWidth, measuredHeight)
                    view.layout(0, 0, viewSize, viewSize)

                    val deckImageBitmap = Bitmap.createBitmap(viewSize, viewSize, Bitmap.Config.ARGB_8888)
                    val canvas = Canvas(deckImageBitmap)
                    view.draw(canvas)

                    // Now we setup to crop
                    val bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
                    canvas.setBitmap(bitmap)

                    // Render circle
                    val paint = Paint(Paint.ANTI_ALIAS_FLAG)
                    paint.color = Color.BLACK
                    canvas.drawCircle(size / 2f, size / 2f, size / 2f, paint)

                    // Render bitmap
                    paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
                    canvas.drawBitmap(deckImageBitmap, null, Rect(0, 0, size, size), paint)

                    updateDeckShortcutIcon(context, deck, bitmap)
                }
            }
        }
    }


    @RequiresApi(Build.VERSION_CODES.N_MR1)
    private fun updateDeckShortcutIcon(context: Context, deck: Deck, bitmap: Bitmap) {
        val shortcutManager = context.shortcutManager()
        val shortcut = shortcutManager.dynamicShortcuts.find { it.id == deck.id }
        if (shortcut != null) {
            val shortLabel = if (deck.name.isNotEmpty()) deck.name.take(10) else "Deck"
            val longLabel = if(deck.name.isNotEmpty()) deck.name.take(25) else "Deck with no name"

            val updatedShortcut = ShortcutInfo.Builder(context, deck.id)
                    .setShortLabel(shortLabel)
                    .setLongLabel(longLabel)
                    .setIcon(Icon.createWithBitmap(bitmap))
                    .setIntent(ShortcutActivity.createOpenDeckIntent(context, deck.id))
                    .setRank(1)
                    .build()

            shortcutManager.updateShortcuts(listOf(updatedShortcut))
        }
    }


    @RequiresApi(Build.VERSION_CODES.N_MR1)
    private fun Context.shortcutManager(): ShortcutManager {
        return this.getSystemService(Context.SHORTCUT_SERVICE) as ShortcutManager
    }
}