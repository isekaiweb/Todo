package com.my.todo.testdouble

import com.my.todo.data.repo.TodoRepo
import com.my.todo.model.data.Todo
import com.my.todo.util.toDateString
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

/**
 * Test double for [TodoRepo]
 **/
class TestDefaultTodoRepo : TodoRepo {

    private val todos = MutableStateFlow(emptyList<Todo>())
    override fun getTodos(): Flow<List<Todo>> = todos


    override suspend fun insertTodo(description: String, title: String, dueDate: Long) {
        val todo = Todo(
            id = System.currentTimeMillis().toInt(),
            description = description,
            dueDate = dueDate.toDateString(),
            title = title,
        )
        todos.emit(todos.value.plus(todo))
    }

    override suspend fun updateTodo(value: Todo) {
        todos.emit(todos.value.map { todo ->
            if (todo.id == value.id) {
                todo.copy(
                    title = value.title,
                    dueDate = value.dueDate,
                    description = value.description
                )
            } else todo
        })
    }

    override suspend fun delete(id: Int) {
        todos.emit(todos.value.filter { it.id != id })
    }

}