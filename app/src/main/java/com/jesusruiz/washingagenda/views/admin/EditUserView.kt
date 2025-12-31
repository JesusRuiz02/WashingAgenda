package com.jesusruiz.washingagenda.views.admin

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
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
    Scaffold(topBar = {TopAppBar(title = {Text(text = "Editar usuario", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold) },
        navigationIcon ={
            IconButton(onClick = {
                navController.popBackStack()
            }) {
                Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Go back")
            }
        })}
    ) { paddingValues ->
        Card(modifier = Modifier.padding().fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            elevation = CardDefaults.cardElevation(6.dp)){
            Column(Modifier.padding(paddingValues)) {
                OutlinedTextField(
                    modifier = Modifier.padding(horizontal = 30.dp).fillMaxWidth(),
                    value = state.editUser.name,
                    label = { Text("Nombre") },
                    onValueChange = { adminViewModel.onAction(AdminViewModel.AdminInputAction.NameChanged(it)) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.secondary,
                        focusedContainerColor = MaterialTheme.colorScheme.surface,
                        unfocusedContainerColor = MaterialTheme.colorScheme.surface
                    )
                )
                OutlinedTextField(
                    modifier = Modifier.padding(horizontal = 30.dp).fillMaxWidth(),
                    value = state.editUser.hours.toString(),
                    label = { Text("Horas restantes") },
                    onValueChange = {
                        val value = it.toIntOrNull() ?: 0
                        adminViewModel.onAction(AdminViewModel.AdminInputAction.HoursChanged(value)) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.secondary,
                        focusedContainerColor = MaterialTheme.colorScheme.surface,
                        unfocusedContainerColor = MaterialTheme.colorScheme.surface
                    )
                )
                //Boton dropdown menu
                var expanded by remember { mutableStateOf(false) }
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded }
                ) {
                    OutlinedTextField(
                        label = { Text("Edificio") },
                        modifier = Modifier.padding(horizontal = 30.dp).fillMaxWidth().menuAnchor(),
                        value = state.adminBuildings[state.editUser.building].orEmpty(),
                        onValueChange = { },
                        readOnly = true,
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.secondary,
                            focusedContainerColor = MaterialTheme.colorScheme.surface,
                            unfocusedContainerColor = MaterialTheme.colorScheme.surface
                        )
                    )
                    ExposedDropdownMenu(expanded = expanded,
                        onDismissRequest = { expanded = false }) {
                        state.adminBuildings.forEach{
                                (id, name) ->
                            DropdownMenuItem(text = { Text(text = name) },
                                onClick = {
                                    adminViewModel.onAction(AdminViewModel.AdminInputAction.BuildingChanged(id))
                                    expanded = false
                                }
                            )
                        }
                    }
                }
                OutlinedTextField(
                    label = { Text("Número de departamento") },
                    modifier = Modifier.padding(horizontal = 30.dp).fillMaxWidth(),
                    value = state.editUser.departmentN,
                    onValueChange = { adminViewModel.onAction(AdminViewModel.AdminInputAction.DepartmentChanged(it)) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.secondary,
                        focusedContainerColor = MaterialTheme.colorScheme.surface,
                        unfocusedContainerColor = MaterialTheme.colorScheme.surface
                    )
                )
                Button(onClick = { /*TODO*/ },
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.padding(horizontal = 30.dp, vertical = 5.dp).fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.dark_green))) {
                    Text(text = "Reiniciar contraseña", color = Color.White)
                }
                Button(onClick = { /*TODO*/ },
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.padding(horizontal = 30.dp, vertical = 5.dp).fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.dark_green)))
                {
                    Text(text = "Guardar cambios", color = Color.White)
                }

            }
        }

    }
}