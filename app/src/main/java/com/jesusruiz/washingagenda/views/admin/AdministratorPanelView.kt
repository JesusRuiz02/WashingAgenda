package com.jesusruiz.washingagenda.views.admin


import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.navigation.NavController
import com.jesusruiz.washingagenda.R
import com.jesusruiz.washingagenda.items.UserCard
import com.jesusruiz.washingagenda.navigation.Screen
import com.jesusruiz.washingagenda.viewModel.AdminViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminPanelView(navController: NavController, adminViewModel: AdminViewModel){
    LaunchedEffect(Unit) {
        adminViewModel.getUsers()
    }
    val state = adminViewModel.adminState

    Scaffold(topBar = {TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface),
        title = {Text(text = "Admin Panel",color = MaterialTheme.colorScheme.secondary)},
        navigationIcon ={
            IconButton(onClick = {
                adminViewModel.logOut()
                navController.navigate(Screen.Login.route) {
                    popUpTo(navController.graph.startDestinationId) {
                        inclusive = true
                    }
                    launchSingleTop = true
                }
            }) {
                Icon(imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                    contentDescription = "Go back",
                    tint = MaterialTheme.colorScheme.secondary)
            }
        })}
    ){ paddingValues ->
            if (state.isLoading)
            {
                Box(
                    Modifier.fillMaxSize()
                        .padding(paddingValues)
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
            else{
                Column(modifier = Modifier.padding(paddingValues), horizontalAlignment = Alignment.CenterHorizontally) {
                    val users = adminViewModel.adminState.users
                    val buildings = adminViewModel.adminState.adminBuildings
                    LazyColumn() {
                        items(users){
                                user ->
                            UserCard(name = user.name, department = user.departmentN, building = buildings[user.building]!!, hours = user.hours)
                            {
                                navController.navigate(Screen.Edit.createRoute(user.userID))
                            } }
                    }
                    Button(onClick = {
                        navController.navigate("AddUser") },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary))
                    {
                        Text(text = "Agregar usuario",color = Color.White)
                    }
                }
            }


    }
}