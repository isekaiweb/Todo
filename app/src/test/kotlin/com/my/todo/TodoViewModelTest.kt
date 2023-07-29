package com.my.todo

import com.my.todo.data.repo.TodoRepo
import com.my.todo.feature.TodoViewModel
import com.my.todo.testdouble.TestDefaultTodoRepo
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class TodoViewModelTest {

    @get:Rule
    val dispatcherRule = MainDispatcherRule()

    private lateinit var viewModel: TodoViewModel
    private val repo: TodoRepo = TestDefaultTodoRepo()


    @Before
    fun setup() {
        viewModel = TodoViewModel(
            repo = repo
        )
    }


    @Test
    fun `TodoViewModel should able observe todo`() = runTest {
        val collectJob = launch(UnconfinedTestDispatcher()) { viewModel.todos.collect() }

        // at first todos should be empty
        assertTrue(viewModel.todos.value.isEmpty())

        // adding
        viewModel.addTodo(
            title = "Tester",
            description = "Tester Desc",
            dueDate = System.currentTimeMillis()
        )

        assertEquals(repo.getTodos().first() ,viewModel.todos.value)
        assertTrue(viewModel.todos.value.isNotEmpty())

        viewModel.updateTodo(todo = viewModel.todos.value[0].copy(title = "Tester Description"))
        assertEquals(repo.getTodos().first()[0].description ,viewModel.todos.value[0].description)

        viewModel.deleteTodo(todo = viewModel.todos.value[0])
        assertTrue(viewModel.todos.value.isEmpty())

        collectJob.cancel()
    }
}