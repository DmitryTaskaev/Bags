package com.bignerdranch.android.lab11json.data.models

import android.provider.BaseColumns
import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.bignerdranch.android.lab11json.data.TASKS_TABLE
import org.jetbrains.annotations.NonNls
import java.util.*


/**
 * Класс задачи
 * @property uid - Индивидуальный идентификатор задачи [UUID]
 * @property preorityId - Индивидуальный идентификатор приоритета [UUID]
 * @property nameTask - Название задачи [String]
 * @property creatTask - Создатель задачи [String]
 * @property text - Суть задачи [String]
 * @property dateTask - Дата задачи [String]
 *
 * @author Таскаев Дмитрий
 */

@Entity(tableName = TASKS_TABLE)
data class Tasks (
    @PrimaryKey(autoGenerate = false)
    @NonNull
    val uid: UUID,
    var preority_id: Int,
    var nameTask: String,
    var creatTask: String,
    var text: String,
    var dateTask: String
)