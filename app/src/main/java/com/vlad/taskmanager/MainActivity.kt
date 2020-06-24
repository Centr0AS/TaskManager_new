package com.vlad.taskmanager

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.*
import java.nio.channels.FileChannel
import java.nio.charset.Charset
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import androidx.appcompat.widget.Toolbar
import androidx.core.view.get

class MainActivity : AppCompatActivity(), OnTaskClickListener {

    private val REQUEST_READ_EXTERNAL = 1
    var tasks = ArrayList<Task>()
    lateinit var taskAdapter: RecyclerView.Adapter<*>
    private lateinit var deleteIcon: Drawable
//    private lateinit var doneIcon: Drawable
    var categories = ArrayList<String>()
    var sortedList = ArrayList<Task>()

    private var swipeBackground: ColorDrawable = ColorDrawable(Color.parseColor("#FF0000"))
//    private var swipeBackground1: ColorDrawable = ColorDrawable(Color.parseColor("#FF00FF0D"))

    private lateinit var categorySelector: Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        val path = this.getExternalFilesDir(null)
        val letDirectory = File(path, "Tasks")
        val file = File(letDirectory, "nots.json")
        if (!file.exists()) {
            createJsonData()
        }
        else {

            if (ActivityCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE
                )
                != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this, arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                    REQUEST_READ_EXTERNAL
                )
            } else {
                parseJson()
            }
        }

        taskAdapter = TaskAdapter(tasks, this )
        rv_task_list.layoutManager = LinearLayoutManager(this)
        rv_task_list.adapter = taskAdapter

        deleteIcon = ContextCompat.getDrawable(this, R.drawable.ic_delete)!!
