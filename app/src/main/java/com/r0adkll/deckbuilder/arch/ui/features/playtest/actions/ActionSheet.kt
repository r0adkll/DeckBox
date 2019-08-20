package com.r0adkll.deckbuilder.arch.ui.features.playtest.actions

import android.os.Parcelable
import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import com.r0adkll.deckbuilder.R
import kotlinx.android.parcel.Parcelize


@Parcelize
class ActionSheet(
        @StringRes val title: Int,
        @ColorRes val titleTint: Int,
        val menuItems: List<MenuItem>
) : Parcelable {
    companion object {

        val ACTIVE: ActionSheet
            get() = ActionSheet(
                    R.string.playtest_title_active,
                    R.color.primaryColor,
                    listOf(
                            MenuItem.Switch(0, R.string.action_playtest_burned, R.drawable.ic_burn),
                            MenuItem.Switch(1, R.string.action_playtest_poisoned, R.drawable.ic_poison),
                            MenuItem.Spinner(2, R.drawable.ic_sleep, R.array.status_effects),
                            MenuItem.Divider,
                            MenuItem.Action(3, R.string.action_playtest_attack, R.drawable.ic_attack),
                            MenuItem.Action(4, R.string.action_playtest_damage, R.drawable.ic_favorite_black_24dp),
                            MenuItem.Action(5, R.string.action_playtest_retreat, R.drawable.ic_exit_to_app_black_24dp),
                            MenuItem.Action(6, R.string.action_playtest_shuffle_to_deck, R.drawable.ic_cards_variant),
                            MenuItem.Action(7, R.string.action_playtest_devolve, R.drawable.ic_pokeball),
                            MenuItem.Action(8, R.string.action_playtest_remove_attachments, R.drawable.ic_wrench),
                            MenuItem.Action(9, R.string.action_playtest_discard, R.drawable.ic_delete_black_24dp)
                    )
            )

        val DECK: ActionSheet
            get() = ActionSheet(
                    R.string.playtest_title_deck,
                    R.color.primaryColor,
                    listOf(
                            MenuItem.Action(0, R.string.action_playtest_shuffle, R.drawable.ic_cards_variant),
                            MenuItem.Action(1, R.string.action_playtest_draw_from_top, R.drawable.ic_cards),
                            MenuItem.Action(2, R.string.action_playtest_draw_from_bottom, R.drawable.ic_cards_outline),
                            MenuItem.Action(3, R.string.action_playtest_search_draw, R.drawable.ic_search_black_24dp),
                            MenuItem.Action(4, R.string.action_playtest_search_place, R.drawable.ic_search_black_24dp),
                            MenuItem.Action(5, R.string.action_playtest_shuffle_hand, R.drawable.ic_cards_playing_outline)

                    )
            )

    }
}