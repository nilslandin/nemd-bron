package com.nemd.bron

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

abstract class UserAwareBaseActivity : AppCompatActivity() {

    override fun onStart() {
        super.onStart()

        if (FirebaseAuth.getInstance().currentUser == null) {
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
