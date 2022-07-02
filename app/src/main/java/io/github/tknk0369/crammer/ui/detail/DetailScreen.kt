package io.github.tknk0369.crammer.ui.detail

import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController

@ExperimentalMaterialApi
@Composable
fun DetailScreen(
    navHostController: NavHostController,
    id: String?
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = id.toString())
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
        Text(text = id.toString(), modifier = Modifier.padding(paddingValues))
    }
}