package com.davinciapp.holmesclub.view.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.method.PasswordTransformationMethod
import android.view.View
import android.view.animation.CycleInterpolator
import android.view.animation.TranslateAnimation
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.ContentLoadingProgressBar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.davinciapp.holmesclub.R
import com.davinciapp.holmesclub.bind
import com.davinciapp.holmesclub.di.ViewModelFactory

class WelcomeActivity : AppCompatActivity() {

    private lateinit var viewModel: WelcomeViewModel

    private val pseudoEditText by bind<EditText>(R.id.et_pseudo_welcome)
    private val passwordEditText by bind<EditText>(R.id.et_password_welcome)
    private val confirmPasswordEditText by bind<EditText>(R.id.et_password_confirm_welcome)
    private val accountButton by bind<Button>(R.id.btn_create_account_welcome)
    private val progressCircle by bind<ContentLoadingProgressBar>(R.id.progress_circle_welcome)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        passwordEditText.transformationMethod = PasswordTransformationMethod()
        confirmPasswordEditText.transformationMethod = PasswordTransformationMethod()

        viewModel = ViewModelProvider(this, ViewModelFactory.getInstance(application))[WelcomeViewModel::class.java]

        intent.getStringExtra(EMAIL_EXTRA)?.let {
            viewModel.email.value = it
        }

        accountButton.setOnClickListener {
            viewModel.checkInputs(pseudoEditText.text.toString().trim(), passwordEditText.text.toString().trim(), confirmPasswordEditText.text.toString().trim())
        }

        //VM
        viewModel.email.observe(this, Observer {
            findViewById<TextView>(R.id.tv_email_welcome).text = it
        })

        viewModel.invalidInput.observe(this, Observer {
            if (it) shakeAnimation(accountButton)
        })

        viewModel.alertMessage.observe(this, Observer {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        })

        viewModel.isLoading.observe(this, Observer {
            if (it) progressCircle.show()
            else progressCircle.hide()
        })

        viewModel.accountSuccessfullyCreated.observe(this, Observer {
            startActivity(EmailActivity.newIntent(this))
            finish()
        })


    }


    private fun alertWithToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun shakeAnimation(view: View, duration: Long = 500L) {
        val shake = TranslateAnimation(0F, 10F, 0F, 0F).apply {
            this.duration = duration
            this.interpolator = CycleInterpolator(7F)
        }
        view.startAnimation(shake)
    }

    companion object {
        private const val EMAIL_EXTRA = "email_extra"

        fun newIntent(context: Context, email: String): Intent {
            val intent = Intent(context, WelcomeActivity::class.java)
            intent.putExtra(EMAIL_EXTRA, email)
            return intent
        }
    }
}

