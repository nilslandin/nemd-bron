package com.nemd.bron.hcp

import android.os.Bundle
import android.widget.Toast
import com.nemd.bron.R
import com.nemd.bron.UserAwareBaseActivity
import com.nemd.bron.network.FireBaseHelper
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_hcp_request_data.*
import timber.log.Timber

class HcpRequestDataActivity : UserAwareBaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hcp_request_data)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        sendRequestBtn.setOnClickListener {
            fireBaseAuth.currentUser?.uid?.let { userId ->
                FireBaseHelper.getFireBaseService().addPendingRequest(ssnET.text.toString(), userId)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                        {
                            Timber.d("Sent request")
                            setResult(RESULT_OK)
                            finish()
                        },
                        {
                            Timber.e(it)
                            Toast.makeText(this, "Unable to create request", Toast.LENGTH_LONG).show()
                        }
                    )
            }
        }
    }
}
