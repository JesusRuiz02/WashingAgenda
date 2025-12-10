package com.jesusruiz.washingagenda.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.jesusruiz.washingagenda.views.HomeView
import com.jesusruiz.washingagenda.views.LoginView

sealed class Screen(val route: String)
{
    data object Login : Screen("Login")
    data object Home : Screen("Home")
    data object Calendar : Screen("Calendar/{userId}")

}

@Composable
fun NavManager()
{
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "Login"){
        composable(Screen.Login.route) {
            LoginView(navController)
        }
        composable(Screen.Home.route) {
            HomeView(navController)
        }
    }
}