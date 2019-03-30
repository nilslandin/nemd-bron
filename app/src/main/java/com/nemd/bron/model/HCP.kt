package com.nemd.bron.model

import com.google.firebase.firestore.DocumentSnapshot

class HCP(document: DocumentSnapshot) {

    val name : String = document["name"] as String
    val email : String = document["email"] as String
    val fireBaseToken : String = document["firebase_token"] as String

    fun getFullName(): String {
        return name
    }

}
