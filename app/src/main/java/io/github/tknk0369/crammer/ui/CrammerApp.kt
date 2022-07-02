package io.github.tknk0369.crammer.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import io.github.tknk0369.crammer.ui.home.HomeScreen
import io.github.tknk0369.crammer.ui.theme.CrammerTheme

@ExperimentalMaterialApi
@Composable
fun CrammerApp(
    navHostController: NavHostController = rememberNavController()
) {
    CrammerTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colors.background
        ) {
            NavHost(
                navController = navHostController,
                startDestination = Screen.Home.route,
            ) {
                composable(Screen.Home.route) {
                    HomeScreen(navHostController)
                }
            }
        }
    }
}

sealed class Screen(val route: String) {
    object Home : Screen(route = "home")
}