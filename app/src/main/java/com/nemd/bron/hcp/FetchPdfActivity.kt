package com.nemd.bron.hcp

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import com.google.firebase.firestore.FirebaseFirestore
import com.nemd.bron.R
import com.nemd.bron.UserAwareBaseActivity
import com.nemd.bron.model.User
import kotlinx.android.synthetic.main.activity_fetch_pdf.*

class FetchPdfActivity : UserAwareBaseActivity() {

    companion object {
        const val REQUEST_ID_EXTRA = "REQUEST_ID_EXTRA"
    }

    private lateinit var requestId: String
    private val fireBaseDB = FirebaseFirestore.getInstance()


    private var handler: Handler? = null
    private var runnable: Runnable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fetch_pdf)

        supportActionBar?.title = "Fetch Journals"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        if (intent.hasExtra(REQUEST_ID_EXTRA)) {

            requestId = intent.getStringExtra(REQUEST_ID_EXTRA)

            fetchUser()
        }

        fetchJournalsBtn.setOnClickListener {
            loading.visibility = View.VISIBLE
            fetchJournalsBtn.visibility = View.GONE
            handler = Handler()
            runnable = Runnable {
                loading.visibility = View.GONE
                showPdf()
            }

            handler?.postDelayed(runnable, 2000)
        }
    }

    private fun showPdf() {
        openPdf.visibility = View.VISIBLE
        openPdf.setOnClickListener {
            Intent(this, ViewPdfActivity::class.java).also {
                startActivity(it)
            }
        }
    }

    private fun fetchUser() {
            fireBaseDB.collection("pendingRequests").document(requestId)
                .get()
                .addOnSuccessListener { pendingRequest ->
                    pendingRequest?.get("ssn")?.let { ssn ->

                        fireBaseDB.collection("users")
                            .whereEqualTo("ssn", ssn as String)
                            .get()
                            .addOnSuccessListener { snapshot ->
                                val user = User(snapshot.documents[0])
                                showUser(user)
                            }
                    }
                }
    }

    private fun showUser(user: User) {
        titleTV.text = getString(R.string.fetch_journals_for_patient, user.getFullName())
    }
}