package com.jesusruiz.washingagenda.events

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.commandiron.wheel_picker_compose.WheelDateTimePicker
import com.commandiron.wheel_picker_compose.core.TimeFormat
import com.commandiron.wheel_picker_compose.core.WheelPickerDefaults
import com.jesusruiz.washingagenda.models.EventModel
import com.jesusruiz.washingagenda.toLocalDateTime
import com.jesusruiz.washingagenda.viewModel.HomeInputAction
import com.jesusruiz.washingagenda.viewModel.HomeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditEventsView(homeViewModel: HomeViewModel, navController: NavController, event: EventModel){
    val state = homeViewModel.homeState
    Scaffold(topBar = {
        CenterAlignedTopAppBar(
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            title = {
                Text(text ="Nuevo Evento", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.secondary)
            },
            navigationIcon = {
                TextButton (onClick = {
                    homeViewModel.onAction(HomeInputAction.CancelEditingEvent)})
                {
                    Text(text = "Cancelar", color = MaterialTheme.colorScheme.secondary)                }
            },
            actions = {
                TextButton(onClick = {
                    homeViewModel.editEvent(){
                        homeViewModel.onAction(HomeInputAction.CancelEditingEvent)
                    }
                })
                {
                    Text(text = "Guardar", color = MaterialTheme.colorScheme.secondary)
                }
            }
        )
    })
    {
            paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {
            Box(modifier = Modifier
                .align(alignment = Alignment.CenterHorizontally)
                .background(color = MaterialTheme.colorScheme.tertiary)
                .fillMaxWidth()
                .height(300.dp)

            ){
                Column(modifier = Modifier.fillMaxHeight(),
                    horizontalAlignment = Alignment.Start) {
                    Row(modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                        verticalAlignment = Alignment.CenterVertically) {
                        Text(modifier = Modifier
                            .padding(start = 20.dp, end = 40.dp) ,
                            text= "Inicio",
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.secondary)
                        WheelDateTimePicker(startDateTime = event.startDate!!,
                            minDateTime = homeViewModel.currentTime,
                            maxDateTime = homeViewModel.maxTime,
                            timeFormat = TimeFormat.HOUR_24,
                            textColor = Color.DarkGray,
                            rowCount = 5,
                            size = DpSize(300.dp, 150.dp),
                            selectorProperties = WheelPickerDefaults.selectorProperties(
                                enabled = true,
                                shape = RoundedCornerShape(6.dp),
                                color = Color.White,
                            ),
                            onSnappedDateTime = {
                                    snappedDateTime ->  homeViewModel.onAction(HomeInputAction.IsStartDateEventChanged(snappedDateTime))
                            })
                    }
                    Row(modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                        verticalAlignment = Alignment.CenterVertically) {
                        Text(modifier = Modifier
                            .padding(start = 20.dp, end = 60.dp) ,
                            text= "Fin",
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.secondary)
                        WheelDateTimePicker(startDateTime = event.startDate!!,
                            minDateTime = homeViewModel.currentTime,
                            maxDateTime = homeViewModel.maxTime,
                            timeFormat = TimeFormat.HOUR_24,
                            textColor = Color.DarkGray,
                            rowCount = 5,
                            size = DpSize(300.dp, 150.dp),
                            selectorProperties = WheelPickerDefaults.selectorProperties(
                                enabled = true,
                                shape = RoundedCornerShape(6.dp),
                                color = Color.White,
                            ),
                            onSnappedDateTime = {
                                    snappedDateTime -> homeViewModel.onAction(HomeInputAction.IsEndDateEventChanged(snappedDateTime))
                            })

                    }
                    Text(modifier = Modifier.padding(start = 20.dp),text = "Horas restantes: ",style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.secondary)
                    TextButton(modifier = Modifier.padding(start = 20.dp),onClick = { /*TODO*/ }) {
                        Text(modifier = Modifier.padding(start = 20.dp),text = "Eliminar evento",style = MaterialTheme.typography.titleLarge)
                    }


                }


            }
        }

    }
}