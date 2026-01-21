package com.jesusruiz.washingagenda.events
import com.commandiron.wheel_picker_compose.core.WheelPickerDefaults
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import com.jesusruiz.washingagenda.datePicker.FullDatePicker
import com.jesusruiz.washingagenda.viewModel.HomeInputAction
import com.jesusruiz.washingagenda.viewModel.HomeViewModel
import com.jesusruiz.washingagenda.withOutSeconds
import java.time.LocalDateTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEventsView(navController: NavController, homeViewModel: HomeViewModel){
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
                    homeViewModel.onAction(HomeInputAction.IsAddingEventChange(!state.value.isAddingEvent))})                {
                    Text(text = "Cancelar", color = MaterialTheme.colorScheme.secondary)
                }
            },
            actions = {
                TextButton(onClick = {
                   homeViewModel.addEvent(){
                       homeViewModel.onAction(HomeInputAction.IsAddingEventChange(!state.value.isAddingEvent))
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
        Column(modifier = Modifier.padding(paddingValues)
            .fillMaxSize()) {
            Card(modifier = Modifier
                .align(alignment = Alignment.CenterHorizontally)
                .fillMaxWidth()
                .height(300.dp),
                colors = CardDefaults.cardColors(MaterialTheme.colorScheme.tertiary)

            ){
                Column(modifier = Modifier.fillMaxHeight(),
                    horizontalAlignment = Alignment.Start) {
                    Row(modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                        verticalAlignment = Alignment.CenterVertically) {
                        FullDatePicker(
                            modifier = Modifier.weight(1f),
                            startText = "Inicia",
                            date = state.value.eventStart.toLocalDate(),
                            hour = state.value.eventStart.toLocalTime().withOutSeconds(),
                            onDateChanged = { newDate ->
                                val newDateTime = LocalDateTime.of(newDate, state.value.eventStart.toLocalTime())
                                homeViewModel.onAction(HomeInputAction.IsStartDateEventChanged(newDateTime))
                            },
                            onHourChanged = { newTime ->
                                val newDateTime = LocalDateTime.of(state.value.eventStart.toLocalDate(), newTime)
                                homeViewModel.onAction(HomeInputAction.IsStartDateEventChanged(newDateTime))
                            }
                        )
                        
                    }
                    Row(modifier = Modifier
                        .padding(top = 10.dp)
                        .fillMaxWidth()
                        .weight(1f),
                        verticalAlignment = Alignment.CenterVertically) {
                        FullDatePicker(
                            modifier = Modifier.weight(1f),
                            startText = "Termina",
                            date = state.value.eventEnd.toLocalDate(),
                            hour = state.value.eventEnd.toLocalTime().withOutSeconds(),
                            onDateChanged = { newDate ->
                                val newDateTime = LocalDateTime.of(newDate, state.value.eventEnd.toLocalTime())
                                homeViewModel.onAction(HomeInputAction.IsEndDateEventChanged(newDateTime))
                            },
                            onHourChanged = { newTime ->
                                val newDateTime = LocalDateTime.of(state.value.eventEnd.toLocalDate(), newTime)
                                homeViewModel.onAction(HomeInputAction.IsEndDateEventChanged(newDateTime))
                            }
                        )

                    }
                    Text(modifier = Modifier.padding(start = 20.dp),text = "Horas restantes: ",style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.secondary)



                }


            }
        }

    }
}