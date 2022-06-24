package io.github.tknk0369.crammer.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import io.github.tknk0369.crammer.ui.theme.CrammerTheme

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CrammerApp() {
    CrammerTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colors.background
        ) {
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = {
                            Text(text = "Home")
                        }
                    )
                }
            ) { paddingValues ->
                val items = listOf("a", "b", "c", "d", "e", "f", "g")
                LazyColumn(modifier = Modifier.padding(paddingValues)) {
                    items(items = items, key = { it }) {
                        ListItem(
                           modifier = Modifier.clickable {  }
                        ) {
                            Text(text = it)
                        }
                    }
                }
            }
        }
    }
}