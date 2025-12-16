package com.jesusruiz.washingagenda.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.jesusruiz.washingagenda.viewModel.AdminViewModel
import com.jesusruiz.washingagenda.viewModel.LoginViewModel
import com.jesusruiz.washingagenda.viewModel.RegisterViewModel
import com.jesusruiz.washingagenda.views.HomeView
import com.jesusruiz.washingagenda.views.LoginView
import com.jesusruiz.washingagenda.views.admin.AddUserView
import com.jesusruiz.washingagenda.views.admin.AdminPanelView


sealed class Screen(val route: String)
{
    data object Login : Screen("Login")
    data object Admin : Screen("Admin")
    data object Home : Screen("Home")
    data object Calendar : Screen("Calendar/{userId}")
    data object AddUser: Screen("AddUser")

}

@Composable
fun NavManager()
{
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "Login"){
        composable(Screen.Login.route) {
            val loginVM: LoginViewModel = hiltViewModel()
            LoginView(navController, loginVM)
        }
        composable(Screen.Home.route) {
            HomeView(navController)
        }
        composable(Screen.Admin.route) {
            val adminViewModel: AdminViewModel = hiltViewModel()
            AdminPanelView(navController, adminViewModel)
        }
        composable(Screen.AddUser.route) {
            val registerViewModel: RegisterViewModel = hiltViewModel()
            AddUserView(navController, registerViewModel)
        }
    }
}