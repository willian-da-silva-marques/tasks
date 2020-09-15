package com.example.tasks.service.repository

import android.content.Context
import com.example.tasks.service.constants.TaskConstants
import com.example.tasks.service.repository.http.PriorityService
import com.example.tasks.service.repository.http.RetrofitClient
import com.example.tasks.service.repository.local.TaskDatabase
import com.example.tasks.service.repository.model.PriorityModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PriorityRepository(context: Context): BaseRepository(context) {

    private val mPriorityService = RetrofitClient.createService(PriorityService::class.java)
    private val mPriorityDatabase = TaskDatabase.getDatabase(context).priorityDAO()

    fun findAll() {

        if (!isConnectionAvailable(context)) {
            return
        }

        val call: Call<List<PriorityModel>> = this.mPriorityService.findAll()
        call.enqueue(object : Callback<List<PriorityModel>> {
            override fun onFailure(call: Call<List<PriorityModel>>, t: Throwable) {}

            override fun onResponse(call: Call<List<PriorityModel>>, response: Response<List<PriorityModel>>) {
                if (response.code() == TaskConstants.HTTP.SUCCESS) {
                    mPriorityDatabase.deleteAll()
                    response.body()?.let { mPriorityDatabase.saveAll(it) }
                }
            }
        })
    }

    fun findAllSaved() = this.mPriorityDatabase.findAll()

    fun findById(id: Int) = this.mPriorityDatabase.findById(id)
}