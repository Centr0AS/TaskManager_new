package com.vlad.taskmanager

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_add_task.*
import java.text.SimpleDateFormat
import java.util.*


class AddTaskActivity : AppCompatActivity() {

    var formate = SimpleDateFormat("dd MMM, YYYY", Locale.getDefault())
    val timeFormat = SimpleDateFormat("hh:mm", Locale.getDefault())


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_task)

        val now = Calendar.getInstance()
        val selectedTime = Calendar.getInstance()
        val selectedDate = Calendar.getInstance()
        var date = formate.format(selectedDate.time)

        textView_Date.text = "Дата: " + formate.format(now.time)

        var isEdit = intent.getStringExtra("isEdit")
        if (isEdit.toInt() == 1) {
            var qTask = intent.getSerializableExtra("currentTask") as Task
            editTextName.setText(qTask.name)
            editTextCategory.setText(qTask.category)
            editTextDescription.setText(qTask.description)
            //TODO: Доделать чтобы дата и тайм тоже схватывался в поле.
        }


        select_date_btn.setOnClickListener {

            val datePicker = DatePickerDialog(
                this, DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                    selectedDate.set(Calendar.YEAR, year)
                    selectedDate.set(Calendar.MONTH, month)
                    selectedDate.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                    date = formate.format(selectedDate.time)

                    Toast.makeText(this, "Date:$date", Toast.LENGTH_SHORT).show()
                    textView_Date.text = "Дата: $date"

                },
                now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH)
            )
            datePicker.show()
        }

        select_time_btn.setOnClickListener {
            try {
                if (textView_Time.text != "Время не задано") {
                    val selectedTime = timeFormat.parse(textView_Time.text.toString())
                    now.time = selectedTime
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

            val timePicker = TimePickerDialog(
                this, TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                    selectedTime.set(Calendar.HOUR_OF_DAY, hourOfDay)
                    selectedTime.set(Calendar.MINUTE, minute)
                    Toast.makeText(
                        this,
                        "time:" + timeFormat.format(selectedTime.time),
                        Toast.LENGTH_SHORT
                    ).show()
                    textView_Time.text = timeFormat.format(selectedTime.time)
                },
                now.get(Calendar.HOUR_OF_DAY), now.get(Calendar.MINUTE), true
            )
            timePicker.show()
        }

        incDate_btn.setOnClickListener {
            selectedDate.add(Calendar.DAY_OF_YEAR, +1)
            date = formate.format(selectedDate.time)
            textView_Date.text = "Дата: $date"
        }

        decDate_btn.setOnClickListener {
            selectedDate.add(Calendar.DAY_OF_YEAR, -1)
            date = formate.format(selectedDate.time)
            textView_Date.text = "Дата: $date"
        }

        add_task_btn.setOnClickListener {
            if (editTextName.text.isBlank()) {
                Toast.makeText(
                    this,
                    "Поле название является обязательным для заполнения!",
                    Toast.LENGTH_SHORT
                ).show()
            } else if (editTextCategory.text.isBlank()) {
                Toast.makeText(
                    this,
                    "Поле категория является обязательным для заполнения!",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                var taskName = editTextName.text.toString()
                var category = editTextCategory.text.toString()
                var description = editTextDescription.text.toString()

                taskName = taskName.trim()
                category = category.trim()
                description = description.trim()

                var setTime = timeFormat.format(selectedTime.time).toString()
                if (textView_Time.text == "Время не задано") {
                    setTime = ""
                }

                val setDate = date.toString()

                val newTask: Task = Task(taskName, category, description, setDate, setTime)
                Toast.makeText(
                    this,
                    newTask.name + newTask.category + newTask.description,
                    Toast.LENGTH_SHORT
                ).show()
                val returnIntent: Intent = Intent()
                returnIntent.putExtra("return_task", newTask)
                Toast.makeText(this, "Задача $taskName добавлена", Toast.LENGTH_SHORT).show()

                val i = Intent(applicationContext, Receiver::class.java)

                i.putExtra("name", newTask.name)

                val pi = PendingIntent.getBroadcast(applicationContext, 111, i, 0)
                val am: AlarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
                val df = SimpleDateFormat("yyyy.MM.dd")
                val howMany: Long =
                    (selectedDate.timeInMillis - System.currentTimeMillis()) + selectedTime.timeInMillis

                am.set(AlarmManager.RTC_WAKEUP, howMany, pi)

                Toast.makeText(applicationContext, "Alarm is set for $howMany", Toast.LENGTH_LONG)
                    .show()

                setResult(Activity.RESULT_OK, returnIntent)
                finish()
            }

        }
    }
}
