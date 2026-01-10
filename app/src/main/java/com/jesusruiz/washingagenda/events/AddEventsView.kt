package com.jesusruiz.washingagenda.events

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TimeInput
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.jesusruiz.washingagenda.R
import com.jesusruiz.washingagenda.viewModel.HomeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEventsView(navController: NavController, homeViewModel: HomeViewModel){
    val state = homeViewModel.homeState
    val timepickerState = rememberTimePickerState(
        initialHour = state.startPickerHour,
        initialMinute = state.startPickerMinute,
        is24Hour = true
    )
    LaunchedEffect(timepickerState.hour, timepickerState.minute) {
        homeViewModel.onAction(HomeViewModel.HomeInputAction.IsStartTimeChanged(
            timepickerState.hour,
            timepickerState.minute))
    }
    Scaffold(topBar = {
        TopAppBar(title = {
            Text(text ="Nuevo Evento" )},
            navigationIcon = {
                IconButton(onClick = {
                    homeViewModel.onAction(HomeViewModel.HomeInputAction.IsAddingEventChange(!state.isAddingEvent))}){
                    Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                }
            })
    })
    {
        paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {
            Box(modifier = Modifier
                .background(color = colorResource(R.color.dark_green))
                .fillMaxWidth()
                .height(150.dp)

            ){
                Text(modifier = Modifier.padding(top = 10.dp),
                    text = "Horas restantes: 5",
                    color = Color.White,
                    style = MaterialTheme.typography.titleMedium)
                TimeInput(modifier = Modifier
                    .align(Alignment.Center)
                    .padding(top = 40.dp)
                    ,state = timepickerState)
            }
        }

    }
}
