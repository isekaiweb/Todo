package com.my.todo.feature.component

import androidx.annotation.VisibleForTesting
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.my.todo.ui.theme.TodoTheme
import kotlin.math.roundToInt


/**
 *  Swipe action is a component that able swipe horizontally
 *  in this layout you can't freely customize the component for end and start, therefore you can only customize :
 *  [endAction] for the action of end component
 *  [startAction] for the action of start component
 *  [endBackgroundColor] [startBackgroundColor] for the background
 *  [startIcon] [endIcon] for the icon
 **/
@Composable
fun SwipeAction(
    threshold: Dp = 60.dp,
    density: Density = LocalDensity.current,
    endIcon: ImageVector = Icons.Rounded.Delete,
    endAction: () -> Unit = {},
    endBackgroundColor: Color = MaterialTheme.colorScheme.error,
    startIcon: ImageVector = Icons.Rounded.Edit,
    startAction: () -> Unit = {},
    startBackgroundColor: Color = MaterialTheme.colorScheme.tertiary,
    content: @Composable () -> Unit = {}
) {

    val (offsetX, setOffsetX) = rememberSaveable { mutableStateOf(0f) }
    val thresholdPx = remember { with(density) { threshold.toPx() } }

    ConstraintLayout {
        val (container, actionEnd, actionStart) = createRefs()
        Box(
            modifier = Modifier
                .testTag(TEST_TAG_START_COMPONENT)
                .zIndex(2f.takeIf { offsetX == thresholdPx } ?: 0f)
                .constrainAs(actionStart) {
                    start.linkTo(container.start)
                    top.linkTo(container.top)
                    bottom.linkTo(container.bottom)

                    height = Dimension.fillToConstraints
                }

                .background(color = startBackgroundColor)
                .fillMaxHeight()
                .width(threshold)
                .clickable { startAction();setOffsetX(0f) },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = startIcon, contentDescription = "edit icon",
                tint = contentColorFor(startBackgroundColor)
            )
        }
        Box(
            modifier = Modifier
                .testTag(TEST_TAG_END_COMPONENT)
                .zIndex(2f.takeIf { offsetX == -thresholdPx } ?: 0f)
                .constrainAs(actionEnd) {
                    end.linkTo(container.end)
                    top.linkTo(container.top)
                    bottom.linkTo(container.bottom)

                    height = Dimension.fillToConstraints
                }
                .background(color = endBackgroundColor)
                .fillMaxHeight()
                .width(threshold)
                .clickable { endAction();setOffsetX(0f) },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = endIcon,
                contentDescription = "delete icon",
                tint = contentColorFor(endBackgroundColor)
            )

        }

        Container(
            offsetX = offsetX,
            thresholdPx = thresholdPx,
            content = content,
            setOffsetX = setOffsetX,
            modifier = Modifier.constrainAs(container) {
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            })
    }

}

@Composable
private fun Container(
    modifier: Modifier = Modifier,
    offsetX: Float,
    setOffsetX: (Float) -> Unit,
    thresholdPx: Float,
    content: @Composable () -> Unit
) {


    val animateOffsetX by animateFloatAsState(
        targetValue = offsetX,
        label = "animate offset x"
    )

    val state = rememberDraggableState(onDelta = { delta ->
        val movement = offsetX + delta

        val isValid =  movement >= -(thresholdPx) && movement <= thresholdPx
        if (isValid) {
            setOffsetX(movement)
        }
    })

    Box(
        modifier = modifier
            .draggable(
                orientation = Orientation.Horizontal,
                state = state,
                onDragStopped = {

                    val startSwipeReleased = {
                        if (offsetX < thresholdPx.minus(50)) setOffsetX(0.0f)
                        else setOffsetX(thresholdPx)
                    }

                    val endSwipeReleased = {
                        if (offsetX > -(thresholdPx.minus(50))) setOffsetX(0.0f)
                        else setOffsetX(-thresholdPx)
                    }

                    val isSwipedStart = offsetX in 0.0..thresholdPx.toDouble()
                    val isSwipedEnd = offsetX >= -(thresholdPx) && offsetX <= 0.0f

                    when {
                        isSwipedStart -> startSwipeReleased()
                        isSwipedEnd -> endSwipeReleased()
                    }
                }
            )
            .offset { IntOffset(animateOffsetX.roundToInt(), 0) }
    ) {
        Column {
            content()
        }
    }
}


@Preview(showBackground = true)
@Composable
private fun SwipeActionPreview() {
    TodoTheme {
        SwipeAction(endAction = {}) {
            Column(
                modifier = Modifier
                    .height(100.dp)
                    .fillMaxWidth()
                    .background(color = MaterialTheme.colorScheme.primary),
            ) {
            }
        }
    }
}

@VisibleForTesting
const val TEST_TAG_START_COMPONENT = "start component"

@VisibleForTesting
const val TEST_TAG_END_COMPONENT = "end component"