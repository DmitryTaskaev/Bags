package com.bignerdranch.android.lab11json

import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bignerdranch.android.lab11json.data.models.Tasks
import java.security.AccessControlContext

class TaskRVAdapter(context: Context?, val data: MutableList<Tasks>) : RecyclerView.Adapter<TaskRVAdapter.TaskViewHolder>() {

    private val layoutInFlater: LayoutInflater = LayoutInflater.from(context)
    private var iClickListener: ItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view: View = layoutInFlater.inflate(R.layout.tasklist, parent, false)
        return  TaskViewHolder(view)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val item = data[position]
        holder.nameTextView.text = item?.nameTask
        holder.textTextView.text = item?.text
        holder.cratTextView.text = item?.creatTask
        holder.dateTextView.text = item?.dateTask
        //if(item?.preority == true){
            //holder.Liner.setBackgroundColor(Color.RED)
            //holder.nameTextView.setTextColor(Color.RED)
        //}
    }

    override fun getItemCount(): Int = data.size

    fun setClickListener(itemClickListener: ItemClickListener?){
        iClickListener = itemClickListener
    }

    interface ItemClickListener{
        fun onItemClick(view: View?, position: Int)
    }

    inner class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        var nameTextView : TextView = itemView.findViewById(R.id.taskName)
        var textTextView : TextView = itemView.findViewById(R.id.taskText)
        var cratTextView : TextView = itemView.findViewById(R.id.taskCreate)
        var dateTextView : TextView = itemView.findViewById(R.id.taskDate)
        var Liner : LinearLayout = itemView.findViewById(R.id.LinerItem)

        init {
            itemView.setOnClickListener(this)
        }
        override fun onClick(view: View?){
            iClickListener?.onItemClick(view,adapterPosition)
        }
    }
}