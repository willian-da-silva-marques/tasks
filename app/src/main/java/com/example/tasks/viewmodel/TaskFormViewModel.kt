package com.example.tasks.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.tasks.service.listener.APIListener
import com.example.tasks.service.repository.PriorityRepository
import com.example.tasks.service.repository.TaskRepository
import com.example.tasks.service.repository.model.PriorityModel
import com.example.tasks.service.repository.model.TaskModel
import com.example.tasks.service.repository.model.ValidationModel

class TaskFormViewModel(application: Application) : AndroidViewModel(application) {

    private val mPriorityRepository = PriorityRepository(application)
    private val mTaskRepository = TaskRepository(application)

    private val mPriorities = MutableLiveData<List<PriorityModel>>()
    var priorities: LiveData<List<PriorityModel>> = this.mPriorities

    private val mSave = MutableLiveData<ValidationModel>()
    var save: LiveData<ValidationModel> = this.mSave

    private val mFinded = MutableLiveData<TaskModel>()
    var finded: LiveData<TaskModel> = this.mFinded

    fun fillPriorities() {
        this.mPriorities.value = this.mPriorityRepository.findAllSaved()
    }

    fun save(task: TaskModel) {
        if (task.id == 0) {
            this.mTaskRepository.save(task, object : APIListener<Boolean> {
                override fun onSuccess(model: Boolean) {
                    mSave.value = ValidationModel()
                }

                override fun onFailure(message: String) {
                    mSave.value = ValidationModel(message)
                }
            })
        } else {
            this.mTaskRepository.update(task, object : APIListener<Boolean> {
                override fun onSuccess(model: Boolean) {
                    mSave.value = ValidationModel()
                }

                override fun onFailure(message: String) {
                    mSave.value = ValidationModel(message)
                }
            })
        }
    }

    fun findById(id: Int) {
        this.mTaskRepository.findById(id, object : APIListener<TaskModel> {
            override fun onSuccess(model: TaskModel) {
                mFinded.value = model
            }

            override fun onFailure(message: String) {}
        })
    }


}