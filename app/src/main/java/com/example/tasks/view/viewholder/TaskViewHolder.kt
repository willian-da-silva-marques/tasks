package com.example.tasks.view.viewholder

import android.app.AlertDialog
import android.graphics.Color
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tasks.R
import com.example.tasks.service.listener.TaskListener
import com.example.tasks.service.repository.PriorityRepository
import com.example.tasks.service.repository.model.TaskModel
import java.text.SimpleDateFormat
import java.util.*

class TaskViewHolder(itemView: View, val listener: TaskListener) :
    RecyclerView.ViewHolder(itemView) {

    private val mLocale = Locale("pt","BR")
    private val mSimpleDateFormat = SimpleDateFormat("dd/MM/yyyy", this.mLocale)

    private val mPriorityRepository = PriorityRepository(itemView.context)

    private var mTextDescription: TextView = itemView.findViewById(R.id.text_description)
    private var mTextPriority: TextView = itemView.findViewById(R.id.text_priority)
    private var mTextDueDate: TextView = itemView.findViewById(R.id.text_due_date)
    private var mImageTask: ImageView = itemView.findViewById(R.id.image_task)

    /**
     * Atribui valores aos elementos de interface e também eventos
     */
    fun bindData(task: TaskModel) {
        val date = SimpleDateFormat("yyyy-MM-dd", Locale.US).parse(task.dueDate)
        this.mTextDescription.text = task.description
        this.mTextPriority.text = this.mPriorityRepository.findById(task.priorityId)
        this.mTextDueDate.text = this.mSimpleDateFormat.format(date!!)

        if (task.complete) {
            this.mTextDescription.setTextColor(Color.GREEN)
            this.mImageTask.setImageResource(R.drawable.ic_done)
        } else {
            this.mTextDescription.setTextColor(Color.MAGENTA)
            this.mImageTask.setImageResource(R.drawable.ic_todo)
        }
        // Eventos
         mTextDescription.setOnClickListener { listener.onListClick(task.id) }
         mImageTask.setOnClickListener {
             if (task.complete) {
                 listener.onUndoClick(task.id)
             } else {
                 listener.onCompleteClick(task.id)
             }
         }

        mTextDescription.setOnLongClickListener {
            AlertDialog.Builder(itemView.context)
                .setTitle(R.string.remocao_de_tarefa)
                .setMessage(R.string.remover_tarefa)
                .setPositiveButton(R.string.sim) { _, _ ->
                     listener.onDeleteClick(task.id)
                }
                .setNeutralButton(R.string.cancelar, null)
                .show()
            true
        }

    }

}