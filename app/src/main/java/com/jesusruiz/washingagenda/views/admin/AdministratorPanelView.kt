package com.jesusruiz.washingagenda.views.admin


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.jesusruiz.washingagenda.items.UserCard
import com.jesusruiz.washingagenda.viewModel.AdminViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminPanelView(navController: NavController, adminViewModel: AdminViewModel){
    LaunchedEffect(Unit) {
        adminViewModel.getUsers()
    }
    Scaffold(topBar = {TopAppBar(title = {Text(text = "Admin Panel")},
        navigationIcon ={
            IconButton(onClick = {
                navController.popBackStack()
            }) {
                Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Go back")
            }
        })}
    ){ paddingValues ->
        Column(modifier = Modifier.padding(paddingValues), horizontalAlignment = Alignment.CenterHorizontally) {
            val users = adminViewModel.adminState.users
           LazyColumn() {
               items(users){
                   user ->
                   UserCard(name = user.name, department = user.departmentN, building = user.building, hours = user.hours)
                   {
                       //navController.navigate("EditUser/${user.userID}")
                   } }
           }
            Button(onClick = {
                navController.navigate("AddUser")
            }) {
                Text(text = "Agregar usuario")
            }
        }

    }
}