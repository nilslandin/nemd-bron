package com.nemd.bron.model

import com.google.firebase.firestore.DocumentSnapshot

class HCP(document: DocumentSnapshot) {

    val firstName : String = document["first_name"] as String
    val lastName : String = document["last_name"] as String
    val email : String = document["email"] as String
    val fireBaseToken : String = document["firebase_token"] as String

    fun getFullName(): String {
        return "$firstName $lastName"
    }

}
