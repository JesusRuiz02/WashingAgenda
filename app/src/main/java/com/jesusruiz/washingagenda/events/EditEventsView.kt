package com.jesusruiz.washingagenda.events

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.jesusruiz.washingagenda.datePicker.FullDatePicker
import com.jesusruiz.washingagenda.models.EventModel
import com.jesusruiz.washingagenda.viewModel.HomeInputAction
import com.jesusruiz.washingagenda.viewModel.HomeViewModel
import com.jesusruiz.washingagenda.withOutSeconds
import kotlinx.coroutines.launch
import java.time.LocalDateTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditEventsView(homeViewModel: HomeViewModel, navController: NavController, eventId: String){
    val state by homeViewModel.homeState
    val event: EventModel = state.events.find { it.id == eventId }!!
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    LaunchedEffect(Unit) {

        homeViewModel.onAction(HomeInputAction.IsStartDateEventChanged(event.startDate!!))
        homeViewModel.onAction(HomeInputAction.IsEndDateEventChanged(event.endDate!!))
    }

    LaunchedEffect(state.errorMessage) {
        val message = state.errorMessage
        if(!message.isNullOrEmpty()){
            scope.launch {
                snackbarHostState.showSnackbar(message)
                homeViewModel.onErrorMessageShown()
            }

        }

    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
        CenterAlignedTopAppBar(
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            title = {
                Text(text ="Editar Evento", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.secondary)
            },
            navigationIcon = {
                TextButton (onClick = {
                    homeViewModel.onAction(HomeInputAction.ClearDatesPicker)
                    homeViewModel.onAction(HomeInputAction.CancelEditingEvent)
                    navController.popBackStack()})
                {
                    Text(text = "Cancelar", color = MaterialTheme.colorScheme.secondary)                }
            },
            actions = {
                TextButton(onClick = {
                    homeViewModel.editEvent(onSuccess = {
                        homeViewModel.onAction(HomeInputAction.ClearDatesPicker)
                        homeViewModel.onAction(HomeInputAction.CancelEditingEvent)
                        navController.popBackStack()
                    })
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
            .fillMaxSize(),
            ) {
            Card (modifier = Modifier

                .align(alignment = Alignment.CenterHorizontally)
                .background(color = MaterialTheme.colorScheme.tertiary)
                .fillMaxWidth()
                .height(300.dp),
                colors = CardDefaults.cardColors(MaterialTheme.colorScheme.tertiary)
            ){
                Column(modifier = Modifier
                    .fillMaxHeight()
                    ,
                    horizontalAlignment = Alignment.Start) {
                    Row(modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                        verticalAlignment = Alignment.CenterVertically) {
                        FullDatePicker(modifier = Modifier.weight(1f),
                            state.eventStart.toLocalDate(),
                            state.eventStart.toLocalTime().withOutSeconds(),
                            startText = "Inicia",
                            onDateChanged = { newDate ->
                                val newDateTime = LocalDateTime.of(newDate, state.eventStart.toLocalTime())
                                homeViewModel.onAction(HomeInputAction.IsStartDateEventChanged(newDateTime))
                            },
                            onHourChanged = {
                                newHour ->
                                val newDateTime = LocalDateTime.of(state.eventStart.toLocalDate(), newHour)
                                homeViewModel.onAction(HomeInputAction.IsStartDateEventChanged(newDateTime))
                            })
                    }
                    Row(modifier = Modifier
                        .padding(top = 10.dp)
                        .fillMaxWidth()
                        .weight(1f),
                        verticalAlignment = Alignment.CenterVertically) {
                        FullDatePicker(modifier = Modifier.weight(1f),
                            state.eventEnd.toLocalDate(),
                            state.eventEnd.toLocalTime().withOutSeconds(),
                            startText = "Termina",
                            onDateChanged = { newDate ->
                                val newDateTime = LocalDateTime.of(newDate, state.eventEnd.toLocalTime())
                                homeViewModel.onAction(HomeInputAction.IsEndDateEventChanged(newDateTime))
                            },
                            onHourChanged = {
                                    newHour ->
                                val newDateTime = LocalDateTime.of(state.eventEnd.toLocalDate(), newHour)
                                homeViewModel.onAction(HomeInputAction.IsEndDateEventChanged(newDateTime))
                            }
                        )

                    }
                    Text(modifier = Modifier.padding(start = 20.dp, top = 20.dp),text = "Horas restantes: ",style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.secondary)
                    Button (modifier = Modifier
                        .padding(start = 20.dp, top = 20.dp, end =20.dp, bottom = 20.dp)
                        .align(Alignment.CenterHorizontally),onClick = { homeViewModel.deleteEvent {
                        homeViewModel.onAction(HomeInputAction.CancelEditingEvent)
                    } },
                        colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary))
                    {
                        Text(modifier = Modifier.padding(start = 20.dp)

                            ,text = "Eliminar evento",style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.tertiary)
                    }


                }


            }
        }

    }
}