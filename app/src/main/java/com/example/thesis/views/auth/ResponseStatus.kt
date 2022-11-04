package com.example.thesis.views.auth

sealed class ResponseStatus {
    object Init: ResponseStatus()

    object Loading: ResponseStatus()

    data class Success(
        val successMessage: String
    ): ResponseStatus()

    data class Failure(
        val errorMessage: String
    ): ResponseStatus()

}