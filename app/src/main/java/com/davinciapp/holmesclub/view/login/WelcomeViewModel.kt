package com.davinciapp.holmesclub.view.login

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class WelcomeViewModel : ViewModel() {

    private val auth = FirebaseAuth.getInstance()

    val email = MutableLiveData<String>()
    val invalidInput = MutableLiveData<Boolean>()
    val alertMessage = MutableLiveData<String>()
    val isLoading = MutableLiveData<Boolean>()
    val accountSuccessfullyCreated = MutableLiveData<Boolean>()


    fun checkInputs(pseudo: String, p1: String, p2: String) {
        if (validPseudo(pseudo) && validPassword(p1, p2)){
            alertMessage.value = "Good to go !"
            isLoading.value = true
            createFirebaseAccount(p1)
        }
    }

    private fun validPseudo(pseudo: String): Boolean {

        return when {
            pseudo.length < 3 -> {
                //Too short
                triggerInvalidInputEvent()
                alertMessage.value = "Pseudo must be at least 3 character long"
                false
            }
            false -> {
                //todo check if already exist in server
                triggerInvalidInputEvent()
                alertMessage.value = "This pseudo has already been taken"
                false
            }
            else -> true
        }
    }

    private fun validPassword(p1: String, p2: String): Boolean {

        return when {
            p1 != p2 -> {
                triggerInvalidInputEvent()
                alertMessage.value = "Password confirmation failed"
                false
            }
            p1.length < 5 -> {
                //Too short
                triggerInvalidInputEvent()
                alertMessage.value = "Password is not long enough"
                false
            }
            else -> true
        }
    }

    private fun createFirebaseAccount(password: String) {
        auth.createUserWithEmailAndPassword(email.value!!, password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    accountSuccessfullyCreated.value = true
                    val user = auth.currentUser!!
                    sendEmailVerification(user)
                } else {
                    Log.w("firebase", "createFirebaseAccount: failure", it.exception)
                }
                isLoading.value = false
            }
    }

    private fun sendEmailVerification(user: FirebaseUser) {
        user.sendEmailVerification().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                alertMessage.value = "Email send"
            } else {
                Log.w("firebase", "sendEmailVerification:failure", task.exception)
            }
        }
    }

    private fun triggerInvalidInputEvent() {
        invalidInput.value = true
        invalidInput.value = false
    }

}