package com.nemd.bron

import android.os.Bundle
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.iid.FirebaseInstanceId
import com.nemd.bron.model.User
import kotlinx.android.synthetic.main.activity_main.*
import timber.log.Timber


class MainActivity : UserAwareBaseActivity() {

    private lateinit var fireBaseDB: FirebaseFirestore

    private var currentUser: FirebaseUser? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fireBaseDB = FirebaseFirestore.getInstance()

        getUser()
    }

    private var user: User? = null

    private fun getUser() {
        val currentUser = fireBaseAuth.currentUser

        if (currentUser != null) {
            fireBaseDB.collection("users").document(currentUser.uid)
                .get()
                .addOnSuccessListener { document ->
                    Timber.d("${document.id} => ${document.data}")
                    user = User(document)

                    updateUI()

                    updateFireBaseToken()
                }
                .addOnFailureListener { exception ->
                    Timber.e(exception, "Error getting User.")
                    logout()
                }
        }
    }

    private fun updateFireBaseToken() {
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

    private fun updateUI() {

        nameTV.text = user?.getFullName()

        signOutBtn.setOnClickListener {
            logout()
        }
    }

    private fun logout() {
        fireBaseAuth.signOut()
        goToLogin()
    }

}
