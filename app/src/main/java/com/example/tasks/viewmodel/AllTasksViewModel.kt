package com.example.tasks.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tasks.service.listener.APIListener
import com.example.tasks.service.repository.TaskRepository
import com.example.tasks.service.repository.model.TaskModel
import com.example.tasks.service.repository.model.ValidationModel

class AllTasksViewModel(application: Application) : AndroidViewModel(application) {

    private val mTaskRepository = TaskRepository(application)

    private val mFindAll = MutableLiveData<List<TaskModel>>()
    var findAll: LiveData<List<TaskModel>> = this.mFindAll

    private val mValidation = MutableLiveData<ValidationModel>()
    var validation: LiveData<ValidationModel> = this.mValidation

    fun findAll() {
        this.mTaskRepository.findAll(object : APIListener<List<TaskModel>> {
            override fun onSuccess(model: List<TaskModel>) {
                mFindAll.value = model
            }

            override fun onFailure(message: String) {
                mFindAll.value = arrayListOf()
            }
        })
    }

    fun complete(id: Int) {
        this.updateStatus(id, true)
    }

    fun undo(id: Int) {
        this.updateStatus(id, false)
    }

    fun delete(id: Int) {
        this.mTaskRepository.delete(id, object : APIListener<Boolean> {
            override fun onSuccess(model: Boolean) {
                findAll()
                mValidation.value = ValidationModel()
            }

            override fun onFailure(message: String) {
                mValidation.value = ValidationModel(message)
            }
        })
    }

    private fun updateStatus(id: Int, complete: Boolean) {
        this.mTaskRepository.updateStatus(id, complete, object : APIListener<Boolean> {
            override fun onSuccess(model: Boolean) {
                findAll()
            }

            override fun onFailure(message: String) {}
        })
    }

}