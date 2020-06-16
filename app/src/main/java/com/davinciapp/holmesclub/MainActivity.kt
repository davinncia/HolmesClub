package com.davinciapp.holmesclub

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.davinciapp.holmesclub.feed.FeedActivity


class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Check login etc...
        startActivity(FeedActivity.newIntent(this))

        //startActivity(WritingActivity.newIntent(this))

    }





}
