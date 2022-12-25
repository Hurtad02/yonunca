package com.example.yonunca_juegoparabeber.online.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.yonunca_juegoparabeber.online.view.SignUpUIState
import com.example.yonunca_juegoparabeber.utils.firebase.FirebaseManager
import kotlinx.coroutines.launch

class SignInViewModel: ViewModel() {

    private val model = FirebaseManager()

    private var uiState: MutableLiveData<SignUpUIState> =
        MutableLiveData<SignUpUIState>().also {
            it.value = SignUpUIState()
        }

    fun getUIState(): MutableLiveData<SignUpUIState>{
        return uiState
    }

    fun signIn(email: String, password: String){
        viewModelScope.launch {
            setLoading(true)
            try {
                val result = model.signIn(email, password)
                if (result != null){
                    uiState.postValue(currentState?.copy(result = true))
                } else {
                    uiState.postValue(currentState?.copy(signUpError = "Combinación de usuario y contraseña inválida, intente nuevamente", isLoading = false))
                }
            } catch (e: Exception){
                uiState.postValue(currentState?.copy(signUpError = e.message.toString(), isLoading = false))
            }
        }
    }

    private val currentState = uiState.value

    private fun setLoading(isLoading: Boolean){
        uiState.postValue(uiState.value?.copy(isLoading = isLoading))
    }

    fun clearError() {
        uiState.postValue(uiState.value?.copy(signUpError = null))
    }

}