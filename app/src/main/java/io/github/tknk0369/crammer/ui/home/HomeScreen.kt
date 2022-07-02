package io.github.tknk0369.crammer.ui.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavHostController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@ExperimentalMaterialApi
@Composable
fun HomeScreen(
    navHostController: NavHostController,
    modalBottomSheetState: ModalBottomSheetState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden),
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
) {
    var newListDialog by rememberSaveable { mutableStateOf(false) }
    ModalBottomSheetLayout(
        sheetState = modalBottomSheetState,
        sheetContent = {
            Column {
                Spacer(modifier = Modifier.padding(4.dp))
                ListItem(
                    modifier = Modifier.clickable {
                        newListDialog = true
                        coroutineScope.launch {
                            modalBottomSheetState.hide()
                        }
                    },
                    icon = { Icon(Icons.Default.List, contentDescription = "Add New List") }
                ) {
                    Text(text = "Add New List")
                }
            }
        },
        sheetShape = RoundedCornerShape(8.dp, 8.dp)
    ) {
        if (newListDialog) {
            val focusRequester = remember {
                FocusRequester()
            }
            LaunchedEffect(Unit) {
                focusRequester.requestFocus()
            }
            Dialog(
                onDismissRequest = { newListDialog = false },
            ) {
                var name by rememberSaveable { mutableStateOf("") }
                Surface(
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .padding(8.dp)
                            .fillMaxWidth(),
                    ) {
                        Text(
                            text = "Add New List",
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.Medium,
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
                                    newListDialog = false
                                }
                            ) {
                                Text(text = "Cancel")
                            }
                            TextButton(
                                onClick = {
                                    newListDialog = false
                                }
                            ) {
                                Text(text = "Ok")
                            }
                        }
                    }
                }
            }
        }
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(text = "Home")
                    }
                )
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = {
                        coroutineScope.launch {
                            modalBottomSheetState.show()
                        }
                    }
                ) {
                    Icon(Icons.Default.Add, contentDescription = "add")
                }
            }
        ) { paddingValues ->
            val items = listOf("a", "b", "c", "d", "e", "f", "g")
            LazyColumn(modifier = Modifier.padding(paddingValues)) {
                items(items = items, key = { it }) {
                    ListItem(
                        modifier = Modifier.clickable { },
                        icon = {
                            Icon(
                                Icons.Default.List,
                                contentDescription = "list"
                            )
                        },
                        trailing = {
                            IconButton(
                                onClick = {

                                }
                            ) {
                                Icon(Icons.Default.MoreVert, contentDescription = "menu")
                            }
                        }
                    ) {
                        Text(text = it)
                    }
                }
            }
        }
    }
}