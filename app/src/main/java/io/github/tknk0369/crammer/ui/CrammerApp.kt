package io.github.tknk0369.crammer.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import io.github.tknk0369.crammer.ui.detail.DetailScreen
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
                composable(
                    route = Screen.Home.route
                ) {
                    HomeScreen(navHostController)
                }
                composable(
                    route = Screen.Detail.route,
                    arguments = listOf(navArgument("id") { type = NavType.StringType })
                ) {
                    DetailScreen(navHostController)
                }
            }
        }
    }
}

sealed class Screen(val route: String) {
    object Home : Screen(route = "home")
    object Detail : Screen(route = "detail/{id}") {
        fun createRoute(id: String) = "detail/$id"
    }
}