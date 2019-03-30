package com.nemd.bron

import android.os.Bundle
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.iid.FirebaseInstanceId
import com.nemd.bron.model.User
import timber.log.Timber

abstract class AbstractMainActivity : UserAwareBaseActivity() {

    protected lateinit var fireBaseDB: FirebaseFirestore

    protected var currentUser: FirebaseUser? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        fireBaseDB = FirebaseFirestore.getInstance()
    }

    protected fun updateFireBaseToken() {
        FirebaseInstanceId.getInstance().instanceId
            .addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Timber.e(task.exception, "Could not get token")
                    return@OnCompleteListener
                }

                // Get new Instance ID token
                task.result?.token?.let { token ->
                    registerToken(token)
                }

                Timber.d("Got token")
            })
    }

    private fun registerToken(token: String) {
        val currentUser = fireBaseAuth.currentUser

        if (currentUser != null) {
            val userUpdate = HashMap<String, Any>()
            userUpdate["firebase_token"] = token

            fireBaseDB.collection("users").document(currentUser.uid)
                .set(userUpdate, SetOptions.merge())
                .addOnSuccessListener { Timber.d("DocumentSnapshot successfully written!") }
                .addOnFailureListener { e -> Timber.w(e, "Error writing document") }
        }
    }
}