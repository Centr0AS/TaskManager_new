package com.vlad.taskmanager
//
////TODO: Адаптировать методы класса для работы с MainActivity.
//
//import android.os.Environment
//import org.json.JSONArray
//import org.json.JSONObject
//import java.io.*
//import java.nio.channels.FileChannel
//import java.nio.charset.Charset
//
//class Serialization
////
//////Serialization Start
// fun createJsonData(tasks: ArrayList<Task>, path: File?)
//{
//    var json = JSONObject()
//
//    json.put("task", writeTasks(tasks))
//    saveJson(json.toString(),path )
//}
//
//private fun saveJson(jsonString: String, path: File?){
//
//    val output: Writer
////    val path = this.getExternalFilesDir(null)
//    val letDirectory = File(path, "Tasks")
//    letDirectory.mkdirs()
//
//    val file = File(letDirectory, "nots.json")
//    if (!file.exists()) {
//        file.createNewFile()
//    }
//    output= BufferedWriter(FileWriter(file))
//    output.write(jsonString)
//    output.close()
//}
////
////private fun createFile(storageDir: File): File {
////
////    val fileName = "nots"
////
////    val storageDir = getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)
////    //Here was null?
////    if (!storageDir?.exists()!!){
////        storageDir.mkdir()
////    }
////
////    return File.createTempFile(
////        fileName,
////        ".json",
////        storageDir
////    )
////}
//private fun writeTasks (tasks: ArrayList<Task>): JSONArray {
//
//    var taskJson = JSONArray()
//
//    tasks.forEach{
//        taskJson.put(
//            JSONArray()
//                .put(it.name)
//                .put(it.category)
//                .put(it.description)
//                .put(it.date)
//                .put(it.time)
//        )
//    }
//    return taskJson
//}
////
//////Serialization end.
////
////
//////Deserialization start (parse).
////private fun parseJson() {
////
////    val jsonObject = JSONObject(readFile())
////    var savedTasks = getTasks(jsonObject.getJSONArray("task"))
////
////}
////
////private fun getTasks(jsonArray: JSONArray, tasks: ArrayList<Task>): ArrayList<Task> {
////
////    var x = 0
////    while (x < jsonArray.length() ){
////        tasks.add(
////            Task(
////                jsonArray.getJSONArray(x).getString(0),
////                jsonArray.getJSONArray(x).getString(1),
////                jsonArray.getJSONArray(x).getString(2),
////                jsonArray.getJSONArray(x).getString(3),
////                jsonArray.getJSONArray(x).getString(4))
////        )
////        x++
////    }
////    return tasks
////}
////
////private fun readFile(): String {
////
////    val path = this.getExternalFilesDir(null)
////    val letDirectory = File(path, "Tasks")
////
////    val file = letDirectory
////        .absolutePath + "/nots.json"
////
////    val stream = FileInputStream(file)
////
////    var jsonString = ""
////    stream.use { stream ->
////        val fileChannel = stream.channel
////        val mappedByteBuffer = fileChannel.map(
////            FileChannel.MapMode.READ_ONLY,
////            0,
////            fileChannel.size()
////        )
////        jsonString = Charset.defaultCharset().decode(mappedByteBuffer).toString()
////    }
////    return jsonString
////}
