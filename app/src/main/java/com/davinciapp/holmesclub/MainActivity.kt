package com.davinciapp.holmesclub

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.davinciapp.holmesclub.view.feed.FeedActivity
import com.davinciapp.holmesclub.view.login.LoginActivity
import com.google.firebase.auth.FirebaseAuth


class MainActivity : AppCompatActivity() {

    private val auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //DEBUG
        //auth.signOut()

        //Check if logged in
        val intent: Intent =
            if (auth.currentUser != null) FeedActivity.newIntent(this)
            else LoginActivity.newIntent(this)

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
        finish()

        //startActivity(WritingActivity.newIntent(this))

    }


}
