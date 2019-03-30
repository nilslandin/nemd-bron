package com.nemd.bron.notification

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.messaging.FirebaseMessagingService
import timber.log.Timber

class MyFirebaseMessagingService : FirebaseMessagingService() {

    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */
    override fun onNewToken(token: String?) {
        Timber.d("Refreshed token: $token")

        if (token != null) {

            FirebaseAuth.getInstance().currentUser?.uid?.let { userId ->

                val userUpdate = HashMap<String, Any>()
                userUpdate["firebase_token"] = token

                FirebaseFirestore.getInstance().collection("users").document(userId)
                    .set(userUpdate, SetOptions.merge())
                    .addOnSuccessListener { Timber.d("DocumentSnapshot successfully written!") }
                    .addOnFailureListener { e -> Timber.w(e, "Error writing document") }
            }
        }
    }
}