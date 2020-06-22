package com.vlad.taskmanager

import java.io.Serializable
import java.util.*

data class Task(val name:String, val category:String, val description :String, val date: String, val time: String ) : Serializable