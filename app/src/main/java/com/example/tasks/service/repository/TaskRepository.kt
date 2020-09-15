package com.example.tasks.service.repository

import android.content.Context
import com.example.tasks.R
import com.example.tasks.service.constants.TaskConstants
import com.example.tasks.service.listener.APIListener
import com.example.tasks.service.repository.http.RetrofitClient
import com.example.tasks.service.repository.http.TaskService
import com.example.tasks.service.repository.model.TaskModel
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TaskRepository(context: Context): BaseRepository(context) {

    private val mTaskService = RetrofitClient.createService(TaskService::class.java)

    fun save(task: TaskModel, listener : APIListener<Boolean>) {
        if (!isConnectionAvailable(context)) {
            listener.onFailure(context.getString(R.string.ERROR_INTERNET_CONNECTION))
            return
        }

        val call: Call<Boolean> = this.mTaskService.create(task.priorityId, task.description,task.dueDate, task.complete)
        call.enqueue(object : Callback<Boolean> {
            override fun onFailure(call: Call<Boolean>, t: Throwable) {
                listener.onFailure(context.getString(R.string.ERROR_UNEXPECTED))
            }

            override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
                if (response.code() != TaskConstants.HTTP.SUCCESS) {
                    val message = Gson().fromJson(response.errorBody()!!.string(), String::class.java)
                    listener.onFailure(message)
                } else {
                    response.body()?.let { listener.onSuccess(it) }
                }
            }
        })
    }

    fun findAll(listener: APIListener<List<TaskModel>>) {
        val call: Call<List<TaskModel>> = this.mTaskService.findAll()
        this.find(call, listener)
    }

    fun findAllNextSevenDays(listener: APIListener<List<TaskModel>>) {
        val call: Call<List<TaskModel>> = this.mTaskService.findAllNextSevenDays()
        this.find(call, listener)
    }

    fun findAllExpireds(listener: APIListener<List<TaskModel>>) {
        val call: Call<List<TaskModel>> = this.mTaskService.findAllExpireds()
        this.find(call, listener)
    }

    fun findById(id: Int, listener: APIListener<TaskModel>) {
        if (!isConnectionAvailable(context)) {
            listener.onFailure(context.getString(R.string.ERROR_INTERNET_CONNECTION))
            return
        }
        val call: Call<TaskModel> = this.mTaskService.findById(id)
        call.enqueue(object : Callback<TaskModel> {
            override fun onFailure(call: Call<TaskModel>, t: Throwable) {
                listener.onFailure(context.getString(R.string.ERROR_UNEXPECTED))
            }

            override fun onResponse(call: Call<TaskModel>, response: Response<TaskModel>) {
                if (response.code() != TaskConstants.HTTP.SUCCESS) {
                    val message = Gson().fromJson(response.errorBody()!!.string(), String::class.java)
                    listener.onFailure(message)
                } else {
                    response.body()?.let { listener.onSuccess(it) }
                }
            }
        })
    }

    fun update(task: TaskModel, listener: APIListener<Boolean>) {
        if (!isConnectionAvailable(context)) {
            listener.onFailure(context.getString(R.string.ERROR_INTERNET_CONNECTION))
            return
        }
        val call: Call<Boolean> = this.mTaskService.update(task.id, task.priorityId, task.description,task.dueDate, task.complete)
        call.enqueue(object : Callback<Boolean> {
            override fun onFailure(call: Call<Boolean>, t: Throwable) {
                listener.onFailure(context.getString(R.string.ERROR_UNEXPECTED))
            }

            override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
                if (response.code() != TaskConstants.HTTP.SUCCESS) {
                    val message = Gson().fromJson(response.errorBody()!!.string(), String::class.java)
                    listener.onFailure(message)
                } else {
                    response.body()?.let { listener.onSuccess(it) }
                }
            }
        })
    }

    fun updateStatus(id: Int, complete: Boolean, listener: APIListener<Boolean>) {

        val call = if (complete) {
            this.mTaskService.complete(id)
        } else {
            this.mTaskService.undo(id)
        }

        if (!isConnectionAvailable(context)) {
            listener.onFailure(context.getString(R.string.ERROR_INTERNET_CONNECTION))
            return
        }

        call.enqueue(object : Callback<Boolean> {
            override fun onFailure(call: Call<Boolean>, t: Throwable) {
                listener.onFailure(context.getString(R.string.ERROR_UNEXPECTED))
            }

            override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
                if (response.code() != TaskConstants.HTTP.SUCCESS) {
                    val message = Gson().fromJson(response.errorBody()!!.string(), String::class.java)
                    listener.onFailure(message)
                } else {
                    response.body()?.let { listener.onSuccess(it) }
                }
            }
        })

    }

    fun delete(id: Int, listener: APIListener<Boolean>) {
        if (!isConnectionAvailable(context)) {
            listener.onFailure(context.getString(R.string.ERROR_INTERNET_CONNECTION))
            return
        }
        val call: Call<Boolean> = this.mTaskService.delete(id)
        call.enqueue(object : Callback<Boolean> {
            override fun onFailure(call: Call<Boolean>, t: Throwable) {
                listener.onFailure(context.getString(R.string.ERROR_UNEXPECTED))
            }

            override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
                if (response.code() != TaskConstants.HTTP.SUCCESS) {
                    val message = Gson().fromJson(response.errorBody()!!.string(), String::class.java)
                    listener.onFailure(message)
                } else {
                    response.body()?.let { listener.onSuccess(it) }
                }
            }
        })
    }

    private fun find(call: Call<List<TaskModel>>, listener: APIListener<List<TaskModel>>) {
        if (!isConnectionAvailable(context)) {
            listener.onFailure(context.getString(R.string.ERROR_INTERNET_CONNECTION))
            return
        }
        call.enqueue(object : Callback<List<TaskModel>> {
            override fun onFailure(call: Call<List<TaskModel>>, t: Throwable) {
                listener.onFailure(context.getString(R.string.ERROR_UNEXPECTED))
            }

            override fun onResponse(call: Call<List<TaskModel>>, response: Response<List<TaskModel>>) {
                if (response.code() != TaskConstants.HTTP.SUCCESS) {
                    val message = Gson().fromJson(response.errorBody()!!.string(), String::class.java)
                    listener.onFailure(message)
                } else {
                    response.body()?.let { listener.onSuccess(it) }
                }
            }
        })
    }
}