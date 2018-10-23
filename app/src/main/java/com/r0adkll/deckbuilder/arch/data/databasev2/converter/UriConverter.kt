package com.r0adkll.deckbuilder.arch.data.databasev2.converter

import android.net.Uri
import androidx.room.TypeConverter


object UriConverter {

    @JvmStatic
    @TypeConverter
    fun urlToUri(url: String?): Uri? = url?.let { Uri.parse(it) }

    @JvmStatic
    @TypeConverter
    fun uriToUrl(uri: Uri?): String? = uri?.toString()
}