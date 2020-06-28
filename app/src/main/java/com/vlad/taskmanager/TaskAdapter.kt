package com.vlad.taskmanager

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.layout_list_item.view.*


class TaskAdapter(private var tasks : ArrayList <Task>, private val onTaskClickListener: OnTaskClickListener) : RecyclerView.Adapter<TaskViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        return TaskViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.layout_list_item, parent, false)
        )
    }

    private var removedPosition: Int = 0
    private lateinit var removedItem: Task


    override fun getItemCount(): Int {
        return tasks.size
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = tasks[position]

        holder.itemView.task_name.text = task.name
        holder.itemView.task_category.text = "#" + task.category
        holder.itemView.task_description.text = task.description
        holder.itemView.task_date.text = task.date
        holder.itemView.task_time.text= task.time

        holder.itemView.setOnClickListener {
            onTaskClickListener.onTaskItemClicked(position)
        }
    }

    fun removeItem(viewHolder: RecyclerView.ViewHolder){
        removedPosition = viewHolder.adapterPosition
        removedItem = tasks[viewHolder.adapterPosition]

        tasks.removeAt(viewHolder.adapterPosition)
        notifyItemRemoved(viewHolder.adapterPosition)

        Snackbar.make(viewHolder.itemView, "Задача ${removedItem.name} удалена", Snackbar.LENGTH_LONG).setAction("ОТМЕНИТЬ"){
            tasks.add(removedPosition, removedItem)
            notifyItemInserted(removedPosition)
        }.show()
    }

    fun removeItemC(viewHolder: RecyclerView.ViewHolder, position: Int, tasks: ArrayList<Task>, sortedList:ArrayList<Task>): ArrayList<Task> {
        removedPosition = position
        removedItem = tasks[position]
       val removedPositionS = viewHolder.adapterPosition

        tasks.removeAt(position)
        sortedList.removeAt(viewHolder.adapterPosition)
        notifyItemRemoved(viewHolder.adapterPosition)

        Snackbar.make(viewHolder.itemView, "Задача ${removedItem.name} удалена", Snackbar.LENGTH_LONG).setAction("ОТМЕНИТЬ"){
            tasks.add(removedPosition, removedItem)
            sortedList.add(removedPositionS, removedItem)
            notifyItemInserted(removedPositionS)

            //TODO: Тут должен быть сейв.

        }.show()

        return tasks
    }

}