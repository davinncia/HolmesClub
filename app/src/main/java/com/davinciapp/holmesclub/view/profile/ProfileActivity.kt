package com.davinciapp.holmesclub.view.profile

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.davinciapp.holmesclub.R
import com.davinciapp.holmesclub.bind
import com.google.android.material.appbar.CollapsingToolbarLayout

class ProfileActivity : AppCompatActivity() {

    private val authorView by bind<CollapsingToolbarLayout>(R.id.collapsing_toolbar_profile)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        authorView.title = "Mike Dillighan"
    }

    companion object {
        fun newIntent(context: Context) = Intent(context, ProfileActivity::class.java)
    }
}