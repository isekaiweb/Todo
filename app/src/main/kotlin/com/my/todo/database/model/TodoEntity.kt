package com.my.todo.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.my.todo.model.data.Todo
import com.my.todo.util.toDateString

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
    val dueDate: Long,
    val state: Boolean = false,
)

fun TodoEntity.asExternalModel() = Todo(
    id = id,
    title = title,
    description = description,
    dueDate = dueDate.toDateString(),
    state = state
)