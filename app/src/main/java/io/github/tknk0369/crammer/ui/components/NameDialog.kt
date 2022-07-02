package io.github.tknk0369.crammer.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import java.lang.Exception

@Composable
fun NameDialog(
    defaultName: String = "",
    title: String,
    onDismissRequest: () -> Unit,
    cancel: () -> Unit,
    ok: (name: String) -> Unit
) {
    val focusRequester = remember {
        FocusRequester()
    }
    LaunchedEffect(Unit) {
        try {
            focusRequester.requestFocus()
        } catch (e: Exception) {

        }
    }
    Dialog(
        onDismissRequest = onDismissRequest,
    ) {
        var name by rememberSaveable { mutableStateOf(defaultName) }
        Surface(
            shape = RoundedCornerShape(8.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth(),
            ) {
                Text(
                    text = title,
                    textAlign = TextAlign.Center,
                    style = TextStyle(fontSize = 20.sp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp),
                )
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp)
                        .focusRequester(focusRequester)
                )
                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    TextButton(
                        onClick = {
                            cancel()
                        }
                    ) {
                        Text(text = "Cancel")
                    }
                    TextButton(
                        onClick = {
                            ok(name)
                        }
                    ) {
                        Text(text = "Ok")
                    }
                }
            }
        }
    }
}