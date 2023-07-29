package com.my.todo.feature.component

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp

@Composable
internal fun FieldEdit(
    modifier: Modifier = Modifier,
    modifierTextField: Modifier = Modifier,
    @StringRes labelId: Int,
    value: String,
    readOnly: Boolean = false,
    enabled: Boolean = true,
    trailingIcon: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    colors: TextFieldColors = OutlinedTextFieldDefaults.colors(),
    onChangeValue: (String) -> Unit,
    maxLines: Int = 1,
) {
    Column(modifier) {
        Text(text = stringResource(id = labelId), style = MaterialTheme.typography.bodyMedium)
        Spacer(modifier = Modifier.height(4.dp))
        OutlinedTextField(
            value = value,
            onValueChange = onChangeValue,
            shape = MaterialTheme.shapes.small,
            modifier = modifierTextField.fillMaxWidth(),
            maxLines = maxLines,
            readOnly = readOnly,
            trailingIcon = trailingIcon,
            leadingIcon = leadingIcon,
            enabled = enabled,
            colors = colors,
        )
    }
}

@Composable
internal fun DisabledFieldEdit(
    modifier: Modifier = Modifier,
    modifierTextField: Modifier = Modifier,
    @StringRes labelId: Int,
    value: String,
    trailingIcon: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
) {
    FieldEdit(
        labelId = labelId,
        value = value,
        onChangeValue = {},
        readOnly = true,
        enabled = false,
        trailingIcon = trailingIcon,
        leadingIcon = leadingIcon,
        modifier = modifier,
        modifierTextField = modifierTextField,
        colors = OutlinedTextFieldDefaults.colors(
            disabledTextColor = LocalContentColor.current,
            disabledTrailingIconColor = LocalContentColor.current,
            disabledBorderColor = MaterialTheme.colorScheme.outline,
        ),
    )
}
