package com.r0adkll.deckbuilder.arch.data.features.community.model

import com.google.firebase.firestore.Exclude

abstract class FirebaseEntity(@Exclude var id: String)
