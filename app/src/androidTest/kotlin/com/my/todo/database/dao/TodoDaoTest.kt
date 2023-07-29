package com.my.todo.database.dao

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.my.todo.database.TodoDatabase
import com.my.todo.database.model.TodoEntity
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes

@OptIn(ExperimentalCoroutinesApi::class)
class TodoDaoTest {
    private lateinit var dao: TodoDao
    private lateinit var db: TodoDatabase

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context,
            TodoDatabase::class.java
        ).build()

        dao = db.todoDao()
    }


    @Test
    fun should_fetch_todo_by_descending_due_date() = runTest {
        val currentTime = System.currentTimeMillis()
        dao.insertAll(
            testTodo(id = 1, dueDate = currentTime),
            testTodo(id = 2, dueDate = currentTime.plus(20.hours.inWholeMilliseconds)),
            testTodo(id = 3, dueDate = currentTime.plus(4.minutes.inWholeMilliseconds)),
            testTodo(id = 4, dueDate = currentTime.plus(1.days.inWholeMilliseconds)),
        )

        val savedTodos = dao.getTodos().first().map { it.id }
        assertEquals(
            listOf(
                4, 2, 3, 1
            ), savedTodos
        )
    }

    @Test
    fun should_deleted_the_given_todo() = runTest {
        val todo = testTodo(id = 1, dueDate = 4)
        dao.insertOrReplaceTodo(todo)
        dao.deleteTodo(todo.id)

        assertEquals(
            emptyList(),
            dao.getTodos().first()
        )
    }

    @Test
    fun should_updated_the_given_todo() = runTest {
        val todo = testTodo(id = 1, dueDate = 4)
        dao.insertOrReplaceTodo(todo)

        dao.insertOrReplaceTodo(
            todo.copy(
                title = "title",
                state = true,
                description = "description",
                dueDate = 6
            )
        )

        assertEquals(
            "title",
            dao.getTodos().first()[0].title
        )
        assertEquals(
            "description",
            dao.getTodos().first()[0].description
        )
        assertEquals(
            true,
            dao.getTodos().first()[0].state
        )
        assertEquals(
            6,
            dao.getTodos().first()[0].dueDate
        )
    }
}

private fun testTodo(
    id: Int,
    dueDate: Long
) = TodoEntity(
    id = id,
    title = "",
    description = "",
    dueDate = dueDate
)