package com.my.todo.feature.component.dialog

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePickerFormatter
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.my.todo.R
import com.my.todo.feature.component.DisabledFieldEdit
import com.my.todo.feature.component.FieldEdit
import com.my.todo.model.data.Todo
import com.my.todo.ui.preview.TodosPreviewProvider
import com.my.todo.ui.theme.TodoTheme
import com.my.todo.util.DEFAULT_PATTERN
import com.my.todo.util.toDateString
import com.my.todo.util.toMillis

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DialogUpsert(
    initialTodo: Todo,
    onDismiss: () -> Unit = {},
    onConfirm: (Todo) -> Unit = {}
) {
    var shouldShowDatePicker by remember { mutableStateOf(false) }
    val (currentTodo, setCurrentTodo) = remember(initialTodo) {
        mutableStateOf(initialTodo)
    }
    val isValid = currentTodo.title.isNotEmpty() && currentTodo.description.isNotEmpty()

    Dialog(onDismissRequest = onDismiss) {
        Card(modifier = Modifier.imePadding()) {
            Text(
                text = stringResource(id = R.string.title_update_todo.takeIf { initialTodo.id != -1 }
                    ?: R.string.title_add_todo),
                style = MaterialTheme.typography.titleLarge.copy(textAlign = TextAlign.Center),
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(16.dp)
            )

            FieldEdit(
                labelId = R.string.title_label,
                value = currentTodo.title,
                onChangeValue = { setCurrentTodo(currentTodo.copy(title = it)) },
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 8.dp)
            )
            FieldEdit(
                labelId = R.string.description_label,
                value = currentTodo.description,
                onChangeValue = { setCurrentTodo(currentTodo.copy(description = it)) },
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 8.dp),
                maxLines = 4
            )
            DisabledFieldEdit(
                labelId = R.string.due_date_label,
                value = currentTodo.dueDate,
                trailingIcon = {
                    IconButton(onClick = { shouldShowDatePicker = true }) {
                        Icon(
                            imageVector = Icons.Rounded.DateRange,
                            contentDescription = "Date Icon"
                        )
                    }
                }, modifier = Modifier.padding(start = 16.dp, end = 16.dp)
            )

            Button(
                onClick = { onConfirm(currentTodo) },
                shape = MaterialTheme.shapes.small,
                modifier = Modifier
                    .padding(
                        top = 24.dp,
                        start = 16.dp, end = 16.dp, bottom = 4.dp
                    )
                    .fillMaxWidth(),
                enabled = isValid
            ) {
                Text(text = stringResource(id = R.string.save))
            }
            OutlinedButton(
                onClick = onDismiss,
                shape = MaterialTheme.shapes.small,
                modifier = Modifier
                    .padding(
                        start = 16.dp, end = 16.dp, bottom = 16.dp
                    )
                    .fillMaxWidth()
            ) {
                Text(text = stringResource(id = R.string.cancel))
            }
        }
    }

    if (shouldShowDatePicker) {
        val initialSelectedDateMillis = initialTodo.dueDate.toMillis()

        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = initialSelectedDateMillis,
        )

        val performChangeDate = {
            setCurrentTodo(currentTodo.copy(dueDate = datePickerState.selectedDateMillis!!.toDateString()))
            shouldShowDatePicker = false
        }


        DatePickerDialog(
            onDismissRequest = { shouldShowDatePicker = false },
            confirmButton = {
                Button(
                    onClick = performChangeDate,
                    shape = MaterialTheme.shapes.small,
                ) {
                    Text(
                        text = stringResource(id = R.string.done),
                    )
                }
            },
            dismissButton = {
                OutlinedButton(
                    onClick = { datePickerState.setSelection(initialSelectedDateMillis) },
                    shape = MaterialTheme.shapes.small,
                ) {
                    Text(
                        text = stringResource(id = R.string.reset),
                    )
                }
            },
            shape = MaterialTheme.shapes.small,
            colors = DatePickerDefaults.colors(
                containerColor = MaterialTheme.colorScheme.background,
            ),
        ) {
            DatePicker(
                title = {
                    Text(
                        text = stringResource(id = R.string.title_date_picker_due_date),
                        style = MaterialTheme.typography.labelMedium,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    )
                },
                state = datePickerState,
                dateFormatter = DatePickerFormatter(
                    selectedDateDescriptionSkeleton = DEFAULT_PATTERN,
                    yearSelectionSkeleton = "MMM yyyy",
                ),
                dateValidator = { selectedDate -> selectedDate >= System.currentTimeMillis() },
                showModeToggle = false,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun DialogUpsertPreview(
    @PreviewParameter(TodosPreviewProvider::class)
    todos: List<Todo>
) {
    TodoTheme {
        DialogUpsert(initialTodo = todos[0])
    }
}