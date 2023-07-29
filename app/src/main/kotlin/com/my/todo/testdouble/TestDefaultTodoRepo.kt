package com.my.todo.testdouble

import com.my.todo.data.repo.TodoRepo
import com.my.todo.model.data.Todo
import com.my.todo.util.toDateString
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

/**
 * Test double for [TodoRepo]
 **/
class TestDefaultTodoRepo : TodoRepo {

    private val todos = mutableListOf<Todo>()
    override fun getTodos(): Flow<List<Todo>> = flowOf(todos)

    override fun getTodo(id: Int): Flow<Todo> = flowOf(todos.first { it.id == id })

    override suspend fun insertTodo(description: String, title: String, dueDate: Long) {
        val todo = Todo(
            id = System.currentTimeMillis().toInt(),
            description = description,
            dueDate = dueDate.toDateString(),
            title = title
        )
        todos.add(todo)
    }

    override suspend fun updateTodo(value: Todo) {
        todos.map { todo ->
            if (todo.id == value.id) {
                todo.copy(
                    title = value.title,
                    dueDate = value.dueDate,
                    description = value.description
                )
            } else todo
        }
    }

    override suspend fun delete(id: Int) {
        val todo = todos.find { it.id == id }
        todos.remove(todo)
    }

}