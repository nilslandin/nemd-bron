package com.nemd.bron.hcp

import android.content.Intent
import android.os.Bundle
import com.nemd.bron.AbstractMainActivity
import com.nemd.bron.R
import com.nemd.bron.model.HCP
import kotlinx.android.synthetic.main.activity_hcp_main.*
import timber.log.Timber

class HcpMainActivity : AbstractMainActivity() {

    companion object {
        private const val REQUEST_CODE = 1
        const val REQUEST_ID_EXTRA = "REQUEST_ID_EXTRA"
    }

    private var hcp: HCP? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hcp_main)
    }

    override fun onStart() {
        super.onStart()

        getUser()

        createPendingRequest.setOnClickListener {
            Intent(this, HcpRequestDataActivity::class.java)
                .also {
                    startActivityForResult(it, REQUEST_CODE)
                }
        }

        signOutBtn.setOnClickListener {
            logout()
        }
    }

    private fun getUser() {
        currentUser = fireBaseAuth.currentUser

        currentUser?.uid?.let { userId ->
            fireBaseDB.collection("healthCareProviders").document(userId)
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
    }


    override fun getUserType(): String {
        return "healthCareProviders"
    }

}