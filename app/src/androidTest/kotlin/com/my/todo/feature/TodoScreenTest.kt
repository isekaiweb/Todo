package com.my.todo.feature

import androidx.compose.ui.res.stringResource
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.swipeLeft
import com.my.todo.R
import com.my.todo.feature.component.TEST_TAG_END_COMPONENT
import com.my.todo.ui.preview.TodosPreviewProvider
import org.junit.Rule
import org.junit.Test

class TodoScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()


    @Test
    fun whenHasOnDeleting_thenDialogDeletingShouldShown() {
        var deletingTitle = ""
        var deleteBtn = ""

        composeTestRule.setContent {
            TodoScreen(todos = TodosPreviewProvider().values.iterator().next())
            deletingTitle = stringResource(id = R.string.title_deleted_todo)
            deleteBtn = stringResource(id = R.string.delete)
        }

        // at first the dialog must not shown
        composeTestRule.onNodeWithText(deletingTitle).assertDoesNotExist()

        // swipe layout to the left to able clicking end component
        composeTestRule.onAllNodesWithTag("todo_layout")[0].performTouchInput {
            swipeLeft()
        }

        composeTestRule.onAllNodesWithTag(TEST_TAG_END_COMPONENT)[0].performClick()
        composeTestRule.onNodeWithText(deletingTitle).assertIsDisplayed()

        // then check if delete btn click the dialog must not shown
        composeTestRule.onNodeWithText(deleteBtn).performClick()
        composeTestRule.onNodeWithText(deletingTitle).assertDoesNotExist()
    }
}