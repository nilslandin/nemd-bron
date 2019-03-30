package com.nemd.bron.hcp

import android.os.Bundle
import com.nemd.bron.AbstractMainActivity
import com.nemd.bron.R
import com.nemd.bron.model.HCP
import com.nemd.bron.network.FireBaseHelper
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_hcp_main.*
import timber.log.Timber

class HcpMainActivity : AbstractMainActivity() {

    private var hcp: HCP? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hcp_main)
    }

    override fun onStart() {
        super.onStart()

        getUser()

        createPendingRequest.setOnClickListener {
            currentUser?.uid?.let { userId ->
                FireBaseHelper.getFireBaseService().addPendingRequest("191212121212", userId)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                        {
                            Timber.d("Sent request")
                        },
                        {
                            Timber.e(it)
                        }
                    )
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
}