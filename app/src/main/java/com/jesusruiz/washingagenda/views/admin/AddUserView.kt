package com.jesusruiz.washingagenda.views.admin

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuItemColors
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
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
            Text(text = stringResource(R.string.add_tenant), color = MaterialTheme.colorScheme.secondary)},
        navigationIcon = {
            IconButton(onClick = {

                navController.popBackStack()}){
                Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = MaterialTheme.colorScheme.secondary)
            }
        })
    })
    { paddingValues ->
        Column(modifier = Modifier
            .padding(paddingValues)
            .fillMaxWidth()){
            var email by remember { mutableStateOf("") }
            var password by remember { mutableStateOf("") }
            var name by remember { mutableStateOf("") }
            var departmentN by remember { mutableStateOf("") }
            var building by remember { mutableStateOf("") }
            OutlinedTextField(modifier = Modifier
                .padding(horizontal = 30.dp, vertical = 10.dp)
                .fillMaxWidth(),
                shape = MaterialTheme.shapes.medium,
                value = name,
                label = { Text(text = stringResource(R.string.name_txt))},
                onValueChange = {name = it},
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text ),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = MaterialTheme.colorScheme.tertiary,
                    focusedLabelColor = MaterialTheme.colorScheme.tertiary,
                    unfocusedLabelColor = MaterialTheme.colorScheme.tertiary,
                    unfocusedTextColor = MaterialTheme.colorScheme.tertiary,
                    focusedBorderColor = MaterialTheme.colorScheme.secondary)
            )
            OutlinedTextField(modifier = Modifier
                .padding(horizontal = 30.dp, vertical = 10.dp)
                .fillMaxWidth(),
                shape = MaterialTheme.shapes.medium,
                value = email,
                onValueChange = {email = it},
                label = { Text(text = stringResource(R.string.email_txt))},
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email ),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = MaterialTheme.colorScheme.tertiary,
                    focusedLabelColor = MaterialTheme.colorScheme.tertiary,
                    unfocusedLabelColor = MaterialTheme.colorScheme.tertiary,
                    unfocusedTextColor = MaterialTheme.colorScheme.tertiary,
                    focusedBorderColor = MaterialTheme.colorScheme.secondary))
            OutlinedTextField(modifier = Modifier
                .padding(horizontal = 30.dp, vertical = 10.dp)
                .fillMaxWidth(),
                value = password,
                shape = MaterialTheme.shapes.medium,
                onValueChange = {password = it},
                label = { Text(text = stringResource(R.string.password_txt))},
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password ),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = MaterialTheme.colorScheme.tertiary,
                    focusedLabelColor = MaterialTheme.colorScheme.tertiary,
                    unfocusedLabelColor = MaterialTheme.colorScheme.tertiary,
                    unfocusedTextColor = MaterialTheme.colorScheme.tertiary,
                    focusedBorderColor = MaterialTheme.colorScheme.secondary)
            )
            //Boton dropdown menu
            var expanded by remember { mutableStateOf(false) }
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {
                OutlinedTextField(
                    label = { Text(stringResource(R.string.building_txt)) },
                    shape = MaterialTheme.shapes.medium,
                    modifier = Modifier
                        .padding(horizontal = 30.dp, vertical = 10.dp)
                        .fillMaxWidth()
                        .menuAnchor(),
                    value = state.adminBuildings[building].orEmpty(),
                    onValueChange = { },
                    readOnly = true,
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = MaterialTheme.colorScheme.tertiary,
                        focusedLabelColor = MaterialTheme.colorScheme.tertiary,
                        unfocusedLabelColor = MaterialTheme.colorScheme.tertiary,
                        unfocusedTextColor = MaterialTheme.colorScheme.tertiary,
                        focusedBorderColor = MaterialTheme.colorScheme.secondary)
                )
                ExposedDropdownMenu(expanded = expanded,
                    onDismissRequest = { expanded = false }) {
                    state.adminBuildings.forEach{
                            (id, name) ->
                        DropdownMenuItem(text = { Text(text = name) },
                            onClick = {
                                building = id
                                expanded = false
                            },

                            )

                    }
                }
            }
            OutlinedTextField(modifier = Modifier
                .padding(horizontal = 30.dp, vertical = 10.dp)
                .fillMaxWidth(),
                value = departmentN,
                shape = MaterialTheme.shapes.medium,
                onValueChange = {departmentN = it},
                label = { Text(text = stringResource(R.string.department_number_txt))},
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number ),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = MaterialTheme.colorScheme.tertiary,
                    focusedLabelColor = MaterialTheme.colorScheme.tertiary,
                    unfocusedLabelColor = MaterialTheme.colorScheme.tertiary,
                    unfocusedTextColor = MaterialTheme.colorScheme.tertiary,
                    focusedBorderColor = MaterialTheme.colorScheme.secondary)
            )
            Button(modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(horizontal = 30.dp, vertical = 10.dp),
                onClick = {
               adminViewModel.addUser(name,email,password,departmentN,building) { navController.popBackStack()}
            },
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)) {
                Text(text = stringResource(R.string.add_tenant), color = Color.White)
            }


        }


    }
}