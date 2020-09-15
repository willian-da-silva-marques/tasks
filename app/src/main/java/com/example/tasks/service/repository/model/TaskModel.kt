package com.example.tasks.service.repository.model

import com.google.gson.annotations.SerializedName

class TaskModel {

    @SerializedName("id")
    var id: Int = 0

    @SerializedName("priorityId")
    var priorityId: Int = 0

    @SerializedName("description")
    var description: String = ""

    @SerializedName("dueDate")
    var dueDate: String = ""

    @SerializedName("complete")
    var complete: Boolean = false
}