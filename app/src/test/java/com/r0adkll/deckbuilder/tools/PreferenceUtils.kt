package com.r0adkll.deckbuilder.tools

import com.f2prateek.rx.preferences2.Preference
import io.reactivex.Observable
import org.amshove.kluent.When
import org.amshove.kluent.any
import org.amshove.kluent.calling
import org.amshove.kluent.itAnswers
import org.amshove.kluent.itReturns
import org.amshove.kluent.mock

inline fun <reified T : Any> mockPreference(
    toReturn: T,
    asObservable: Observable<T>? = null,
    noinline onSet: PreferenceSetter<T>? = null,
    isSet: Boolean? = null
): Preference<T> {
    val mock = mock<Preference<T>>()
    When calling mock.get() itReturns toReturn

    if (asObservable != null) {
        When calling mock.asObservable() itReturns asObservable
    }

    if (onSet != null) {
        When calling mock.set(any(T::class)) itAnswers {
            onSet.invoke(it.getArgument(0))
        }
    }

    if (isSet != null) {
        When calling mock.isSet itReturns isSet
    }

    return mock
}

typealias PreferenceSetter<T> = (T) -> Unit
