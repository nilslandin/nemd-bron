package com.nemd.bron

import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
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
                }
                .addOnFailureListener { exception ->
                    Timber.e(exception, "Error getting User.")
                    logout()
                }
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
