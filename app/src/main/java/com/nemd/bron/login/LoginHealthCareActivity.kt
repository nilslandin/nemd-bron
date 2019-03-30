package com.nemd.bron.login

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.nemd.bron.patient.PatientMainActivity
import com.nemd.bron.R
import com.nemd.bron.SharedPreferenceHelper
import com.nemd.bron.UserType
import com.nemd.bron.hcp.HcpMainActivity
import kotlinx.android.synthetic.main.activity_login_hcp.*
import timber.log.Timber

class LoginHealthCareActivity : AppCompatActivity() {


    private val firebaseAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_hcp)

        loginBtn.setOnClickListener {
            login()
        }
    }

    fun login() {
        val email = emailET.text.toString()
        val password = passwordET.text.toString()

        Timber.d("Login %s %s", email, password)

        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Timber.d("signInWithEmail:success")

                    SharedPreferenceHelper.setUserType(this, UserType.HCP)

                    Intent(this, HcpMainActivity::class.java)
                        .also {
                            startActivity(it)
                            finishAffinity()
                        }
                } else {
                    // If sign in fails, display a message to the user.
                    Timber.e(task.exception, "signInWithEmail:failure")
                    Toast.makeText(baseContext, "Authentication failed.", Toast.LENGTH_SHORT).show()
                }
            }
    }
}