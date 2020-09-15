package com.example.tasks.service.repository.http

import com.example.tasks.service.repository.model.PriorityModel
import retrofit2.Call
import retrofit2.http.GET

interface PriorityService {

    @GET("Priority")
    fun findAll(): Call<List<PriorityModel>>
}