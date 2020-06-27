package com.davinciapp.holmesclub.view.login

import android.util.Log
import androidx.core.util.PatternsCompat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuth

class LoginViewModel : ViewModel() {

    private val auth = FirebaseAuth.getInstance()


    //EVENTS
    val invalidEmail = MutableLiveData<Boolean>()
    val existingEmail = MutableLiveData<Boolean>()
    val invalidPassword = MutableLiveData<Boolean>()
    val isLoading = MutableLiveData<Boolean>()
    val alertMessage = MutableLiveData<String>()
    val successfullyConnected = MutableLiveData<Boolean>()

    //VALUES
    private var mEmail = ""

    fun checkEmail(email: String) {
        //FORMAT
        if (PatternsCompat.EMAIL_ADDRESS.matcher(email).matches()) {
            //Valid
            checkKnownEmail(email)
            mEmail = email
        } else {
            //Alert view
            triggerInvalidEmailEvent()
        }
    }


    fun checkPassword(password: String) {

        if (password.length >= 5) {
            //CONNECT
            logInUser(password)
        } else {
            triggerInvalidPasswordEvent()
        }
    }

    //--------------------------------------------------------------------------------------------//
    //                                      F I R E    B A S E
    //--------------------------------------------------------------------------------------------//
    private fun checkKnownEmail(email: String) {
        isLoading.value = true

        auth.fetchSignInMethodsForEmail(email).addOnCompleteListener {

            if (it.isSuccessful) {
                if (it.result?.signInMethods.isNullOrEmpty()) {
                    existingEmail.value = false
                    Log.d("debuglog", "Unknown")
                } else {
                    existingEmail.value = true
                    Log.d("debuglog", "Already in db")
                }
            } else {
                Log.w("Firebase", "checkKnownEmail: failure", it.exception)
            }
            isLoading.value = false

        }
    }

    private fun logInUser(password: String) {
        isLoading.value = true

        auth.signInWithEmailAndPassword(mEmail, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    alertMessage.value = "Success !"
                    successfullyConnected.value = true
                } else {
                    triggerInvalidPasswordEvent()
                    Log.w("Firebase", "logInUser: failure", task.exception)

                    if (task.exception is FirebaseTooManyRequestsException){
                        alertMessage.value = "Too many request, please try again later"
                    }
                }
                isLoading.value = false
            }
    }

    //--------------------------------------------------------------------------------------------//
    //                                           E V E N T S
    //--------------------------------------------------------------------------------------------//
    private fun triggerInvalidEmailEvent() {
        invalidEmail.value = true
        invalidEmail.value = false
    }

    private fun triggerInvalidPasswordEvent() {
        invalidPassword.value = true
        invalidPassword.value = false
    }

}