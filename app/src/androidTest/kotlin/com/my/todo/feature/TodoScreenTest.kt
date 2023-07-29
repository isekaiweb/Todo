package com.my.todo.feature

import androidx.compose.ui.res.stringResource
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.swipeLeft
import androidx.compose.ui.test.swipeRight
import com.my.todo.R
import com.my.todo.feature.component.TEST_TAG_END_COMPONENT
import com.my.todo.feature.component.TEST_TAG_START_COMPONENT
import com.my.todo.feature.component.TEST_TAG_TODO_LAYOUT
import com.my.todo.ui.preview.TodosPreviewProvider
import org.junit.Rule
import org.junit.Test

class TodoScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()


    @Test
    fun whenHasOnDeleting_thenDialogDeletingShouldShown() {
        var deletingTitle = ""
        var deleteBtnTxt = ""

        composeTestRule.setContent {
            TodoScreen(todos = TodosPreviewProvider().values.iterator().next())
            deletingTitle = stringResource(id = R.string.title_deleted_todo)
            deleteBtnTxt = stringResource(id = R.string.delete)
        }

        // at first the dialog must not shown
        composeTestRule.onNodeWithText(deletingTitle).assertDoesNotExist()

        // swipe layout to the left to able clicking end component
        composeTestRule.onAllNodesWithTag(TEST_TAG_TODO_LAYOUT)[0].performTouchInput {
            swipeLeft()
        }

        composeTestRule.onAllNodesWithTag(TEST_TAG_END_COMPONENT)[0].performClick()
        composeTestRule.onNodeWithText(deletingTitle).assertIsDisplayed()

        // then check if delete btn click the dialog must not shown
        composeTestRule.onNodeWithText(deleteBtnTxt).performClick()
        composeTestRule.onNodeWithText(deletingTitle).assertDoesNotExist()
    }

    @Test
    fun whenHasOnUpsert_bySwipingToLeft_thenDialogUpsertShouldShown() {
        var titleUpdateTodo = ""
        var saveBtnTxt = ""

        composeTestRule.setContent {
            TodoScreen(todos = TodosPreviewProvider().values.iterator().next())
            titleUpdateTodo = stringResource(id = R.string.title_update_todo)
            saveBtnTxt = stringResource(id = R.string.save)
        }

        // at first the dialog must not shown
        composeTestRule.onNodeWithText(titleUpdateTodo).assertDoesNotExist()

        // swipe layout to the right to able clicking end component
        composeTestRule.onAllNodesWithTag(TEST_TAG_TODO_LAYOUT)[0].performTouchInput {
            swipeRight()
        }

        composeTestRule.onAllNodesWithTag(TEST_TAG_START_COMPONENT)[0].performClick()
        composeTestRule.onNodeWithText(titleUpdateTodo).assertIsDisplayed()

        // then check if delete btn click the dialog must not shown
        composeTestRule.onNodeWithText(saveBtnTxt).performClick()
        composeTestRule.onNodeWithText(titleUpdateTodo).assertDoesNotExist()
    }


    @Test
    fun whenHasOnUpsert_byClickingFAB_thenDialogUpsertShouldShown() {
        var titleAddTodo = ""
        var cancelBtnTxt = ""
        var fabBtnTxt = ""

        composeTestRule.setContent {
            TodoScreen(todos = TodosPreviewProvider().values.iterator().next())
            titleAddTodo = stringResource(id = R.string.title_add_todo)
            cancelBtnTxt = stringResource(id = R.string.cancel)
            fabBtnTxt = stringResource(id = R.string.desc_fab_add_todo)
        }

        // at first the dialog must not shown
        composeTestRule.onNodeWithText(titleAddTodo).assertDoesNotExist()

        // then click fab to show dialog
        composeTestRule.onNodeWithContentDescription(fabBtnTxt).performClick()
        composeTestRule.onNodeWithText(titleAddTodo).assertIsDisplayed()

        // then check if delete btn click the dialog must not shown
        composeTestRule.onNodeWithText(cancelBtnTxt).performClick()
        composeTestRule.onNodeWithText(titleAddTodo).assertDoesNotExist()
    }
}