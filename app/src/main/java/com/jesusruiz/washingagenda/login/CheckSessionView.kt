package com.jesusruiz.washingagenda.login

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavController
import com.jesusruiz.washingagenda.viewModel.CheckSessionViewModel

@Composable
fun CheckSessionView(navController: NavController, checkSessionViewModel: CheckSessionViewModel)
{
    LaunchedEffect(Unit) {
        checkSessionViewModel.checkSession(
            isAdmin = { navController.navigate("Admin") },
            isUser = { navController.navigate("Admin")},
            notSessionInit = { navController.navigate("Login") })
    }
}