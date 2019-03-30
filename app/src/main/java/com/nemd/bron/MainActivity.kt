package com.nemd.bron

import android.os.Bundle
import android.view.View
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : UserAwareBaseActivity() {
    
    private var fireBaseAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        updateUI()
    }

    private fun updateUI() {
        val currentUser = fireBaseAuth.currentUser

        loggedInContainer.visibility = View.VISIBLE

        nameTV.text = currentUser?.email

        signOutBtn.setOnClickListener {
            fireBaseAuth.signOut()
            goToLogin()
        }
    }
}
