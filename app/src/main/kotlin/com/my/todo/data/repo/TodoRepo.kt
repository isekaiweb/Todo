package com.my.todo.data.repo

import com.my.todo.model.data.Todo
import kotlinx.coroutines.flow.Flow

interface TodoRepo {
    fun getTodos(): Flow<List<Todo>>
    fun getTodo(id:Int): Flow<Todo>
    suspend fun insertTodo(
        description: String,
        title: String,
        dueDate: Long,
    )

    suspend fun updateTodo(value: Todo)

    suspend fun delete(id: Int)
}