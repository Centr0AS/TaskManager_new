package com.vlad.taskmanager

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_finances.*
import java.util.*

class FinancesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_finances)

        var balance = 1000
        var currencyType = "p"
        var showBalance = balance.toString() + currencyType
        balance_textView.text = showBalance

        val currentDate = Calendar.getInstance()
        var month = currentDate.get(Calendar.MONTH)
        var dayToMonth = balance/DateFuncs().getDaysinMonth(month)

        textView_Month.text = "Тратя по $dayToMonth $currencyType в день, их хватит на месяц."

        exitButton.setOnClickListener {
            finish()
        }
        balance_textView.setOnClickListener {
            Toast.makeText(this, "При нажатии будет появляться окошко где можно будет ввести данные", Toast.LENGTH_SHORT).show()
            //TODO: Реализовать активити(интент) для ввода значения баланса.
        }

            //TODO: Красивое оформление активити.
    }



}
