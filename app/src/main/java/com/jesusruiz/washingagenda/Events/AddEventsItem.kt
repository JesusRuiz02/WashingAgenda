package com.jesusruiz.washingagenda.Events

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEventsView(navController: NavController){
    Scaffold(topBar = {
        TopAppBar(title = {
            Text(text ="Nuevo Evento" )},
            navigationIcon = {
                IconButton(onClick = {
                    navController.popBackStack()}){
                    Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                }
            })
    })
    {
        paddingValues ->
    }
}
