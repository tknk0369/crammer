package io.github.tknk0369.crammer.ui.detail

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import io.github.tknk0369.crammer.ui.components.EditDialog

@ExperimentalMaterialApi
@Composable
fun DetailScreen(
    navHostController: NavHostController,
    id: String?,
    viewModel: DetailViewModel = hiltViewModel()
) {
    LaunchedEffect(Unit) {
        viewModel.init(id)
    }
    val scrollState = rememberLazyListState()
    val name by viewModel.name.collectAsState()
    val knowledge by viewModel.knowledge.collectAsState()
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = name)
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            navHostController.popBackStack()
                        }
                    ) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "back")
                    }
                }
            )
        }
    ) { paddingValues ->
        var newQuestion by rememberSaveable { mutableStateOf("") }
        var answer by rememberSaveable { mutableStateOf("") }
        val questionFocusRequester = remember { FocusRequester() }
        val answerFocusRequester = remember { FocusRequester() }
        LazyColumn(
            modifier = Modifier.padding(paddingValues),
            state = scrollState
        ) {
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    Column {
                        OutlinedTextField(
                            value = newQuestion,
                            onValueChange = { newQuestion = it },
                            modifier = Modifier
                                .focusRequester(questionFocusRequester)
                                .padding(4.dp, 0.dp),
                            label = { Text(text = "New question") },
                            keyboardActions = KeyboardActions(
                                onDone = {
                                    if (newQuestion.isNotEmpty()) {
                                        if (answer.isNotEmpty()) {
                                            viewModel.addKnowledge(newQuestion, answer, id)
                                            newQuestion = ""
                                            answer = ""
                                        } else {
                                            answerFocusRequester.requestFocus()
                                        }
                                    }
                                }
                            ),
                            singleLine = true,
                            colors = TextFieldDefaults.outlinedTextFieldColors(
                                focusedBorderColor = Color.Transparent,
                                unfocusedBorderColor = Color.Transparent
                            ),
                        )
                        OutlinedTextField(
                            value = answer,
                            onValueChange = { answer = it },
                            modifier = Modifier
                                .focusRequester(answerFocusRequester)
                                .padding(4.dp, 0.dp),
                            label = { Text(text = "Answer") },
                            keyboardActions = KeyboardActions(
                                onDone = {
                                    if (answer.isNotEmpty()) {
                                        questionFocusRequester.requestFocus()
                                        if (newQuestion.isNotEmpty()) {
                                            viewModel.addKnowledge(newQuestion, answer, id)
                                            newQuestion = ""
                                            answer = ""
                                        }
                                    }
                                }
                            ),
                            singleLine = true,
                            colors = TextFieldDefaults.outlinedTextFieldColors(
                                focusedBorderColor = Color.Transparent,
                                unfocusedBorderColor = Color.Transparent
                            ),
                        )
                    }
                }
            }
            items(items = knowledge, key = { it.id }) {
                var edit by rememberSaveable { mutableStateOf(false) }
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .clickable { edit = true }
                ) {
                    Column {
                        Text(text = "question: ${it.question}", modifier = Modifier.padding(4.dp))
                        Text(text = "answer: ${it.answer}", modifier = Modifier.padding(4.dp))
                    }
                    if (edit) {
                        EditDialog(
                            defaultQuestion = it.question,
                            defaultAnswer = it.answer,
                            title = "Edit question",
                            onDismissRequest = { edit = false },
                            cancel = { edit = false },
                            ok = { question, answer ->
                                edit = false
                                viewModel.updateKnowledge(question, answer, it)
                            },
                            delete = {
                                edit = false
                                viewModel.deleteKnowledge(it)
                            }
                        )
                    }
                }
            }
        }
    }
}