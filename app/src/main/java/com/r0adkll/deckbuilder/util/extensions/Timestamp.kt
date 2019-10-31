package com.r0adkll.deckbuilder.util.extensions

import com.google.firebase.Timestamp

val Timestamp.milliseconds: Long get() = toDate().time
