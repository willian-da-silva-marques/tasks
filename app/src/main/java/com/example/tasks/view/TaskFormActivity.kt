package com.example.tasks.view

import android.app.DatePickerDialog
import android.os.Bundle
import android.renderscript.RenderScript
import android.view.View
import android.widget.ArrayAdapter
import android.widget.DatePicker
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.tasks.R
import com.example.tasks.service.constants.TaskConstants
import com.example.tasks.service.repository.model.TaskModel
import com.example.tasks.viewmodel.TaskFormViewModel
import kotlinx.android.synthetic.main.activity_register.button_save
import kotlinx.android.synthetic.main.activity_task_form.*
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.Calendar

class TaskFormActivity : AppCompatActivity(), View.OnClickListener, DatePickerDialog.OnDateSetListener {

    private lateinit var mViewModel: TaskFormViewModel
    private val mLocale = Locale("pt", "BR")
    private val mSimpleDateFormat = SimpleDateFormat("dd/MM/yyyy", mLocale)

    private var mTaskId = 0

    private val mPrioritiesIds: MutableList<Int> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_form)

        this.mViewModel = ViewModelProvider(this).get(TaskFormViewModel::class.java)

        // Inicializa eventos
        this.listeners()
        this.observe()

        this.mViewModel.fillPriorities()

        this.loadDataFromActivity()
    }

    override fun onClick(v: View) {
        when(v.id) {
            R.id.button_save -> {
                this.handleSave()
            }
            R.id.button_date -> {
                this.showDatePicker()
            }
        }
    }

    override fun onDateSet(datePicker: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        val calendar = Calendar.getInstance(this.mLocale)
        calendar.set(year, month, dayOfMonth)
        val date = this.mSimpleDateFormat.format(calendar.time)
        button_date.text = date
    }

    private fun loadDataFromActivity() {
        val bundle = intent.extras
        if (bundle != null) {
            this.mTaskId = bundle.getInt(TaskConstants.BUNDLE.TASKID)
            this.mViewModel.findById(this.mTaskId)
            button_save.text = getText(R.string.update_task)
        }
    }

    private fun handleSave() {
        val task = TaskModel().apply {
            this.id = mTaskId
            this.description = edit_description.text.toString()
            this.complete = check_complete.isChecked
            this.dueDate = button_date.text.toString()
            this.priorityId = mPrioritiesIds[spinner_priority.selectedItemPosition]
        }
        this.mViewModel.save(task)
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance(this.mLocale)
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)
        DatePickerDialog(this,this, year, month, dayOfMonth).show()
    }

    private fun observe() {
        this.mViewModel.priorities.observe(this, Observer {
            val priorities: MutableList<String> = arrayListOf()
            for (item in it) {
                this.mPrioritiesIds.add(item.id)
                priorities.add(item.description)
            }

            val adapter = ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, priorities)
            spinner_priority.adapter = adapter
        })

        this.mViewModel.save.observe(this, Observer {
            if (it.isSuccessful()) {
                if (this.mTaskId == 0) {
                    this.showToast(getString(R.string.task_created))
                } else {
                    this.showToast(getString(R.string.task_updated))
                }
                finish()
            } else {
                this.showToast(it.getMessage())
            }
        })

        this.mViewModel.finded.observe(this, Observer {
            if (it != null) {
                edit_description.setText(it.description)
                check_complete.isChecked = it.complete
                spinner_priority.setSelection(this.getIndex(it.priorityId))

                val date = SimpleDateFormat("yyyy-MM-dd", Locale.US).parse(it.dueDate)
                button_date.text = this.mSimpleDateFormat.format(date)
            }
        })
    }

    private fun getIndex(priorityId: Int): Int {
        var index = 0
        for (id in this.mPrioritiesIds) {
            if (id == priorityId) {
                index = id
                break
            }
        }
        return index
    }

    private fun listeners() {
        button_save.setOnClickListener(this)
        button_date.setOnClickListener(this)
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
}
