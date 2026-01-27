package com.jesusruiz.washingagenda.navigation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.jesusruiz.washingagenda.events.AddEventsView
import com.jesusruiz.washingagenda.events.EditEventsView
import com.jesusruiz.washingagenda.login.CheckSessionView
import com.jesusruiz.washingagenda.viewModel.AdminViewModel
import com.jesusruiz.washingagenda.viewModel.LoginViewModel
import com.jesusruiz.washingagenda.viewModel.RegisterViewModel
import com.jesusruiz.washingagenda.views.HomeView
import com.jesusruiz.washingagenda.login.LoginView
import com.jesusruiz.washingagenda.models.EventModel
import com.jesusruiz.washingagenda.viewModel.CheckSessionViewModel
import com.jesusruiz.washingagenda.viewModel.HomeViewModel
import com.jesusruiz.washingagenda.views.admin.AddUserView
import com.jesusruiz.washingagenda.views.admin.AdminPanelView
import com.jesusruiz.washingagenda.views.admin.EditUserView
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.LocalDateTime


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

    data object EditEvent: Screen("EditEvent/{eventId}")
    {
        fun createRoute(eventId: String) = "EditEvent/$eventId"
    }

    data object AddEvent: Screen("AddEvent")
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

        navigation(route = "schedule_graph", startDestination = Screen.Home.route){
            composable(Screen.Home.route) {  backstackEntry ->
                val parentEntry = remember(backstackEntry) {
                    navController.getBackStackEntry("schedule_graph")
                }
                val homeViewModel : HomeViewModel = hiltViewModel(parentEntry)
                HomeView(navController, homeViewModel)
            }
            composable(Screen.EditEvent.route){
                backStackEntry ->
                val eventId = backStackEntry.arguments?.getString("eventId")!!
                val parentEntry = remember(backStackEntry) {
                    navController.getBackStackEntry("schedule_graph")
                }
                val homeViewModel : HomeViewModel = hiltViewModel(parentEntry)
               // EditEventsView(homeViewModel, navController, homeViewModel )
            }
            composable(Screen.AddEvent.route) {
                 backStackEntry ->
                val parentEntry = remember(backStackEntry) {
                    navController.getBackStackEntry("schedule_graph")
                }
                val homeViewModel : HomeViewModel = hiltViewModel(parentEntry)
                AddEventsView(navController, homeViewModel)
            }

        }

        navigation(route = "admin_graph", startDestination = Screen.Admin.route)
        {
            composable(Screen.Admin.route) { backStackEntry ->
                val parentEntry = remember(backStackEntry) {
                    navController.getBackStackEntry("admin_graph")
                }
                val adminViewModel: AdminViewModel = hiltViewModel(parentEntry)
                AdminPanelView(navController, adminViewModel)
            }
            composable(Screen.AddUser.route) {
                backStackEntry ->
                val parentEntry = remember(backStackEntry){
                    navController.getBackStackEntry("admin_graph")
                }
                val adminViewModel: AdminViewModel = hiltViewModel(parentEntry)
                AddUserView(navController, adminViewModel)
            }
            composable(route = Screen.Edit.route,
                arguments = listOf(navArgument("userId") { type = NavType.StringType})
            ){ backStackEntry ->
                val userID = backStackEntry.arguments?.getString("userId")!!
                val parentEntry = remember(backStackEntry) {
                    navController.getBackStackEntry("admin_graph")
                }
                val adminViewModel: AdminViewModel = hiltViewModel(parentEntry)
                EditUserView(userID,navController, adminViewModel)
            }
        }
    }
}