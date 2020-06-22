package com.vlad.taskmanager

class DateFuncs
{
    fun getDaysinMonth (month: Int): Int {
        when (month){
            1 -> return 29
            3, 5, 8, 0 -> return 30
            else -> return 31
        }
    }

    fun getFirstDayofWeek (weekDay :Int, currentDay :Int, daysinMonth :Int):Int
    {
        var correctWeekDay = weekDay
        when (weekDay){
            1-> correctWeekDay = 7
            2-> correctWeekDay = 1
            3-> correctWeekDay = 2
            4 -> correctWeekDay = 3
            5 -> correctWeekDay = 4
            6-> correctWeekDay = 5
            7-> correctWeekDay = 6

            else -> correctWeekDay = weekDay
        }

        var firstDayOfWeek = (currentDay - correctWeekDay + 1 )

        if (firstDayOfWeek < 1)
            firstDayOfWeek = 1
        else if (firstDayOfWeek > daysinMonth )
            firstDayOfWeek = daysinMonth - currentDay

        return firstDayOfWeek
    }
    
    
}