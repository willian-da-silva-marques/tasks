package com.example.tasks.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tasks.R
import com.example.tasks.service.listener.TaskListener
import com.example.tasks.service.repository.model.TaskModel
import com.example.tasks.view.viewholder.TaskViewHolder

class TaskAdapter : RecyclerView.Adapter<TaskViewHolder>() {

    private var mTasks: List<TaskModel> = arrayListOf()
    private lateinit var mListener: TaskListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.row_task_list, parent, false)
        return TaskViewHolder(itemView, mListener)
    }

    override fun getItemCount(): Int = this.mTasks.size

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.bindData(this.mTasks[position])
    }

    fun attachListener(listener: TaskListener) {
        this.mListener = listener
    }

    fun updateListener(tasks: List<TaskModel>) {
        this.mTasks = tasks
        notifyDataSetChanged()
    }
}