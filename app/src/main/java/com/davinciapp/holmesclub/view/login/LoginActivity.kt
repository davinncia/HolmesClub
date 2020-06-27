package com.davinciapp.holmesclub.view.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.method.PasswordTransformationMethod
import android.view.View
import android.view.animation.CycleInterpolator
import android.view.animation.TranslateAnimation
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.ContentLoadingProgressBar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.davinciapp.holmesclub.R
import com.davinciapp.holmesclub.bind
import com.davinciapp.holmesclub.di.ViewModelFactory
import com.davinciapp.holmesclub.view.feed.FeedActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var viewModel: LoginViewModel

    private val emailTextView by bind<TextView>(R.id.tv_email_signin)
    private val emailButton by bind<ImageButton>(R.id.ib_confirm_email_login)
    private val emailEditText by bind<EditText>(R.id.et_email_signin)

    private val progressCircle by bind<ContentLoadingProgressBar>(R.id.progress_circle_login)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)


        emailButton.alpha = 0.5F

        viewModel = ViewModelProvider(this, ViewModelFactory.getInstance(application))[LoginViewModel::class.java]


        emailButton.setOnClickListener {
            //sendConfirmationEmail(emailEditText.text.toString())
            viewModel.checkEmail(emailEditText.text.toString().trim())
        }

        //OBSERVE VIEW MODEL
        viewModel.invalidEmail.observe(this, Observer {
            if (it) Toast.makeText(this, "Email format not valid", Toast.LENGTH_SHORT).show()
            shakeAnimation(emailEditText)
        })

        viewModel.invalidPassword.observe(this, Observer {
            shakeAnimation(emailEditText)
            //TODO: forgot password
        })

        viewModel.existingEmail.observe(this, Observer {
            if (it) {
                //Log in
                askForPassword()
            } else {
                //New mail
                startActivity(WelcomeActivity.newIntent(this, emailEditText.text.toString()))
            }
        })

        viewModel.isLoading.observe(this, Observer {
            setLoadingUi(it)
        })

        viewModel.alertMessage.observe(this, Observer {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        })


        viewModel.successfullyConnected.observe(this, Observer {
            startActivity(FeedActivity.newIntent(this))
            finish()
        })
    }

    //--------------------------------------------------------------------------------------------//
    //                                      PASSWORD AUTH
    //--------------------------------------------------------------------------------------------//
    private fun askForPassword() {

        emailTextView.text = "password"
        emailEditText.setText("")
        emailEditText.translationX = emailEditText.translationX + 500F
        emailEditText.animate().translationXBy(-500F).setDuration(500).start()
        emailEditText.transformationMethod = PasswordTransformationMethod()

        emailButton.setOnClickListener(null)
        emailButton.setOnClickListener {
            viewModel.checkPassword(emailEditText.text.toString().trim())
        }

    }

    private fun setLoadingUi(isLoading: Boolean) {
        if (isLoading) {
            progressCircle.show()
            emailButton.visibility = View.INVISIBLE
        } else {
            progressCircle.hide()
            emailButton.visibility = View.VISIBLE
        }
    }

    private fun shakeAnimation(view: View, duration: Long = 500L) {
        val shake = TranslateAnimation(0F, 10F, 0F, 0F).apply {
            this.duration = duration
            this.interpolator = CycleInterpolator(7F)
        }
        view.startAnimation(shake)
    }


    //--------------------------------------------------------------------------------------------//
    //                                      PASSWORD LESS
    //--------------------------------------------------------------------------------------------//
    /*
    private fun sendConfirmationEmail(email: String) {
        val actionCodeSettings = ActionCodeSettings.newBuilder()
            .setUrl("https://holmesclub.page.link/sign")
            .setHandleCodeInApp(true)
            .setAndroidPackageName(resources.getString(R.string.package_name), true, "12")
            .build()

        val auth = FirebaseAuth.getInstance()
        auth.sendSignInLinkToEmail(email, actionCodeSettings)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) Toast.makeText(this, "Email send", Toast.LENGTH_SHORT).show()
                else Toast.makeText(this, "Failure.", Toast.LENGTH_SHORT).show()
            }
    }

     */

    companion object {
        fun newIntent(context: Context) = Intent(context, LoginActivity::class.java)
    }
}