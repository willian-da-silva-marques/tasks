package com.example.tasks.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.tasks.service.constants.TaskConstants
import com.example.tasks.service.listener.APIListener
import com.example.tasks.service.repository.PersonRepository
import com.example.tasks.service.repository.PriorityRepository
import com.example.tasks.service.repository.http.RetrofitClient
import com.example.tasks.service.repository.local.SecurityPreferences
import com.example.tasks.service.repository.model.HeaderModel
import com.example.tasks.service.repository.model.ValidationModel

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    private val mPersonRepository = PersonRepository(application)
    private val mPriorityRepository = PriorityRepository(application)

    private val mSharedPreferences = SecurityPreferences(application)

    private val mLogin = MutableLiveData<ValidationModel>()
    var login: LiveData<ValidationModel> = this.mLogin

    private val mLoggedUser = MutableLiveData<Boolean>()
    var loggedUser: LiveData<Boolean> = this.mLoggedUser

    /**
     * Faz login usando API
     */
    fun doLogin(email: String, password: String) {
        this.mPersonRepository.login(email, password, object : APIListener<HeaderModel> {
            override fun onSuccess(model: HeaderModel) {
                mSharedPreferences.store(TaskConstants.SHARED.TOKEN_KEY, model.token)
                mSharedPreferences.store(TaskConstants.SHARED.PERSON_KEY, model.personKey)
                mSharedPreferences.store(TaskConstants.SHARED.PERSON_NAME, model.name)
                RetrofitClient.addHeader(model.token, model.personKey)
                mLogin.value = ValidationModel()
            }

            override fun onFailure(message: String) {
                mLogin.value = ValidationModel(message)
            }
        })
    }

    /**
     * Verifica se usuário está logado
     */
    fun verifyLoggedUser() {
        val token = this.mSharedPreferences.get(TaskConstants.SHARED.TOKEN_KEY)
        val personKey = this.mSharedPreferences.get(TaskConstants.SHARED.PERSON_KEY)
        RetrofitClient.addHeader(token, personKey)
        val logged = (token.isNotBlank() && personKey.isNotBlank())

        if (!logged) {
            this.mPriorityRepository.findAll()
        }

        this.mLoggedUser.value = logged
    }

}