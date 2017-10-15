package com.r0adkll.deckbuilder.arch.data


import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.f2prateek.rx.preferences2.RxSharedPreferences
import com.r0adkll.deckbuilder.arch.data.features.decks.repository.DecksDataRepository
import com.r0adkll.deckbuilder.arch.domain.features.decks.repository.DecksRepository
import com.r0adkll.deckbuilder.internal.di.AppScope
import com.r0adkll.deckbuilder.util.Schedulers
import dagger.Module
import dagger.Provides
import io.reactivex.android.schedulers.AndroidSchedulers


@Module
class DataModule {

    @Provides @AppScope
    fun provideSharedPreferences(context: Context): SharedPreferences {
        return PreferenceManager.getDefaultSharedPreferences(context)
    }


    @Provides @AppScope
    fun provideRxSharedPreferences(sharedPreferences: SharedPreferences) : RxSharedPreferences {
        return RxSharedPreferences.create(sharedPreferences)
    }


    @Provides @AppScope
    fun provideSchedulers(): Schedulers = Schedulers(
            AndroidSchedulers.mainThread(),
            io.reactivex.schedulers.Schedulers.io(),
            io.reactivex.schedulers.Schedulers.computation(),
            io.reactivex.schedulers.Schedulers.io()
    )


    /*
     * Repositories
     */

    @Provides @AppScope
    fun provideDecksRepository(repository: DecksDataRepository): DecksRepository = repository

}