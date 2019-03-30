package com.nemd.bron

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*
import timber.log.Timber

class LoginActivity : AppCompatActivity() {

    private val firebaseAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        loginBtn.setOnClickListener {
            login()
        }
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

        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Timber.d("signInWithEmail:success")

                    Intent(this, MainActivity::class.java)
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