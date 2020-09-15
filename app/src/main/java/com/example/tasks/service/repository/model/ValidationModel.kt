package com.example.tasks.service.repository.model

class ValidationModel(message: String = "") {

    private var mStatus: Boolean = true
    private var mMessage: String = ""

    init {
        if (message != "") {
            this.mStatus = false
            this.mMessage = message
        }
    }

    fun isSuccessful() = this.mStatus

    fun getMessage() = this.mMessage
}