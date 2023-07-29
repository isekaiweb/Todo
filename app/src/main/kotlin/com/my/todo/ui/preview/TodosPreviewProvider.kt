package com.my.todo.ui.preview

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.my.todo.model.data.Todo

class TodosPreviewProvider : PreviewParameterProvider<List<Todo>> {
    override val values: Sequence<List<Todo>>
        get() = sequenceOf(
            listOf(
                Todo(
                    id = 1,
                    title = "Work out",
                    state = false,
                    dueDate = "27 Mei 2023, 11:39",
                    description = "Need to Workout"
                ),
                Todo(
                    id = 2,
                    title = "Create Project",
                    state = true,
                    dueDate = "28 Mei 2023, 10:23",
                    description = "Project for porto folio"
                )
            )
        )
}