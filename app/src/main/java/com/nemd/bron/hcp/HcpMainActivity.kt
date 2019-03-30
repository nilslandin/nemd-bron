package com.nemd.bron.hcp

import android.os.Bundle
import com.nemd.bron.AbstractMainActivity
import com.nemd.bron.R
import com.nemd.bron.model.HCP
import com.nemd.bron.model.User
import kotlinx.android.synthetic.main.activity_main.*
import timber.log.Timber

class HcpMainActivity : AbstractMainActivity() {

    private var hcp: HCP? = null

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
            fireBaseDB.collection("healthCareProviders").document(currentUser.uid)
                .get()
                .addOnSuccessListener { document ->
                    Timber.d("${document.id} => ${document.data}")
                    hcp = HCP(document)

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
        nameTV.text = hcp?.getFullName()

        signOutBtn.setOnClickListener {
            logout()
        }
    }
}