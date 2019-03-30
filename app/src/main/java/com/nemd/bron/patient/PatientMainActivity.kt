package com.nemd.bron.patient

import android.os.Bundle
import com.nemd.bron.AbstractMainActivity
import com.nemd.bron.R
import com.nemd.bron.model.User
import com.nemd.bron.model.PendingRequest
import kotlinx.android.synthetic.main.activity_patient_main.*
import timber.log.Timber
import java.util.*


class PatientMainActivity : AbstractMainActivity() {

    private var user: User? = null
    private var pendingRequest = ArrayList<PendingRequest>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_patient_main)
    }

    override fun onStart() {
        super.onStart()

        getUser()
    }

    private fun getUser() {
        if (currentUser == null) {
            currentUser = fireBaseAuth.currentUser
        }

        currentUser?.uid?.let { userId ->
            fireBaseDB.collection("users").document(userId)
                .get()
                .addOnSuccessListener { document ->
                    Timber.d("${document.id} => ${document.data}")
                    user = User(document)

                    updateUI()

                    updateFireBaseToken()

                    getPendingRequests()
                }
                .addOnFailureListener { exception ->
                    Timber.e(exception, "Error getting User.")
                    logout()
                }
        }
    }

    private fun getPendingRequests() {
        user?.ssn?.let { ssn ->
            fireBaseDB.collection("pendingRequests")
                .whereEqualTo("ssn", ssn)
                .whereEqualTo("accepted", false)
                .whereEqualTo("declined", false)
                .get()
                .addOnSuccessListener { snapshot ->
                    snapshot.forEach { doc ->
                        pendingRequest.add(PendingRequest(doc))
                    }

                    showPendingRequests()
                }
        }
    }

    private fun showPendingRequests() {

    }

    private fun updateUI() {
        nameTV.text = user?.getFullName()

        signOutBtn.setOnClickListener {
            logout()
        }
    }

    override fun getUserType(): String {
        return "users"
    }
}
