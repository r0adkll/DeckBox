package com.r0adkll.deckbuilder.internal.analytics.firebase

import android.content.Context
import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.FirebaseAnalytics.Param.*
import com.r0adkll.deckbuilder.internal.analytics.AnalyticInterface
import com.r0adkll.deckbuilder.internal.analytics.Event
import com.r0adkll.deckbuilder.util.bundle


class FirebaseAnalyticInterface(
        context: Context
) : AnalyticInterface {

    private val firebaseAnalytics: FirebaseAnalytics = FirebaseAnalytics.getInstance(context)


    override fun setUserId(id: String) {
        firebaseAnalytics.setUserId(id)
    }


    override fun setUserProperty(key: String, value: String?) {
        firebaseAnalytics.setUserProperty(key, value)
    }


    override fun postEvent(event: Event) {
        firebaseAnalytics.logEvent(getEvent(event), getBundle(event))
    }


    private fun getEvent(event: Event): String = when(event) {
        is Event.Login -> FirebaseAnalytics.Event.LOGIN
        is Event.SignUp -> FirebaseAnalytics.Event.SIGN_UP
        is Event.SelectContent -> FirebaseAnalytics.Event.SELECT_CONTENT
        is Event.Search -> FirebaseAnalytics.Event.SEARCH
        is Event.Share -> FirebaseAnalytics.Event.SHARE
        Event.TutorialBegin -> FirebaseAnalytics.Event.TUTORIAL_BEGIN
        Event.TutorialComplete -> FirebaseAnalytics.Event.TUTORIAL_COMPLETE
    }


    private fun getBundle(event: Event): Bundle = when(event) {
        is Event.Login.Google -> bundle { SIGN_UP_METHOD to "google" }
        is Event.Login.Anonymous -> bundle { SIGN_UP_METHOD to "anonymous" }
        is Event.SignUp.Google -> bundle { SIGN_UP_METHOD to "google" }
        is Event.Search -> bundle { SEARCH_TERM to event.term }
        is Event.Share -> bundle {
            CONTENT_TYPE to event.type
            ITEM_ID to event.id
        }
        is Event.SelectContent -> bundle {
            CONTENT_TYPE to event.type
            ITEM_ID to event.id
            if (event.name != null) {
                ITEM_NAME to event.name!!
            }
            if (event.value != null) {
                VALUE to event.value!!
            }
        }
        else -> Bundle()
    }
}