package com.nemd.bron.patient

import android.os.Bundle
import com.nemd.bron.AbstractMainActivity
import com.nemd.bron.R
import com.nemd.bron.model.User
import kotlinx.android.synthetic.main.activity_main.*
import timber.log.Timber


class PatientMainActivity : AbstractMainActivity() {

    private var user: User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onStart() {
        super.onStart()

        getUser()
    }

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

    private fun updateUI() {
        nameTV.text = user?.getFullName()

        signOutBtn.setOnClickListener {
            logout()
        }
    }
}
