package com.vlad.taskmanager

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_add_task.*
import kotlinx.android.synthetic.main.activity_task_detail.*
import kotlinx.android.synthetic.main.activity_task_detail.textView_Date
import kotlinx.android.synthetic.main.layout_list_item.*
import kotlinx.android.synthetic.main.layout_list_item.task_name

lateinit var oldName: String

class TaskDetail : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_detail)

        val qtask = intent.getSerializableExtra("name") as Task
        oldName = qtask.name

        task_name.text = qtask.name
        textView_category.text = qtask.category
        textView_description.text = qtask.description
        textView_Date.text = qtask.date
        textView_CTime.text = qtask.time

        icon_name.setOnClickListener {
            Toast.makeText(this, "Название задачи.",Toast.LENGTH_SHORT).show()
        }

        icon_cat.setOnClickListener {
            Toast.makeText(this, "Категория задачи.",Toast.LENGTH_SHORT).show()
        }
        ic_info.setOnClickListener {
            Toast.makeText(this, "Название задачи.",Toast.LENGTH_SHORT).show()
        }

        exit_Button.setOnClickListener {
            finish()
        }

        edit_button.setOnClickListener {
            val myIntent = Intent(this, AddTaskActivity::class.java)
            myIntent.putExtra("currentTask", qtask)
            startActivityForResult(myIntent, 13)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data != null) {
            if (resultCode == Activity.RESULT_OK) {
                val returnedTask: Task = data.getSerializableExtra("return_task") as Task
                task_name.text = returnedTask.name
                textView_category.text = returnedTask.category
                textView_description.text = returnedTask.description
                textView_Date.text = returnedTask.date
                textView_CTime.text = returnedTask.time
                //TODO: Изменение текущей задачи.
                val returnIntent: Intent = Intent()
                returnIntent.putExtra("return_task", returnedTask)
                returnIntent.putExtra("oldName", oldName)

                setResult(Activity.RESULT_OK, returnIntent)

            }

        }

    }
}
