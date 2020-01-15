package com.r0adkll.deckbuilder.arch.ui.features.exporter.di

import com.r0adkll.deckbuilder.arch.domain.features.exporter.ExportTask
import com.r0adkll.deckbuilder.internal.di.scopes.ActivityScope
import dagger.Module
import dagger.Provides

@Module
class MultiExportModule(val deckId: String) {

    @Provides
    @ActivityScope
    fun provideExportTask(): ExportTask = ExportTask(deckId)
}
