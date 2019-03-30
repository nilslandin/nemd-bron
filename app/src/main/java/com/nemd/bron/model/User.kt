package com.nemd.bron.model

import com.google.firebase.firestore.DocumentSnapshot

class User(document: DocumentSnapshot) {
    fun getFullName(): String {
        return "$firstName $lastName"
    }

    val firstName = document["first_name"] as String
    val lastName = document["last_name"] as String
    val ssn = document["ssn"] as Long
    val email = document["email"] as String
    val firebaseToken = document["firebase_token"] as String
}
