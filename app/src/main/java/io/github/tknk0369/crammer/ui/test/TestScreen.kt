package io.github.tknk0369.crammer.ui.test

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController

@Composable
fun TestScreen(
    navHostController: NavHostController,
    id: String?,
    viewModel: TestViewModel = hiltViewModel()
) {
    LaunchedEffect(Unit) {
        viewModel.init(id)
    }
    val name by viewModel.name.collectAsState()
    val knowledge by viewModel.knowledge.collectAsState()
    val randomKnowledge = knowledge.shuffled()
    var number by rememberSaveable { mutableStateOf(0) }
    var expanded by rememberSaveable { mutableStateOf(false) }
    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(
                        onClick = {
                            navHostController.popBackStack()
                        }
                    ) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "back")
                    }
                },
                title = {
                    Text(text = "$name Test")
                }
            )
        }
    ) {
        Column(
            modifier = Modifier
                .padding(it)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                Button(
                    onClick = {
                        number -= 1
                        expanded = false
                    },
                    enabled = number != 0
                ) {
                    Text(text = "Prev")
                }
                Button(
                    onClick = {
                        number += 1
                        expanded = false
                    },
                    enabled = knowledge.size - 1 != number
                ) {
                    Text(text = "Next")
                }
            }
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .clickable {
                        expanded = !expanded
                    }
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    Text(
                        text = "Question",
                        textAlign = TextAlign.Center,
                        style = TextStyle(fontSize = 20.sp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(4.dp)
                    )
                    Text(
                        text = try {
                            randomKnowledge[number].question
                        } catch (e: Exception) {
                            e.toString()
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(4.dp)
                    )
                    AnimatedVisibility(visible = expanded) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(4.dp)
                        ) {
                            Text(
                                text = "Answer",
                                textAlign = TextAlign.Center,
                                style = TextStyle(fontSize = 20.sp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(4.dp)
                            )
                            if(expanded) {
                                Text(
                                    text = try {
                                        randomKnowledge[number].answer
                                    } catch (e: Exception) {
                                        e.toString()
                                    },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(4.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}