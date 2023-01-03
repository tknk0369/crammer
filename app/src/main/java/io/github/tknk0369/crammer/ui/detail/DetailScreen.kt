package io.github.tknk0369.crammer.ui.detail

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.FileUpload
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import io.github.tknk0369.crammer.ui.components.EditDialog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@ExperimentalMaterialApi
@Composable
fun DetailScreen(
    navHostController: NavHostController,
    id: String?,
    viewModel: DetailViewModel = hiltViewModel(),
    coroutineScope: CoroutineScope = rememberCoroutineScope()
) {
    LaunchedEffect(Unit) {
        viewModel.init(id)
    }
    val scrollState = rememberLazyListState()
    val name by viewModel.name.collectAsState()
    val knowledge by viewModel.knowledge.collectAsState()
    val questionFocusRequester = remember { FocusRequester() }
    val answerFocusRequester = remember { FocusRequester() }
    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument(),
        onResult = { viewModel.importKnowledgeFromCSV(it, id, context) }
    )
    val newDialog = remember {
        mutableStateOf(false)
    }
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
                },
                actions = {
                    IconButton(
                        onClick = {
                            launcher.launch(arrayOf("text/csv"))
                        }
                    ) {
                        Icon(Icons.Default.FileUpload, contentDescription = "Import CSV")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
//                    coroutineScope.launch {
//                        scrollState.animateScrollToItem(0)
//                        questionFocusRequester.requestFocus()
//                    }
                    newDialog.value = true
                }
            ) {
                Icon(Icons.Default.Add, contentDescription = "add")
            }
        }
    ) { paddingValues ->
        var newQuestion by rememberSaveable { mutableStateOf("") }
        var answer by rememberSaveable { mutableStateOf("") }
        if (newDialog.value) {
            Dialog(onDismissRequest = { newDialog.value = false }) {
                Surface(
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .padding(8.dp)
                            .fillMaxWidth(),
                    ) {
                        Text(
                            text = "Add new question",
                            textAlign = TextAlign.Center,
                            style = TextStyle(fontSize = 20.sp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(4.dp),
                        )
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
                        )
                        Row(
                            horizontalArrangement = Arrangement.End,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            TextButton(
                                onClick = {
                                    newDialog.value = false
                                    newQuestion = ""
                                    answer = ""
                                }
                            ) {
                                Text(text = "Cancel")
                            }
                            TextButton(
                                onClick = {
                                    if (newQuestion.isNotEmpty()) {
                                        if (answer.isNotEmpty()) {
                                            viewModel.addKnowledge(newQuestion, answer, id)
                                            newQuestion = ""
                                            answer = ""
                                        } else {
                                            questionFocusRequester.requestFocus()
                                        }
                                    }
                                }
                            ) {
                                Text(text = "Ok")
                            }
                        }
                    }
                }
            }
        }
        LazyColumn(
            modifier = Modifier.padding(paddingValues),
            state = scrollState
        ) {
            items(items = knowledge.sortedBy { it.question }, key = { it.id }) {
                var edit by rememberSaveable { mutableStateOf(false) }
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { edit = true }
                        .padding(8.dp)
                ) {
                    Text(text = it.question, modifier = Modifier.padding(4.dp))
                    Text(text = it.answer, modifier = Modifier.padding(4.dp))
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