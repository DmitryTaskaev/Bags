package com.bignerdranch.android.lab11json.data.models

import android.provider.BaseColumns
import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.bignerdranch.android.lab11json.data.PRIOR_TABLE
import java.util.*

/**
 * Класс приоритета
 * @property uid - Уникальный идентификатор задачи [UUID]
 * @property uid_task - идентификатор задачи [UUID]
 * @property preority - Приоритет задачи [String]
 *
 * @author Таскаев Дмитрий
 */

@Entity(tableName = PRIOR_TABLE)
data class Priority (
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    @ColumnInfo(index = true)
    var preority: String
)