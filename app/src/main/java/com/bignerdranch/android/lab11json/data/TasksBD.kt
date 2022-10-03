package com.bignerdranch.android.lab11json.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.bignerdranch.android.lab11json.data.models.Priority
import com.bignerdranch.android.lab11json.data.models.Tasks

@Database(entities = [Priority::class, Tasks::class], version = 1)

abstract class TasksBD: RoomDatabase() {
    abstract fun TasksDAO(): TasksDAO

}