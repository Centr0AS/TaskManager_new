package com.vlad.taskmanager

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_add_task.*
import kotlinx.android.synthetic.main.activity_task_detail.*
import kotlinx.android.synthetic.main.activity_task_detail.textView_Date
import kotlinx.android.synthetic.main.layout_list_item.*
import kotlinx.android.synthetic.main.layout_list_item.task_name

class TaskDetail : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_detail)

        val qtask = intent.getSerializableExtra("name") as Task

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
        //TODO: Красиво оформить активити для подробной информации.
    }
}
