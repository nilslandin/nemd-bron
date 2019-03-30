package com.nemd.bron.model

import com.google.firebase.firestore.DocumentSnapshot

class User(document: DocumentSnapshot) {

    val firstName = document["first_name"] as String
    val lastName = document["last_name"] as String
    val ssn = document["ssn"] as String
    val email = document["email"] as String
    val firebaseToken = document["firebase_token"] as String

    fun getFullName(): String {
        return "$firstName $lastName"
    }

}
