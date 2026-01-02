package com.jesusruiz.washingagenda.views.admin

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
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
import com.jesusruiz.washingagenda.viewModel.RegisterViewModel
import kotlin.collections.component1
import kotlin.collections.component2
import kotlin.text.orEmpty

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddUserView(navController: NavController, adminViewModel: AdminViewModel)
{
    val state = adminViewModel.adminState
    Scaffold(topBar = {
        TopAppBar(title = {
            Text(text ="Agregar Inquilino" )},
        navigationIcon = {
            IconButton(onClick = {
                navController.popBackStack()}){
                Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
            }
        })
    })
    { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues).fillMaxWidth()){
            var email by remember { mutableStateOf("") }
            var password by remember { mutableStateOf("") }
            var name by remember { mutableStateOf("") }
            var departmentN by remember { mutableStateOf("") }
            var building by remember { mutableStateOf("") }
            OutlinedTextField(modifier = Modifier.padding(horizontal = 30.dp, vertical = 10.dp).fillMaxWidth(),
                value = name,
                onValueChange = {name = it},
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text ),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedContainerColor = colorResource(id = R.color.dark_green),
                    unfocusedContainerColor = colorResource(id = R.color.dark_green))
            )
            OutlinedTextField(modifier = Modifier.padding(horizontal = 30.dp, vertical = 10.dp).fillMaxWidth(),
                value = email,
                onValueChange = {email = it},
                label = { Text(text = "Email")},
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email ),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = colorResource(id = R.color.dark_green),
                    unfocusedContainerColor = colorResource(id = R.color.dark_green),
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White)
                )
            OutlinedTextField(modifier = Modifier.padding(horizontal = 30.dp, vertical = 10.dp).fillMaxWidth(),
                value = password,
                onValueChange = {password = it},
                label = { Text(text = "Contraseña")},
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password ),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = colorResource(id = R.color.dark_green),
                    unfocusedContainerColor = colorResource(id = R.color.dark_green),
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White)
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
                    value = state.adminBuildings[building].orEmpty(),
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
                                Log.d("probando", building)
                                building = id
                                expanded = false
                            }
                        )
                    }
                }
            }
            OutlinedTextField(modifier = Modifier.padding(horizontal = 30.dp, vertical = 10.dp).fillMaxWidth(),
                value = departmentN,
                onValueChange = {departmentN = it},
                label = { Text(text = "Número de Departamento")},
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number ),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = colorResource(id = R.color.dark_green),
                    unfocusedContainerColor = colorResource(id = R.color.dark_green),
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White)
            )
            Button(onClick = {
               adminViewModel.addUser(name,email,password,departmentN,building) { navController.popBackStack() }
            }) {
                Text(text = "Agregar inquilino")
            }


        }


    }
}