//        doneIcon = ContextCompat.getDrawable(this, R.drawable.ic_done_black_24dp)!!

        taskAdapter.notifyDataSetChanged()

        categorySelector = findViewById<Spinner>(R.id.sp_option)
         categories = makeCategoryList()
        categorySelector.adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, categories)

        categorySelector.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(
                parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
//                Toast.makeText(this@MainActivity, "On Spinner Clicked$id",Toast.LENGTH_SHORT).show()

                val currentDate = Calendar.getInstance()
                var formate = SimpleDateFormat("dd MMM, YYYY", Locale.getDefault())
                var tempDate = formate.format(currentDate.time)

                when (position){
                    0 -> {
                        taskAdapter = TaskAdapter(tasks, this@MainActivity)
                        rv_task_list.adapter = taskAdapter
                        taskAdapter.notifyDataSetChanged()
                        }
                    1-> {
                        val sortedList = makeSortedListbyCategory(tempDate.toString(),"",2)
                        taskAdapter = TaskAdapter(sortedList, this@MainActivity)
                        rv_task_list.adapter = taskAdapter
                        taskAdapter.notifyDataSetChanged()
                    }
                    2-> {
                        var tomorrowDay = currentDate.get(Calendar.DAY_OF_MONTH)
                        val month = currentDate.get(Calendar.MONTH)

                        if (tomorrowDay + 1 > DateFuncs().getDaysinMonth(month))
                            tomorrowDay = 1
                        else
                            tomorrowDay += 1

                        val sortedList = makeSortedListbyCategory(tomorrowDay.toString(),"",5)
                        taskAdapter = TaskAdapter(sortedList, this@MainActivity)
                        rv_task_list.adapter = taskAdapter
                        taskAdapter.notifyDataSetChanged()
                    }
                    3-> {
                        //TODO: Проверить фильтрацию на текущей неделе (fixed?).
                        val month = currentDate.get(Calendar.MONTH)
                        val daysinMounth = DateFuncs().getDaysinMonth(month)

                        var weekDay = currentDate.get(Calendar.DAY_OF_WEEK)
                        var currentDay = currentDate.get(Calendar.DAY_OF_MONTH)
                        var minDate = DateFuncs().getFirstDayofWeek(weekDay, currentDay, daysinMounth)

                        formate = SimpleDateFormat("MMM", Locale.getDefault())
                        tempDate = formate.format(currentDate.time)

                        val sortedList = makeSortedListbyCategory(minDate.toString(),tempDate,3)
                        taskAdapter = TaskAdapter(sortedList, this@MainActivity)
                        rv_task_list.adapter = taskAdapter
                        taskAdapter.notifyDataSetChanged()

                    }
                    4->{
                        formate = SimpleDateFormat("MMM", Locale.getDefault())
                        tempDate = formate.format(currentDate.time)
                        val sortedList = makeSortedListbyCategory(tempDate.toString(),"",4)
                        taskAdapter = TaskAdapter(sortedList, this@MainActivity)
                        rv_task_list.adapter = taskAdapter
                        taskAdapter.notifyDataSetChanged()

                    }
                    else -> {
                        val sortedList = makeSortedListbyCategory(categories[position],"",1)
                    taskAdapter = TaskAdapter(sortedList, this@MainActivity)
                    rv_task_list.adapter = taskAdapter
                    taskAdapter.notifyDataSetChanged()
                    }
                }
            }
        }

        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, position: Int) {
                var selOption = categorySelector.selectedItemPosition
                if (selOption != 0)
                {
//                    var selectedTask = tasks.find { name -> sortedList[position].name.equals(tasks) }
                    var poss = viewHolder.adapterPosition

                    var selTask = sortedList[poss]
                   var  selectedTask1 = tasks.indexOf(selTask)
//                    taskAdapter = TaskAdapter(tasks, this@MainActivity)
//                    rv_task_list.adapter = taskAdapter
                    tasks = (taskAdapter as TaskAdapter).removeItemC(viewHolder, selectedTask1, tasks,sortedList)
                }
                else
                {
                    (taskAdapter as TaskAdapter).removeItem(viewHolder)

                }
                //TODO: Пофиксить удаление из категорий (fixed но не записывается востановленная задача).
                createJsonData()
            }

            override fun onChildDraw(
                c: Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
            ) {

                val itemView = viewHolder.itemView

                val iconMargin = (itemView.height - deleteIcon.intrinsicHeight) / 2

                if (dX > 0){
                    swipeBackground.setBounds(itemView.left, itemView.top, dX.toInt(), itemView.bottom)
                    deleteIcon.setBounds(itemView.left + iconMargin, itemView.top + iconMargin, itemView.left + iconMargin + deleteIcon.intrinsicWidth,
                        itemView.bottom - iconMargin )
                } else {
                    //TODO: На красной стороне иногда вылезает графический баг с красным цветом (пока не актуально).
//                    swipeBackground1.setBounds(itemView.right + dX.toInt(), itemView.top, itemView.right, itemView.bottom)
//                    doneIcon.setBounds(itemView.right - iconMargin -doneIcon.intrinsicWidth, itemView.top + iconMargin, itemView.right - iconMargin,
//                        itemView.bottom - iconMargin )
                    swipeBackground.setBounds(itemView.right + dX.toInt(), itemView.top, itemView.right, itemView.bottom)
                    deleteIcon.setBounds(itemView.right - iconMargin -deleteIcon.intrinsicWidth, itemView.top + iconMargin, itemView.right - iconMargin,
                        itemView.bottom - iconMargin )
//                    swipeBackground1.draw(c)
                }

                swipeBackground.draw(c)

                c.save()

                if (dX > 0)
                    c.clipRect(itemView.left, itemView.top, dX.toInt(), itemView.bottom)
                else
                    c.clipRect(itemView.right + dX.toInt(), itemView.top, itemView.right, itemView.bottom)

                deleteIcon.draw(c)
//                doneIcon.draw(c)

                c.restore()

                super.onChildDraw(
                    c,
                    recyclerView,
                    viewHolder,
                    dX,
                    dY,
                    actionState,
                    isCurrentlyActive
                )
            }
        }

        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(rv_task_list)

        addTask_button.setOnClickListener {
            val myIntent = Intent(this, AddTaskActivity::class.java)
            startActivityForResult(myIntent, 12)
        }
    }

    //Serialization Start
    private fun createJsonData()
    {
        var json = JSONObject()
//        var writeTask = tasks
        json.put("task", writeTasks(tasks))
        saveJson(json.toString() )
    }

    private fun saveJson(jsonString: String){

        val output:Writer
        val path = this.getExternalFilesDir(null)
        val letDirectory = File(path, "Tasks")
        letDirectory.mkdirs()

        val file = File(letDirectory, "nots.json")
        if (!file.exists()) {
            file.createNewFile()
        }
        output=BufferedWriter(FileWriter(file))
        output.write(jsonString)
        output.close()
    }

    private fun createFile(): File {

        val fileName = "nots"

        val storageDir = getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)
        //Here was null?
        if (!storageDir?.exists()!!){
            storageDir.mkdir()
        }

        return File.createTempFile(
            fileName,
            ".json",
            storageDir
        )
    }
    private fun writeTasks (tasks: ArrayList<Task>): JSONArray {

        var taskJson = JSONArray()

        tasks.forEach{
            taskJson.put(
                JSONArray()
                    .put(it.name)
                    .put(it.category)
                    .put(it.description)
                    .put(it.date)
                    .put(it.time)
            )
        }
        return taskJson
    }

    //Serialization end.


    //Deserialization start (parse).
    private fun parseJson() {

        val jsonObject = JSONObject(readFile())
        var savedTasks = getTasks(jsonObject.getJSONArray("task"))

    }

    private fun getTasks(jsonArray: JSONArray): ArrayList<Task> {

        var x = 0
        while (x < jsonArray.length() ){
            tasks.add(
                Task(
                jsonArray.getJSONArray(x).getString(0),
                jsonArray.getJSONArray(x).getString(1),
                jsonArray.getJSONArray(x).getString(2),
                jsonArray.getJSONArray(x).getString(3),
                jsonArray.getJSONArray(x).getString(4))
            )
            x++
        }
            return tasks
    }

    private fun readFile(): String {

        val path = this.getExternalFilesDir(null)
        val letDirectory = File(path, "Tasks")

        val file = letDirectory
            .absolutePath + "/nots.json"

        val stream = FileInputStream(file)

        var jsonString = ""
        stream.use { stream ->
            val fileChannel = stream.channel
            val mappedByteBuffer = fileChannel.map(
                    FileChannel.MapMode.READ_ONLY,
                    0,
                    fileChannel.size()
            )
            jsonString = Charset.defaultCharset().decode(mappedByteBuffer).toString()
        }
            return jsonString
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == REQUEST_READ_EXTERNAL) parseJson()
    }

    //Deserialization end (parse).

    override fun onTaskItemClicked(position: Int) {

        val intent = Intent(this, TaskDetail::class.java)
        //TODO: Не работает показ дополнительных данных из категорий (fixed).

        var selOption = categorySelector.selectedItemPosition
        if (selOption != 0)
        {
            var selTask = sortedList[position]
            var  selectedTask1 = tasks.indexOf(selTask)

            intent.putExtra("name", selTask)
            startActivity(intent)
        }
        else
        {
            intent.putExtra("name", tasks[position])
            startActivity(intent)

        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (data != null) {
            if (resultCode == Activity.RESULT_OK) {
                val returnedTask: Task = data.getSerializableExtra("return_task") as Task
                tasks.add(returnedTask)
                categories = makeCategoryList()
                categorySelector.adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, categories)
                taskAdapter = TaskAdapter(tasks, this )
                taskAdapter.notifyDataSetChanged()
                createJsonData()
            } else {

            }
        }
    }

    private fun makeCategoryList(): ArrayList<String> {
        var categories = ArrayList<String>()
        categories.add("Все")
        categories.add("Сегодня")
        categories.add("Завтра")
        categories.add("Неделя")
        categories.add("Месяц")
        var i = 0
        Toast.makeText(this, tasks.size.toString(), Toast.LENGTH_SHORT).show()
        while (i <= tasks.size -1)
        {
            var checkedValue = tasks[i].category
            if (categories.contains(checkedValue))
            {
//                continue // Взаместо i++?
                i++
            }
            else {
                categories.add(checkedValue)
                i++
            }
        }
        return categories
    }

    private fun makeSortedListbyCategory(value: String, currentMonth: String, action: Int ): ArrayList<Task> {
        //action = 1 - категория
        //action = 2 - задачи на сегодня
        //action = 3 - задачи на эту неделю
        //action = 4 - задачи на этот месяц
        //action = 5 - задачи на завтра
       sortedList = ArrayList<Task>()
        var i = 0
        when (action){
            1-> {
                while (i < tasks.size)
                {
                    if (tasks[i].category == value)
                    {
                        sortedList.add(tasks[i])
                        i++
                    }
                    else{i++}
                }
            }
            2-> {
                while (i < tasks.size) {
                    if (tasks[i].date == value) {
                        sortedList.add(tasks[i])
                        i++
                    } else {
                        i++
                    }
                }
            }
            3-> {
                val correctValue = value.toInt()
                var currentWeek = ArrayList<String>()
                for (i in 0..6)
                    currentWeek.add((correctValue + i).toString())

                var i = 0
                var j = 0
                var add = false
                while (i < tasks.size) {
                    var dateDay = tasks[i].date.toCharArray()
                    var numberOfDay = dateDay[0].toString() + dateDay[1].toString()
                    while (j <= 6) {
                        if (numberOfDay == (currentWeek[j]) &&
                            tasks[i].date.contains(currentMonth)) {
                            add = true
//                            j++
                            break // Возможно из за него может быть баг.
                        } else {
                            j++
                        }
                    }
                    if (add) {
                        sortedList.add(tasks[i])
                        i++
                        j = 0
                        add = false
                    } else {
                        i++
                        j = 0
                    }
                }
            }
            4->{
                while (i < tasks.size) {
                    if (tasks[i].date.contains(value)) {
                        sortedList.add(tasks[i])
                        i++
                    } else {
                        i++
                    }
                }
            }
            5->{
                while (i < tasks.size) {
                    if (tasks[i].date.contains(value)) {
                        sortedList.add(tasks[i])
                        i++
                    } else {
                        i++
                    }
                }
            }
        }
        return sortedList
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
         super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        var selectedOption = ""

        when(item.itemId){

            R.id.settings -> selectedOption = "Settings"
            R.id.finances -> selectedOption = "Finances"
            R.id.about -> selectedOption = "About"
        }
        Toast.makeText(this, "Option: $selectedOption", Toast.LENGTH_SHORT).show()

        when (selectedOption){

            "Finances"-> {
                val financeIntent = Intent(this, FinancesActivity::class.java)
                startActivity(financeIntent)
            }
            "Settings" ->{
                createJsonData()

            }
            "About" -> {

            }
        }
        return super.onOptionsItemSelected(item)
    }

}
