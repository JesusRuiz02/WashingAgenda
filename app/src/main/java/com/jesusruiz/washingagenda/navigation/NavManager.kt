package com.jesusruiz.washingagenda.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.jesusruiz.washingagenda.login.CheckSessionView
import com.jesusruiz.washingagenda.viewModel.AdminViewModel
import com.jesusruiz.washingagenda.viewModel.LoginViewModel
import com.jesusruiz.washingagenda.viewModel.RegisterViewModel
import com.jesusruiz.washingagenda.views.HomeView
import com.jesusruiz.washingagenda.login.LoginView
import com.jesusruiz.washingagenda.viewModel.CheckSessionViewModel
import com.jesusruiz.washingagenda.views.admin.AddUserView
import com.jesusruiz.washingagenda.views.admin.AdminPanelView
import com.jesusruiz.washingagenda.views.admin.EditUserView
import dagger.hilt.android.lifecycle.HiltViewModel


sealed class Screen(val route: String)
{
    data object CheckSession: Screen("CheckSession")
    data object Login : Screen("Login")
    data object Admin : Screen("Admin")
    data object Home : Screen("Home")
    data object Edit: Screen("EditUser/{userId}")
    {
        fun createRoute(userId: String) = "EditUser/$userId"
    }
    data object AddUser: Screen("AddUser")

}

@Composable
fun NavManager()
{
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "CheckSession"){
        composable(Screen.Login.route) {
            val loginVM: LoginViewModel = hiltViewModel()
            LoginView(navController, loginVM)
        }
        composable(Screen.CheckSession.route) {
            val checkSessionViewModel: CheckSessionViewModel = hiltViewModel()
            CheckSessionView(navController, checkSessionViewModel)
        }
        composable(route = Screen.Edit.route,
            arguments = listOf(navArgument("userId") { type = NavType.StringType})
        ){ backStackEntry ->
            val userID = backStackEntry.arguments?.getString("userId")
            val adminViewModel: AdminViewModel = hiltViewModel()
           EditUserView(userID!!,navController, adminViewModel)

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