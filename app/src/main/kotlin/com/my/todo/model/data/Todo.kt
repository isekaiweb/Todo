package com.my.todo.model.data

/**
 * External data layer representation of a fully populated todos
 **/
data class Todo(
    val id: Int,
    val title: String,
    val description: String,
    val dueDate: String,
)
