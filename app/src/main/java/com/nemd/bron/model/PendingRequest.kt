package com.nemd.bron.model

import com.google.firebase.firestore.QueryDocumentSnapshot

class PendingRequest(doc: QueryDocumentSnapshot) {

    val hpid = doc["hpid"] as String
}
