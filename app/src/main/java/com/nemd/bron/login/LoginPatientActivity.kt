package com.nemd.bron.login

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.nemd.bron.patient.PatientMainActivity
import com.nemd.bron.R
import com.nemd.bron.SharedPreferenceHelper
import com.nemd.bron.UserType
import kotlinx.android.synthetic.main.activity_login_patient.*
import timber.log.Timber

class LoginPatientActivity : AppCompatActivity() {

    private val firebaseAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_patient)

        loginBtn.setOnClickListener {
            login()
        }

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    fun login() {
        val ssn = ssnET.text.toString()
        val email =
            when (ssn) {
                "191212121212" -> "pontus.thome@gmail.com"
                else -> ""
            }
        val password = passwordET.text.toString()

        Timber.d("Login %s %s", email, password)

        loading.visibility = View.VISIBLE
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Timber.d("signInWithEmail:success")

                    loading.visibility = View.GONE

                    SharedPreferenceHelper.setUserType(this, UserType.PATIENT)
                    Intent(this, PatientMainActivity::class.java)
                        .also {
                            startActivity(it)
                            finishAffinity()
                        }
                } else {
                    loading.visibility = View.GONE
                    // If sign in fails, display a message to the user.
                    Timber.e(task.exception, "signInWithEmail:failure")
                    Toast.makeText(baseContext, "Authentication failed.", Toast.LENGTH_SHORT).show()
                }
            }
    }
}