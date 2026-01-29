package com.jesusruiz.washingagenda.views.admin


import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.jesusruiz.washingagenda.R
import com.jesusruiz.washingagenda.items.UserCard
import com.jesusruiz.washingagenda.navigation.Screen
import com.jesusruiz.washingagenda.viewModel.AdminViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminPanelView(navController: NavController, adminViewModel: AdminViewModel){
    val state = adminViewModel.adminState
    LaunchedEffect(Unit) {
        adminViewModel.getUsers()
    }
    LaunchedEffect(state.users) {
        adminViewModel.gettingUsersByBuilding()
    }
    LaunchedEffect(state.buildingFilter) {
        adminViewModel.gettingUsersByBuilding()
    }


    Scaffold(topBar = {
        CenterAlignedTopAppBar(
            colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface),
            title = {Text(text = stringResource(R.string.admin_panel_txt),color = MaterialTheme.colorScheme.secondary, fontWeight = FontWeight.Bold)},
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
            })
    }
    ){ paddingValues ->
            if (state.isLoading)
            {
                Box(
                    Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
            else{
                Column(modifier = Modifier.padding(paddingValues), horizontalAlignment = Alignment.CenterHorizontally) {
                    var expanded by remember { mutableStateOf(false) }
                    ExposedDropdownMenuBox(
                        expanded = expanded,
                        onExpandedChange = { expanded = !expanded }
                    ) {
                        OutlinedTextField(
                            label = { stringResource(R.string.building_txt) },
                            shape = MaterialTheme.shapes.medium,
                            modifier = Modifier
                                .padding(horizontal = 30.dp, vertical = 10.dp)
                                .fillMaxWidth()
                                .menuAnchor(),
                            value = state.adminBuildings[state.buildingFilter].orEmpty(),
                            onValueChange = { },
                            readOnly = true,
                            trailingIcon = {
                                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                            },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = MaterialTheme.colorScheme.secondary,
                                unfocusedBorderColor = MaterialTheme.colorScheme.secondary,
                                focusedContainerColor = MaterialTheme.colorScheme.tertiary,
                                unfocusedContainerColor = MaterialTheme.colorScheme.tertiary,
                                focusedTextColor = MaterialTheme.colorScheme.primary,
                                unfocusedTextColor = MaterialTheme.colorScheme.primary,
                            )
                        )
                        ExposedDropdownMenu(expanded = expanded,
                            onDismissRequest = { expanded = false }) {
                            state.adminBuildings.forEach{
                                    (id, name) ->
                                DropdownMenuItem(text = { Text(text = name) },
                                    onClick = {
                                        adminViewModel.onAction(AdminViewModel.AdminInputAction.BuildingFilterChanged(id))
                                        Log.d("Edificio",state.buildingFilter )
                                        expanded = false
                                    }
                                )
                            }
                        }
                    }
                    val buildings = adminViewModel.adminState.adminBuildings
                    LazyColumn {
                        items(state.buildingUsers){
                                user ->
                            UserCard(name = user.name, department = user.departmentN, building = buildings[user.building]!!, hours = user.hours.toInt())
                            {
                                navController.navigate(Screen.Edit.createRoute(user.userID))
                            } }
                    }
                    Button(onClick = {
                        navController.navigate("AddUser") },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary),
                        modifier = Modifier.padding(top = 20.dp))
                    {
                        Text(text = "Agregar usuario",color = Color.White)
                    }
                }
            }


    }
}