package com.davinciapp.holmesclub.view.login

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth

class EmailViewModel(firebaseAuth: FirebaseAuth) : ViewModel() {

    private val user = firebaseAuth.currentUser!!

    val userEmail = MutableLiveData<String>(user.email)
    val isValidated = MutableLiveData<Boolean>()
    val emailSend = MutableLiveData<Boolean>()

    init {
        sendEmailVerification()
    }

    fun checkEmailValidation() {
        isValidated.value = user.isEmailVerified
    }

    fun sendEmailVerification() {
        user.sendEmailVerification().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                emailSend.value = true
            } else {
                Log.w("firebase", "sendEmailVerification:failure", task.exception)
                emailSend.value = false
            }
        }
    }

}