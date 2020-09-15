package com.example.tasks.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.tasks.service.constants.TaskConstants
import com.example.tasks.service.listener.APIListener
import com.example.tasks.service.repository.PersonRepository
import com.example.tasks.service.repository.local.SecurityPreferences
import com.example.tasks.service.repository.model.HeaderModel
import com.example.tasks.service.repository.model.ValidationModel

class RegisterViewModel(application: Application) : AndroidViewModel(application) {

    private val mPersonRepository = PersonRepository(application)

    private val mSharedPreferences = SecurityPreferences(application)

    private val mCreate = MutableLiveData<ValidationModel>()
    var create: LiveData<ValidationModel> = this.mCreate

    fun create(name: String, email: String, password: String) {
        this.mPersonRepository.create(name, email, password, object : APIListener<HeaderModel> {
            override fun onSuccess(model: HeaderModel) {
                mSharedPreferences.store(TaskConstants.SHARED.TOKEN_KEY, model.token)
                mSharedPreferences.store(TaskConstants.SHARED.PERSON_KEY, model.personKey)
                mSharedPreferences.store(TaskConstants.SHARED.PERSON_NAME, model.name)
                mCreate.value = ValidationModel()
            }

            override fun onFailure(message: String) {
                mCreate.value = ValidationModel(message)
            }
        })
    }

}