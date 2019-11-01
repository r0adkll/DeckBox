package com.r0adkll.deckbuilder.util.extensions

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase

val Firebase.auth: FirebaseAuth
    get() = FirebaseAuth.getInstance()
