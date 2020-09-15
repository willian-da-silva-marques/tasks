package com.example.tasks.service.repository.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.tasks.service.repository.model.PriorityModel

@Dao
interface PriorityDAO {

    @Insert
    fun saveAll(priorities: List<PriorityModel>)

    @Query("DELETE FROM priority")
    fun deleteAll()

    @Query("SELECT * FROM priority")
    fun findAll(): List<PriorityModel>

    @Query("SELECT description FROM priority WHERE id = :id")
    fun findById(id: Int): String
}