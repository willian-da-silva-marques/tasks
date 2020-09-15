package com.example.tasks.service.repository.http

import com.example.tasks.service.repository.model.TaskModel
import retrofit2.Call
import retrofit2.http.*

interface TaskService {

    /*Lista todas as tarefas*/
    @GET("Task")
    fun findAll(): Call<List<TaskModel>>

    /*Lista todas as tarefas dentro de período de sete dias.*/
    @GET("Task/Next7Days")
    fun findAllNextSevenDays(): Call<List<TaskModel>>

    /*Lista todas as tarefas expiradas.*/
    @GET("Task/Overdue")
    fun findAllExpireds(): Call<List<TaskModel>>

    /*Retorna uma única tarefa*/
    @GET("Task/{id}")
    fun findById(@Path(value = "id", encoded = true) id: Int): Call<TaskModel>

    @POST("Task")
    @FormUrlEncoded
    fun create(
        @Field("PriorityId") priorityId: Int,
        @Field("Description") description: String,
        @Field("DueDate") dueDate: String,
        @Field("Complete") complete: Boolean
    ): Call<Boolean>

    @HTTP(method = "PUT", path = "Task", hasBody = true)
    @FormUrlEncoded
    fun update(
        @Field("Id") id: Int,
        @Field("PriorityId") priorityId: Int,
        @Field("Description") description: String,
        @Field("DueDate") dueDate: String,
        @Field("Complete") complete: Boolean
    ): Call<Boolean>

    @HTTP(method = "PUT", path = "Task/Complete", hasBody = true)
    @FormUrlEncoded
    fun complete(@Field("Id") id: Int): Call<Boolean>

    @HTTP(method = "PUT", path = "Task/Undo", hasBody = true)
    @FormUrlEncoded
    fun undo(@Field("Id") id: Int): Call<Boolean>

    @HTTP(method = "DELETE", path = "Task", hasBody = true)
    @FormUrlEncoded
    fun delete(@Field("Id") id: Int): Call<Boolean>
}