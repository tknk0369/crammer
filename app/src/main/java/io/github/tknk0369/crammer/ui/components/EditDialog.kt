package io.github.tknk0369.crammer.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog

@Composable
fun EditDialog(
    defaultQuestion: String = "",
    defaultAnswer: String = "",
    title: String,
    onDismissRequest: () -> Unit,
    cancel: () -> Unit,
    ok: (question: String, answer: String) -> Unit,
    delete: () -> Unit,
) {
    val questionFocusRequester = remember { FocusRequester() }
    val answerFocusRequester = remember { FocusRequester() }
    LaunchedEffect(Unit) {
        try {
            questionFocusRequester.requestFocus()
        } catch (e: Exception) {

        }
    }
    Dialog(
        onDismissRequest = onDismissRequest
    ) {
        var question by rememberSaveable { mutableStateOf(defaultQuestion) }
        var answer by rememberSaveable { mutableStateOf(defaultAnswer) }
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
                    value = question,
                    onValueChange = { question = it },
                    modifier = Modifier
                        .focusRequester(questionFocusRequester)
                        .fillMaxWidth()
                        .padding(4.dp, 0.dp),
                    label = { Text(text = "Question") },
                    keyboardActions = KeyboardActions(
                        onDone = {
                            answerFocusRequester.requestFocus()
                        }
                    ),
                    singleLine = true,
                )
                OutlinedTextField(
                    value = answer,
                    onValueChange = { answer = it },
                    modifier = Modifier
                        .focusRequester(answerFocusRequester)
                        .fillMaxWidth()
                        .padding(4.dp, 0.dp),
                    label = { Text(text = "Answer") },
                    keyboardActions = KeyboardActions(
                        onDone = {
                            questionFocusRequester.requestFocus()
                        }
                    ),
                    singleLine = true,
                )
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    IconButton(
                        onClick = {
                            delete()
                        }
                    ) {
                        Icon(Icons.Default.Delete, contentDescription = "delete")
                    }
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
                                ok(question, answer)
                            }
                        ) {
                            Text(text = "Ok")
                        }
                    }
                }
            }
        }
    }
}