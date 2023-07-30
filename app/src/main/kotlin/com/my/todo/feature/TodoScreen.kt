package com.my.todo.feature

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AddCircle
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import com.my.todo.R
import com.my.todo.feature.component.TodoLayout
import com.my.todo.feature.component.dialog.DialogDeleting
import com.my.todo.feature.component.dialog.DialogUpsert
import com.my.todo.model.data.Todo
import com.my.todo.ui.preview.TodosPreviewProvider
import com.my.todo.util.toDateString
import com.my.todo.util.toMillis

@Composable
fun TodoRoute(
    viewModel: TodoViewModel = hiltViewModel()
) {
    val todos by viewModel.todos.collectAsState()

    TodoScreen(
        todos = todos,
        onDelete = viewModel::deleteTodo,
        onAdd = viewModel::addTodo,
        onUpdate = viewModel::updateTodo
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun TodoScreen(
    todos: List<Todo>,
    onUpdate: (Todo) -> Unit = {},
    onAdd: (title: String, description: String, dueDate: Long) -> Unit = { _, _, _ -> },
    onDelete: (id: Int) -> Unit = {},
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val (onDeletingTodo, setOnDeletingTodo) = remember { mutableStateOf<Int?>(null) }
    val (onUpsertTodo, setOnUpsertTodo) = remember { mutableStateOf<Todo?>(null) }

    ConstraintLayout(modifier = Modifier.fillMaxSize()) {
        val (topBar, content, fab) = createRefs()
        CenterAlignedTopAppBar(title = {
            Text(
                text = stringResource(id = R.string.app_name),
                style = MaterialTheme.typography.titleLarge
            )
        }, modifier = Modifier.constrainAs(topBar) {
            top.linkTo(parent.top)
            start.linkTo(parent.start)
            end.linkTo(parent.end)
        })
        LazyColumn(
            modifier = Modifier.constrainAs(content) {
                top.linkTo(topBar.bottom)
                bottom.linkTo(parent.bottom)
                start.linkTo(parent.start)
                end.linkTo(parent.end)

                width = Dimension.fillToConstraints
                height = Dimension.fillToConstraints
            },
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 48.dp)
        ) {
            items(items = todos, key = { it.id }) { todo ->
                TodoLayout(
                    todo = todo,
                    onDelete = setOnDeletingTodo,
                    onChangeState = onUpdate,
                    onEdit = setOnUpsertTodo
                )
            }
        }
        FloatingActionButton(
            onClick = { setOnUpsertTodo(emptyTodo()) },
            modifier = Modifier.constrainAs(fab) {
                end.linkTo(parent.end, 16.dp)
                bottom.linkTo(parent.bottom, 24.dp)
            }) {
            Icon(
                imageVector = Icons.Rounded.AddCircle,
                contentDescription = stringResource(id = R.string.desc_fab_add_todo)
            )
        }
    }

    if (onDeletingTodo != null) {
        DialogDeleting(onDismiss = {
            setOnDeletingTodo(null)
        }, onConfirm = {
            onDelete(onDeletingTodo)
            setOnDeletingTodo(null)
        })
    }

    if (onUpsertTodo != null) {
        DialogUpsert(
            initialTodo = onUpsertTodo,
            onDismiss = { setOnUpsertTodo(null); keyboardController?.hide() },
            onConfirm = { todo ->
                if (todo.id == -1) {
                    onAdd(todo.title, todo.description, todo.dueDate.toMillis())
                } else onUpdate(todo)

                keyboardController?.hide()
                setOnUpsertTodo(null)
            }
        )
    }
}


@Preview(showBackground = true)
@Composable
private fun TodoScreenPreview(
    @PreviewParameter(TodosPreviewProvider::class)
    todos: List<Todo>
) {
    TodoScreen(todos = todos)
}

private fun emptyTodo() = Todo(
    id = -1,
    description = "",
    title = "",
    dueDate = System.currentTimeMillis().toDateString()
)