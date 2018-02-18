package com.r0adkll.deckbuilder.arch.domain


import paperparcel.PaperParcel
import paperparcel.PaperParcelable


@PaperParcel
data class ExportTask(
        val deckId: String?,
        val sessionId: Long?
) : PaperParcelable {
    companion object {
        @JvmField val CREATOR = PaperParcelExportTask.CREATOR
    }
}