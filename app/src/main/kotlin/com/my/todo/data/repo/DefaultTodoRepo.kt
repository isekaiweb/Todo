package com.my.todo.data.repo

import com.my.todo.database.dao.TodoDao
import com.my.todo.database.model.TodoEntity
import com.my.todo.database.model.asExternalModel
import com.my.todo.model.data.Todo
import com.my.todo.util.toMillis
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DefaultTodoRepo @Inject constructor(
    private val dao: TodoDao,
) : TodoRepo {

    override fun getTodos(): Flow<List<Todo>> =
        dao.getTodos().map { flow -> flow.map { it.asExternalModel() } }

    override fun getTodo(id: Int): Flow<Todo> = flow {
        try {
            dao.getTodo(id).map {
                val todo = it?.asExternalModel()

                if (todo != null) {
                    emit(todo)
                } else emptyFlow<Todo>()
            }
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }

    override suspend fun insertTodo(description: String, title: String, dueDate: Long) {
        val entity = TodoEntity(
            description = description,
            title = title,
            dueDate = dueDate
        )
        dao.insertOrReplaceTodo(entity)
    }

    override suspend fun updateTodo(value: Todo) {
        val entity = TodoEntity(
            id = value.id,
            dueDate = value.dueDate.toMillis(),
            title = value.title,
            description = value.description
        )
        dao.insertOrReplaceTodo(entity)
    }
}