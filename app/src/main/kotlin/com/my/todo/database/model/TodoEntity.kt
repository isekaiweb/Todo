package com.my.todo.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Defines an database entity that stored todos
 **/
@Entity(
    tableName = "todo"
)
data class TodoEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 1,
    val title: String,
    val description: String,
    val dueDate: Long
)
