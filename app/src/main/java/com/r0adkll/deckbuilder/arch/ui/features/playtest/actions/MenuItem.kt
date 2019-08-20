package com.r0adkll.deckbuilder.arch.ui.features.playtest.actions

import android.os.Parcelable
import androidx.annotation.ArrayRes
import androidx.annotation.DrawableRes
import androidx.annotation.IdRes
import androidx.annotation.StringRes
import kotlinx.android.parcel.Parcelize

sealed class MenuItem(
        open val id: Int,
        @StringRes open val textResourceId: Int,
        @DrawableRes open val iconResourceId: Int
) : Parcelable {

    @Parcelize
    class Action(
            override val id: Int,
            override val textResourceId: Int,
            override val iconResourceId: Int
    ) : MenuItem(id, textResourceId, iconResourceId)

    @Parcelize
    object Divider : MenuItem(-1, 0, 0)

    @Parcelize
    class Switch(
            override val id: Int,
            override val textResourceId: Int,
            override val iconResourceId: Int,
            val isChecked: Boolean = false
    ) : MenuItem(id, textResourceId, iconResourceId)

    @Parcelize
    class Spinner(
            override val id: Int,
            override val iconResourceId: Int,
            @ArrayRes val entriesResourceId: Int,
            val currentEntry: Int = 0
    ) : MenuItem(id, 0, iconResourceId)
}