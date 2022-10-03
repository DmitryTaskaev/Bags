package com.bignerdranch.android.lab11json

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.bignerdranch.android.lab11json.data.DATABASE_NAME
import com.bignerdranch.android.lab11json.data.TasksBD
import com.bignerdranch.android.lab11json.data.TasksDAO
import com.bignerdranch.android.lab11json.data.models.Priority
import com.bignerdranch.android.lab11json.data.models.Tasks
import java.util.concurrent.Executors

class TaskInfo : AppCompatActivity() {

    private lateinit var addTask : ImageButton
    private lateinit var listTask: MutableList<Tasks>
    private var index : Int = -1

    private var kol_preority : Int = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_info)
        listTask = mutableListOf()

        var db: TasksBD = Room.databaseBuilder(this, TasksBD::class.java, DATABASE_NAME).build()
        val TaskDAO = db.TasksDAO()
        val exec = Executors.newSingleThreadExecutor()
        exec.execute{
            if(TaskDAO.getCountPreority() == 0){
                TaskDAO.addPreoruty(Priority(0,"Работа"))
            }
        }

        //Переход в окно добавления
        addTask = findViewById(R.id.addTaskBtn)
        addTask.setOnClickListener {
            val reDir = Intent(this, MainActivity::class.java)
            startActivity(reDir)
        }
    }
    override fun onBackPressed() {
        AlertDialog.Builder(this).apply {
            setTitle("Подтверждение")
            setMessage("Вы уверены, что хотите выйти из программы?")
            setPositiveButton("Да"){ _, _ ->
                super.onBackPressed()
            }
            setNegativeButton("Нет"){ _, _ ->

            }
            setCancelable(true)
        }.create().show()
    }
    override fun onResume() {
        super.onResume()
        DBtoList()
        updateInformation()
    }
    fun DBtoList(){
        listTask.clear()
        var db: TasksBD = Room.databaseBuilder(this, TasksBD::class.java, DATABASE_NAME).build()
        val TaskDAO = db.TasksDAO()
        val Taskss = TaskDAO.getAllTasks()
        Taskss.observe(this, androidx.lifecycle.Observer {
            it.forEach {
                listTask.add(Tasks(it.uid,it.preority_id,it.nameTask,it.creatTask,it.text,it.dateTask))
                updateInformation()
            }
        })
    }
    fun updateInformation(){
        Log.d("DBSSADD","updInfo выполнен")
        val rv = findViewById<RecyclerView>(R.id.TaskLisrRv)
        val adapter = TaskRVAdapter(this, listTask)
        val rvListener = object : TaskRVAdapter.ItemClickListener{
            override fun onItemClick(view: View?, position: Int) {
                val intent = Intent(this@TaskInfo, MainActivity::class.java)
                intent.putExtra("index", position)
                intent.putExtra("uid", listTask[position].uid.toString())
                Log.d("UUID-TXT", listTask[position]?.uid.toString())
                index = position
                startActivity(intent)
            }
        }
        adapter.setClickListener(rvListener)
        rv.layoutManager = LinearLayoutManager(this)
        rv.adapter = adapter
    }

}