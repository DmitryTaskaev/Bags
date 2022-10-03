package com.bignerdranch.android.lab11json.data

import androidx.lifecycle.LiveData
import androidx.room.*
import com.bignerdranch.android.lab11json.data.models.Priority
import com.bignerdranch.android.lab11json.data.models.Tasks
import java.util.*

@Dao
interface TasksDAO {

    /*
    *   Таблица Preority
    *   Запрос запрашивает все данные таблицы приоритета
    */

    @Query("SELECT * FROM $PRIOR_TABLE")
    fun getAllPreority(): LiveData<MutableList<Priority>>

    @Query("SELECT preority FROM $PRIOR_TABLE")
    fun getAllPreorString(): LiveData<MutableList<String>>

    @Query("SELECT * FROM $PRIOR_TABLE WHERE id=:id")
    fun getPreority(id: Int): LiveData<MutableList<Priority>>

    @Query("SELECT id FROM $PRIOR_TABLE WHERE preority =:name")
    fun getPreorityId(name:String): Int

    @Query("SELECT COUNT(*) FROM $PRIOR_TABLE")
    fun getCountPreority(): Int

    //Добавление приоритета
    @Insert
    fun addPreoruty(preority: Priority)

    //Изменения приоритета
    @Update
    fun savePreoruty(preority: Priority)

    //Удаление приоритета
    @Delete
    fun delPreoruty(preority: Priority)

    /*
    *   Таблица Tasks
    *   Запрос запрашивает все данные таблицы задач
    */

    @Query("SELECT * FROM $TASKS_TABLE")
    fun getAllTasks(): LiveData<MutableList<Tasks>>

    @Query("SELECT * FROM $TASKS_TABLE WHERE uid=:uid")
    fun getTasks(uid: UUID): LiveData<MutableList<Tasks>>

    //Добавление задачи
    @Insert
    fun addTasks(tasks: Tasks)

    //Изменение задачи
    @Update
    fun saveTasks(tasks: Tasks)

    //Удаление задачи
    @Delete
    fun delTasks(tasks: Tasks)
}