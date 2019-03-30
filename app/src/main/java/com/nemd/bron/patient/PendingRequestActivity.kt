package com.nemd.bron.patient

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.nemd.bron.R
import com.nemd.bron.UserAwareBaseActivity
import com.nemd.bron.network.FireBaseHelper
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_pending_request.*
import timber.log.Timber

class PendingRequestActivity : UserAwareBaseActivity() {

    companion object {
        const val REQUEST_ID_EXTRA = "REQUEST_ID_EXTRA"
    }

    private lateinit var requestId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pending_request)

        if (!intent.hasExtra(REQUEST_ID_EXTRA)) {
            goToMain()
        }
        else {
            requestId = intent.getStringExtra(REQUEST_ID_EXTRA)

            approveBtn.setOnClickListener{
                sendConsent(true)
            }
            declineBtn.setOnClickListener{
                sendConsent(false)
            }
        }
    }

    private var consentDisposable: Disposable? = null

    private fun sendConsent(consentApproved: Boolean) {
        loading.visibility = View.VISIBLE

        consentDisposable?.dispose()
        consentDisposable = FireBaseHelper.getFireBaseService().sendConsent(requestId, consentApproved.toString())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    Timber.d("Success")
                    loading.visibility = View.GONE
                    goToMain()
                },
                {
                    Timber.e("Failed")
                    loading.visibility = View.GONE
                }
            )
    }

    private fun goToMain() {
        Intent(this, PatientMainActivity::class.java)
            .also {
                startActivity(it)
                finish()
            }
    }

    override fun onDestroy() {
        consentDisposable?.dispose()
        super.onDestroy()
    }
}