package com.nemd.bron

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*


class MainActivity : AppCompatActivity() {

    companion object {
        private const val RC_SIGN_IN = 1
    }

    private var fireBaseAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        updateUI()
    }

    private fun updateUI() {
        if (fireBaseAuth.currentUser == null) {
            showSignIn()
        }
        else {
            showSignedIn()
        }
    }

    private fun showSignIn() {
        val providers = arrayListOf(
            AuthUI.IdpConfig.EmailBuilder().build())

        startActivityForResult(
            AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .build(),
            RC_SIGN_IN)
    }

    private fun showSignedIn() {
        val currentUser = fireBaseAuth.currentUser

        loggedInContainer.visibility = View.VISIBLE

        nameTV.text = currentUser?.email

        signOutBtn.setOnClickListener {
            fireBaseAuth.signOut()
            updateUI()
        }
    }
}
