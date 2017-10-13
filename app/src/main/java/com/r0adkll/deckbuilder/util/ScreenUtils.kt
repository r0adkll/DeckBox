package com.r0adkll.deckbuilder.util


import android.content.res.Resources


object ScreenUtils {

    fun smallestWidth(resources: Resources, widthInDp: Int): Boolean {
        return resources.configuration.smallestScreenWidthDp >= widthInDp
    }


    fun smallestWidth(resources: Resources, config: Config): Boolean = smallestWidth(resources, config.widthInDp)


    enum class Config(val widthInDp: Int) {
        PHONE(320),
        PHABLET(480),
        TABLET_7IN(600),
        TABLET_10(720)
    }
}