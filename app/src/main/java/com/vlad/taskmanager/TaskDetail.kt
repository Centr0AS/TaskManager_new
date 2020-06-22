package com.vlad.taskmanager

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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

        exit_Button.setOnClickListener {
            finish()
        }
        //TODO: Красиво оформить активити для подробной информации.
    }
}
