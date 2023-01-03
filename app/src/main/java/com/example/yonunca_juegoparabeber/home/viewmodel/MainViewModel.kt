package com.example.yonunca_juegoparabeber.home.viewmodel

import android.content.Intent
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import com.example.yonunca_juegoparabeber.base.BaseApplication.Companion.getApplicationContext
import com.example.yonunca_juegoparabeber.online.view.OnlineScreenUIState
import com.example.yonunca_juegoparabeber.utils.firebase.FirebaseManager
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch

class MainViewModel: ViewModel() {

    private val firebaseManager = FirebaseManager()
    private var uiState: MutableLiveData<OnlineScreenUIState> =
        MutableLiveData<OnlineScreenUIState>().also {
            it.value = OnlineScreenUIState(
                user = getCurrentUser(),
                isOnline = firebaseManager.isLoggedIn()
            )
        }

    init {
        viewModelScope.launch {
            firebaseManager.getCurrentUser().asFlow().collect {
                updateCurrentUser()
            }
        }
    }

    fun getUIState(): MutableLiveData<OnlineScreenUIState> {
        return uiState
    }

    private fun getCurrentUser(): FirebaseUser? =
        FirebaseAuth.getInstance().currentUser

    private fun updateCurrentUser() {
        uiState.value = uiState.value?.copy(
            user = getCurrentUser(),
            isOnline = firebaseManager.isLoggedIn()
        )
    }

    fun tryToLogin(signInLauncher: ActivityResultLauncher<Intent>) {
        firebaseManager.startSignInRequest(signInLauncher)
    }

    fun onSignInResult(result: FirebaseAuthUIAuthenticationResult) {

        if (result.resultCode == AppCompatActivity.RESULT_OK) {
            // Successfully signed in
            updateCurrentUser()
        } else {
            uiState.value = uiState.value?.copy(messageToShow = "Error al iniciar sesion")
        }
    }

    fun signOut() {
        Firebase.auth.signOut()
        updateCurrentUser()
    }

    fun deleteAccount() {
        Firebase.auth.currentUser!!
            .delete()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    Toast.makeText(getApplicationContext(), "La cuenta ha sido eliminada", Toast.LENGTH_LONG).show()
                    updateCurrentUser()
                }
            }
    }
}