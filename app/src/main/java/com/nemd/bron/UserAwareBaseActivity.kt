package com.nemd.bron

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

abstract class UserAwareBaseActivity : AppCompatActivity() {

    protected val fireBaseAuth : FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onStart() {
        super.onStart()

        if (fireBaseAuth.currentUser == null) {
            goToLogin()
        }
    }

    protected fun goToLogin() {
        Intent(this, LoginActivity::class.java)
            .also {
                startActivity(it)
                finishAffinity()
            }
    }
}
