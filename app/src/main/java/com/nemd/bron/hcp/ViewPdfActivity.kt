package com.nemd.bron.hcp

import android.os.Bundle
import com.nemd.bron.R
import com.nemd.bron.UserAwareBaseActivity

class ViewPdfActivity : UserAwareBaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_pdf)

        supportActionBar?.title = "Journal View"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }
}
