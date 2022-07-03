package io.github.tknk0369.crammer.ui.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import io.github.tknk0369.crammer.ui.Screen
import io.github.tknk0369.crammer.ui.components.NameDialog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@ExperimentalMaterialApi
@Composable
fun HomeScreen(
    navHostController: NavHostController,
    modalBottomSheetState: ModalBottomSheetState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden),
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    viewModel: HomeViewModel = hiltViewModel()
) {
    val knowledgeList = viewModel.knowledgeList.collectAsState(initial = listOf())
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
            NameDialog(
                title = "Add New List",
                onDismissRequest = {
                    newListDialog = false
                },
                cancel = {
                    newListDialog = false
                },
                ok = { name ->
                    viewModel.addNewKnowledgeList(name)
                    newListDialog = false
                },
            )
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
            LazyColumn(modifier = Modifier.padding(paddingValues)) {
                items(items = knowledgeList.value, key = { it.id }) {
                    var rename by rememberSaveable {
                        mutableStateOf(false)
                    }
                    var expanded by rememberSaveable {
                        mutableStateOf(false)
                    }
                    ListItem(
                        modifier = Modifier.clickable {
                            navHostController.navigate(Screen.Detail.createRoute(it.id))
                        },
                        icon = {
                            Icon(
                                Icons.Default.List,
                                contentDescription = "list"
                            )
                        },
                        trailing = {
                            IconButton(
                                onClick = {
                                    expanded = true
                                }
                            ) {
                                Icon(Icons.Default.MoreVert, contentDescription = "menu")
                            }
                            DropdownMenu(
                                expanded = expanded,
                                onDismissRequest = { expanded = false }
                            ) {
                                DropdownMenuItem(
                                    onClick = {
                                        expanded = false
                                        navHostController.navigate(Screen.Test.createRoute(it.id))
                                    }
                                ) {
                                    Text("Test")
                                }
                                DropdownMenuItem(
                                    onClick = {
                                        expanded = false
                                        rename = true
                                    }
                                ) {
                                    Text("Rename")
                                }
                                DropdownMenuItem(
                                    onClick = {
                                        expanded = false
                                        viewModel.deleteKnowledgeList(it)
                                    }
                                ) {
                                    Text("Delete")
                                }
                            }

                        }
                    ) {
                        Text(text = it.name)
                        if (rename) {
                            NameDialog(
                                defaultName = it.name,
                                title = "New Name",
                                onDismissRequest = { rename = false },
                                cancel = { rename = false },
                                ok = { name ->
                                    viewModel.renameKnowledgeList(it, name)
                                    rename = false
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}