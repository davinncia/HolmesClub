package com.davinciapp.holmesclub.view.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.davinciapp.holmesclub.R
import com.davinciapp.holmesclub.bind
import com.davinciapp.holmesclub.di.ViewModelFactory
import com.davinciapp.holmesclub.view.feed.FeedActivity

class EmailActivity : AppCompatActivity() {


    private val emailTextView by bind<TextView>(R.id.tv_email_email)
    private val sendEmailButton by bind<Button>(R.id.btn_send_again_email)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_email)

        val viewModel = ViewModelProvider(this, ViewModelFactory.getInstance(application))[EmailViewModel::class.java]

        viewModel.checkEmailValidation()

        sendEmailButton.setOnClickListener {
            viewModel.sendEmailVerification()
        }

        viewModel.userEmail.observe(this, Observer {
            emailTextView.text = it
        })

        viewModel.isValidated.observe(this, Observer {
            if (it) {
                startActivity(FeedActivity.newIntent(this))
                finish()
            }
        })

        viewModel.emailSend.observe(this, Observer {
            val message =
                if (it) "Email send"
                else "Failed to send email"
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        })
    }

    companion object {
        fun newIntent(context: Context) = Intent(context, EmailActivity::class.java)
    }
}