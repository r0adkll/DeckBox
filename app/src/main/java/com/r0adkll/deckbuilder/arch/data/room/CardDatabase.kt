package com.r0adkll.deckbuilder.arch.data.room

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase


@Database(entities = [

], version = 1)
abstract class CardDatabase : RoomDatabase() {


}