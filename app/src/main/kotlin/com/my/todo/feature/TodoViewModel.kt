package com.my.todo.feature

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.my.todo.data.repo.TodoRepo
import com.my.todo.model.data.Todo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TodoViewModel @Inject constructor(
    private val repo: TodoRepo
) : ViewModel() {

    val todos = repo.getTodos().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = emptyList()
    )

    fun updateTodo(todo: Todo) {
        viewModelScope.launch { repo.updateTodo(todo) }
    }

    fun deleteTodo(id: Int) {
        viewModelScope.launch { repo.delete(id) }
    }

    fun addTodo(
        title: String,
        description: String,
        dueDate: Long
    ) {
        viewModelScope.launch {
            repo.insertTodo(
                description = description,
                title = title,
                dueDate = dueDate
            )
        }
    }
}