package com.r0adkll.deckbuilder.arch.ui

import android.app.TaskStackBuilder
import android.content.Context
import android.content.Intent
import android.content.pm.ShortcutInfo
import android.content.pm.ShortcutManager
import android.graphics.drawable.Icon
import android.os.Build
import android.support.annotation.RequiresApi
import com.r0adkll.deckbuilder.R
import com.r0adkll.deckbuilder.arch.domain.features.decks.model.Deck


object Shortcuts {

    private const val CREATE_DECK_ID = "create-new-deck"

    /**
     * Add the 'Create new deck' shortcut if it doesn't already exist
     */
    fun addNewDeckShortcut(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
            val shortcutManager = context.shortcutManager()
            val createDeckShortcut = shortcutManager.dynamicShortcuts.find { it.id == CREATE_DECK_ID }
            if (createDeckShortcut == null) {
                val shortcut = ShortcutInfo.Builder(context, CREATE_DECK_ID)
                        .setShortLabel(context.getString(R.string.shortcut_create_deck_short_label))
                        .setLongLabel(context.getString(R.string.shortcut_create_deck_long_label))
                        .setIcon(Icon.createWithResource(context, R.drawable.ic_add_white_24dp))
                        .setIntent(RouteActivity.createNewDeckIntent(context))
                        .setRank(0)
                        .build()

                shortcutManager.addDynamicShortcuts(listOf(shortcut))
            }
        }
    }


    /**
     * Add a deck shortcut to the list of shortcuts so user's can quickly access the edit screen
     * for their decks
     */
    fun addDeckShortcut(context: Context, deck: Deck) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
            val shortcutManager = context.shortcutManager()

            val shortcut = ShortcutInfo.Builder(context, deck.id)
                    .setShortLabel(deck.name.take(10))
                    .setLongLabel(deck.name.take(25))
                    .setIcon(Icon.createWithResource(context, R.drawable.ic_cards_variant))
                    .setIntent(RouteActivity.createOpenDeckIntent(context, deck.id))
                    .setRank(1)
                    .build()

            // 1) Determine if we already have a shortcut for this deck then float it's rank to 1
            val existing = shortcutManager.dynamicShortcuts.find { it.id == deck.id }
            if (existing != null) {
                shortcutManager.updateShortcuts(listOf(shortcut))
            } else {

                // Determine if we need to drop a shortcut
                if (shortcutManager.dynamicShortcuts.size + shortcutManager.manifestShortcuts.size >= shortcutManager.maxShortcutCountPerActivity) {
                    // Remove the last dynamic shortcut
                    shortcutManager.dynamicShortcuts.lastOrNull()?.let {
                        shortcutManager.removeDynamicShortcuts(listOf(it.id))
                    }
                }

                shortcutManager.addDynamicShortcuts(listOf(shortcut))
            }
        }
    }


    /**
     * Clear all shortcuts out
     */
    fun clearShortcuts(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
            context.shortcutManager()
                    .removeAllDynamicShortcuts()
        }
    }


    @RequiresApi(Build.VERSION_CODES.N_MR1)
    private fun Context.shortcutManager(): ShortcutManager {
        return this.getSystemService(Context.SHORTCUT_SERVICE) as ShortcutManager
    }
}