package com.example.yonunca_juegoparabeber.utils.firebase

import android.R.attr.password
import android.content.ContentValues.TAG
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.firebase.ui.auth.AuthUI
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await


class FirebaseManager() {

    private val auth = Firebase.auth
    private val currentUser = MutableLiveData<FirebaseUser>().also {
        getUserData()
    }

    fun getCurrentUser(): LiveData<FirebaseUser> = currentUser

    private fun getLoginProviders(): ArrayList<AuthUI.IdpConfig> =
        arrayListOf(
            AuthUI.IdpConfig.GoogleBuilder().build()
        )

    fun startSignInRequest(signInLauncher: ActivityResultLauncher<Intent>) {
        val signInIntent = AuthUI.getInstance()
            .createSignInIntentBuilder()
            .setAvailableProviders(getLoginProviders())
            .build()
        signInLauncher.launch(signInIntent)
    }

    private fun getUserData() {
        auth.addAuthStateListener {
            if (it.currentUser != null) {
                currentUser.postValue(it.currentUser)
            }
        }
    }

    fun isLoggedIn(): Boolean {
        return auth.currentUser != null
    }

    fun getUserEmail(): String {
        return currentUser.value!!.email!!
    }

    suspend fun createUser(email: String, password: String): FirebaseUser? {
        return auth.createUserWithEmailAndPassword(email, password)
            .await().user
    }

    suspend fun signIn(email: String, password: String): FirebaseUser? {
        return auth.signInWithEmailAndPassword(email, password)
            .await().user
    }
}

















