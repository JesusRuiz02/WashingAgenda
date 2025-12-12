package com.jesusruiz.washingagenda.views.admin

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
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
import com.jesusruiz.washingagenda.viewModel.LoginViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddUserView(navController: NavController, loginVM: LoginViewModel)
{
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var departmentN by remember { mutableStateOf("") }
    var building by remember { mutableStateOf("") }
    val state = loginVM.uiState
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
            OutlinedTextField(modifier = Modifier.padding(horizontal = 30.dp).fillMaxWidth(),
                value = name,
                onValueChange = {name = it},
                label = { Text(text = "Nombre",
                    style = LocalTextStyle.current.copy(color = Color.White)
                )},
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text ),
                colors = OutlinedTextFieldDefaults.colors(focusedContainerColor = colorResource(id = R.color.dark_green),
                    unfocusedContainerColor = colorResource(id = R.color.dark_green))
            )
            OutlinedTextField(modifier = Modifier.padding(horizontal = 30.dp).fillMaxWidth(),
                value = email,
                onValueChange = {email = it},
                label = { Text(text = "Email",
                    style = LocalTextStyle.current.copy(color = Color.White)
                )},
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email ),
                colors = OutlinedTextFieldDefaults.colors(focusedContainerColor = colorResource(id = R.color.dark_green),
                    unfocusedContainerColor = colorResource(id = R.color.dark_green))
                )
            OutlinedTextField(modifier = Modifier.padding(horizontal = 30.dp).fillMaxWidth(),
                value = password,
                onValueChange = {password = it},
                label = { Text(text = "Contraseña",
                    style = LocalTextStyle.current.copy(color = Color.White)
                )},
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password ),
                colors = OutlinedTextFieldDefaults.colors(focusedContainerColor = colorResource(id = R.color.dark_green),
                    unfocusedContainerColor = colorResource(id = R.color.dark_green))
            )
            OutlinedTextField(modifier = Modifier.padding(horizontal = 30.dp).fillMaxWidth(),
                value = building,
                onValueChange = {building = it},
                label = { Text(text = "Edificio",
                    style = LocalTextStyle.current.copy(color = Color.White)
                )},
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text ),
                colors = OutlinedTextFieldDefaults.colors(focusedContainerColor = colorResource(id = R.color.dark_green),
                    unfocusedContainerColor = colorResource(id = R.color.dark_green))
            )
            OutlinedTextField(modifier = Modifier.padding(horizontal = 30.dp).fillMaxWidth(),
                value = building,
                onValueChange = {building = it},
                label = { Text(text = "Edificio",
                    style = LocalTextStyle.current.copy(color = Color.White)
                )},
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text ),
                colors = OutlinedTextFieldDefaults.colors(focusedContainerColor = colorResource(id = R.color.dark_green),
                    unfocusedContainerColor = colorResource(id = R.color.dark_green))
            )
            OutlinedTextField(modifier = Modifier.padding(horizontal = 30.dp).fillMaxWidth(),
                value = departmentN,
                onValueChange = {departmentN = it},
                label = { Text(text = "Número de Departamento",
                    style = LocalTextStyle.current.copy(color = Color.White)
                )},
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number ),
                colors = OutlinedTextFieldDefaults.colors(focusedContainerColor = colorResource(id = R.color.dark_green),
                    unfocusedContainerColor = colorResource(id = R.color.dark_green))
            )
            Button(onClick = {

            }) {
                Text(text = "Agregar inquilino")
            }


        }


    }
}