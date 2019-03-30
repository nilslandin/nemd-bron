package com.nemd.bron

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.nemd.bron.hcp.HcpMainActivity
import com.nemd.bron.login.LoginActivity
import com.nemd.bron.patient.PatientMainActivity

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val firebaseAuth = FirebaseAuth.getInstance()

        val currentUser = firebaseAuth.currentUser

        val intent =
            if (currentUser != null) {
                if (SharedPreferenceHelper.getUserType(this) == UserType.PATIENT) {
                    Intent(this, PatientMainActivity::class.java)
                }
                else {
                    Intent(this, HcpMainActivity::class.java)
                }
            } else {
                Intent(this, LoginActivity::class.java)
            }

        startActivity(intent)
        finish()
    }
}