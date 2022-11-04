package com.example.thesis.viewModels

import androidx.lifecycle.*
import com.example.thesis.repository.AuthRepository
import com.example.thesis.views.auth.ResponseStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch


class LoginViewModel: ViewModel(){

    private var authRepository = AuthRepository()

    var authenticatedStateFlow = MutableStateFlow<ResponseStatus>(ResponseStatus.Init)
    var registerUserStateFlow = MutableStateFlow<ResponseStatus>(ResponseStatus.Init)

    var mEmail = MutableLiveData<String>()
    var mPassword = MutableLiveData<String>()
    var mPasswordAgain = MutableLiveData<String>()
    var mUsername = MutableLiveData<String>()


    fun login() = viewModelScope.launch {
        authRepository.signIn(mEmail.value.toString(), mPassword.value.toString()).collect{ value ->
            authenticatedStateFlow.value = value
        }
    }

    fun register() = viewModelScope.launch {
        authRepository.register(
            mUsername.value.toString(),
            mEmail.value.toString(),
            mPassword.value.toString()
        ).collect { values ->
            registerUserStateFlow.value = values
        }
    }


    fun sendPasswordResetEmail(){
        mEmail.value?.let {
            authRepository.sendPasswordResetEmail(it)
        }
    }

}