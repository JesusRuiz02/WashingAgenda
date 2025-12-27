package com.jesusruiz.washingagenda.views.admin

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.jesusruiz.washingagenda.R
import com.jesusruiz.washingagenda.viewModel.AdminViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditUserView(idUser: String, navController : NavController, adminViewModel: AdminViewModel){
    val state = adminViewModel.adminState
    LaunchedEffect(Unit) {
        adminViewModel.getUserById(idUser)
    }
    Scaffold(topBar = {TopAppBar(title = {Text(text = "Editar usuario")},
        navigationIcon ={
            IconButton(onClick = {
                navController.popBackStack()
            }) {
                Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Go back")
            }
        })}
    ) { paddingValues ->
        var name by remember { mutableStateOf("") }
        var email by remember { mutableStateOf("") }
        var building by remember { mutableStateOf("") }
        var department by remember { mutableStateOf("") }
        var hours by remember { mutableStateOf("") }
        Column(Modifier.padding(paddingValues)) {
            OutlinedTextField(
                modifier = Modifier.padding(horizontal = 30.dp).fillMaxWidth(),
                value = state.editUser.name,
                onValueChange = { adminViewModel.onAction(AdminViewModel.AdminInputAction.NameChanged(it)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedContainerColor = colorResource(id = R.color.dark_green),
                    unfocusedContainerColor = colorResource(id = R.color.dark_green)
                )
            )
            OutlinedTextField(
                modifier = Modifier.padding(horizontal = 30.dp).fillMaxWidth(),
                value = state.editUser.hours.toString(),
                onValueChange = {
                    val value = it.toIntOrNull() ?: 0
                    adminViewModel.onAction(AdminViewModel.AdminInputAction.HoursChanged(value)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedContainerColor = colorResource(id = R.color.dark_green),
                    unfocusedContainerColor = colorResource(id = R.color.dark_green)
                )
            )
            OutlinedTextField(
                modifier = Modifier.padding(horizontal = 30.dp).fillMaxWidth(),
                value = state.editUser.building,
                onValueChange = { adminViewModel.onAction(AdminViewModel.AdminInputAction.BuildingChanged(it)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedContainerColor = colorResource(id = R.color.dark_green),
                    unfocusedContainerColor = colorResource(id = R.color.dark_green)
                )
            )
            OutlinedTextField(
                modifier = Modifier.padding(horizontal = 30.dp).fillMaxWidth(),
                value = state.editUser.departmentN,
                onValueChange = { adminViewModel.onAction(AdminViewModel.AdminInputAction.DepartmentChanged(it)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedContainerColor = colorResource(id = R.color.dark_green),
                    unfocusedContainerColor = colorResource(id = R.color.dark_green)
                )
            )

            Button(onClick = { /*TODO*/ },
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.padding(horizontal = 30.dp).fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.dark_green))) {
                Text(text = "Reiniciar contraseña", color = Color.White)
            }
            Button(onClick = { /*TODO*/ },
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.padding(horizontal = 30.dp).fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.dark_green)))
            {
                Text(text = "Guardar cambios", color = Color.White)
            }




        }
    }
}