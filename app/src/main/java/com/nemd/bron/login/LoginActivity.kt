package com.nemd.bron.login

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.nemd.bron.R
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        signInPatientBtn.setOnClickListener {
            Intent(this, LoginPatientActivity::class.java)
                .also {
                    startActivity(it)
                }
        }

        signInHealthCareProviderBtn.setOnClickListener {
            Intent(this, LoginHealthCareActivity::class.java)
                .also {
                    startActivity(it)
                }
        }
    }
}