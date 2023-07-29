package com.my.todo.feature.component

import androidx.annotation.VisibleForTesting
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.my.todo.model.data.Todo
import com.my.todo.ui.preview.TodosPreviewProvider

@Composable
fun TodoLayout(
    todo: Todo,
    onDelete: (id: Int) -> Unit = {},
    onEdit: (Todo) -> Unit = {},
    onChangeState: (Todo) -> Unit = {},
) {
    OutlinedCard(shape = MaterialTheme.shapes.small, modifier = Modifier.testTag(TEST_TAG_TODO_LAYOUT)) {
        SwipeAction(startAction = { onEdit(todo) }, endAction = { onDelete(todo.id) }) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = MaterialTheme.colorScheme.background)
                    .padding(end = 16.dp, top = 16.dp, bottom = 16.dp)

            ) {
                RadioButton(
                    selected = todo.state,
                    onClick = { onChangeState(todo.copy(state = !todo.state)) }
                )
                Spacer(modifier = Modifier.width(8.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(text = todo.title, style = MaterialTheme.typography.titleMedium.copy(
                        textDecoration = TextDecoration.LineThrough.takeIf { todo.state }
                    ))
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(text = todo.description, style = MaterialTheme.typography.bodyMedium.copy(
                        textDecoration = TextDecoration.LineThrough.takeIf { todo.state }
                    ))
                }
                Text(text = todo.dueDate, style = MaterialTheme.typography.labelMedium.copy(
                    textDecoration = TextDecoration.LineThrough.takeIf { todo.state }
                ))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun TodoLayoutPreview(
    @PreviewParameter(TodosPreviewProvider::class)
    todos: List<Todo>
) {
    TodoLayout(todo = todos[0])
}

@VisibleForTesting
const val TEST_TAG_TODO_LAYOUT = "todo_layout"