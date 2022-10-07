package com.bignerdranch.android.lab11json

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import androidx.core.view.isVisible
import androidx.room.Room
import com.bignerdranch.android.lab11json.data.DATABASE_NAME
import com.bignerdranch.android.lab11json.data.TasksBD
import com.bignerdranch.android.lab11json.data.models.Priority
import com.bignerdranch.android.lab11json.data.models.Tasks
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.concurrent.Executors
import java.util.zip.Inflater

class MainActivity : AppCompatActivity() {
    private lateinit var nameTask: EditText
    private lateinit var namesTask: EditText
    private lateinit var textTask: EditText
    private lateinit var preoritytTask: Spinner
    private lateinit var dateTask: CalendarView
    private lateinit var btnTask: Button
    private lateinit var delTask: Button
    private var calendarDate: String? = null
    private lateinit var addPreor: Button
    private lateinit var btnTaskInfo: ImageButton
    private lateinit var tvHead: TextView
    private lateinit var listPreor: MutableList<Tasks>
    private lateinit var spin:Spinner

    private var date : String? = null
    private var uid: UUID? = null



    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        listPreor = mutableListOf()
        nameTask = findViewById(R.id.nameTask)
        namesTask = findViewById(R.id.namesTask)
        textTask = findViewById(R.id.textTask)
        preoritytTask = findViewById(R.id.spinnerPreor)
        btnTask = findViewById(R.id.buttonTask)
        delTask = findViewById(R.id.delTasks)
        dateTask = findViewById(R.id.calendarView)
        btnTaskInfo = findViewById(R.id.backTaskInfo)
        tvHead = findViewById(R.id.textView3)
        addPreor = findViewById(R.id.addPreority)
        spin = findViewById(R.id.spinnerPreor)




        var index = intent.getIntExtra("index",-1)
        var txtuid = intent.getStringExtra("uid")

        if(txtuid != null){
            uid = UUID.fromString(txtuid)
        }
        val currentDate = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
        val formatted = currentDate.format(formatter)
        calendarDate = formatted
        dateTask.setOnDateChangeListener(){_, year, month, dayOfMonth -> calendarDate = "$dayOfMonth.${month+1}.$year"}
        val cl = Calendar.getInstance()
        UpdateSpinner()

        if(index > -1){
            delTask.isVisible = true
            tvHead.text = "Редактирование"
            btnTask.text = "Изменить"
            var db: TasksBD = Room.databaseBuilder(this, TasksBD::class.java, DATABASE_NAME).build()
            val TaskDAO = db.TasksDAO()
            val Tasks = TaskDAO.getTasks(uid!!)
            Tasks.observe(this, androidx.lifecycle.Observer {
                it.forEach {
                    nameTask.setText(it.nameTask)
                    namesTask.setText(it.creatTask)
                    textTask.setText(it.text)
                    val date = it.dateTask.split(".")
                    cl.set(date?.get(2)!!.toInt(),date[1].toInt()-1,date[0].toInt())
                    dateTask.date = cl.timeInMillis
                    spin.setSelection(it.preority_id)
                }
            })
        }
        else {
            delTask.isVisible = false
        }

        //Отправка
        btnTask.setOnClickListener {
            if(index == -1){
                var db: TasksBD = Room.databaseBuilder(this, TasksBD::class.java, DATABASE_NAME).build()
                val TaskDAO = db.TasksDAO()
                val exec = Executors.newSingleThreadExecutor()
                var ui = UUID.randomUUID()
                //Добавление в базу
                //getTypeId(viewBinding.spinnerType.selectedItem.toString())dDA
                exec.execute{
                    TaskDAO.addTasks(Tasks(ui!!,preoritytTask.selectedItemPosition+1,nameTask.text.toString(),namesTask.text.toString(),textTask.text.toString(),calendarDate.toString()))
                }
            }
            else
            {
                var db: TasksBD = Room.databaseBuilder(this, TasksBD::class.java, DATABASE_NAME).build()
                val TaskDAO = db.TasksDAO()
                val exec = Executors.newSingleThreadExecutor()
                exec.execute{
                    TaskDAO.saveTasks(Tasks(uid!!,preoritytTask.selectedItemPosition+1,nameTask.text.toString(),namesTask.text.toString(),textTask.text.toString(),calendarDate.toString()))
                }
            }
            super.onBackPressed()
        }
        btnTaskInfo.setOnClickListener {
            super.onBackPressed()
        }
        delTask.setOnClickListener(){
            var db: TasksBD = Room.databaseBuilder(this, TasksBD::class.java, DATABASE_NAME).build()
            val TaskDAO = db.TasksDAO()
            val exec = Executors.newSingleThreadExecutor()
            exec.execute{
                TaskDAO.delTasks(Tasks(uid!!,preoritytTask.selectedItemPosition+1,nameTask.text.toString(),namesTask.text.toString(),textTask.text.toString(),date.toString()))
            }
            super.onBackPressed()
        }
        addPreor.setOnClickListener(){
            val dialog = androidx.appcompat.app.AlertDialog.Builder(this)
            val dialodView = layoutInflater.inflate(R.layout.dialog_signin,null)
            val name = dialodView.findViewById<EditText>(R.id.namePreority)
            dialog.setTitle("Добавление приоритета")
            dialog.setView(dialodView)
            dialog.setCancelable(true)
            dialog.setPositiveButton("Добавить", { dialogInterface: DialogInterface, i: Int -> })
            dialog.setNegativeButton("Отмена"){ _, _ ->

            }
            val customDialog = dialog.create()
            customDialog.show()
            customDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
                Log.d("PREORITY", name.text.toString())
                var db: TasksBD = Room.databaseBuilder(this@MainActivity, TasksBD::class.java, DATABASE_NAME).build()
                val TaskDAO = db.TasksDAO()
                val exec = Executors.newSingleThreadExecutor()
                exec.execute{
                    TaskDAO.addPreoruty(Priority(0,name.text.toString()))
                }
                UpdateSpinner()
                customDialog.cancel()
            }
        }
    }
    fun UpdateSpinner(){
        var db: TasksBD = Room.databaseBuilder(this, TasksBD::class.java, DATABASE_NAME).build()
        val TaskDAO = db.TasksDAO()
        TaskDAO.getAllPreorString().observe(this,androidx.lifecycle.Observer {
            val arrayAdapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item,it)
            spin.adapter = arrayAdapter
        })
        spin.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, view: View?, position: Int, id: Long) {
                Toast.makeText(applicationContext, "$position ${spin.selectedItem}", Toast.LENGTH_SHORT).show()
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }
    }
}