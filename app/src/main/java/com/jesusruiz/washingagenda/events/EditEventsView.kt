package com.jesusruiz.washingagenda.events

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.jesusruiz.washingagenda.R
import com.jesusruiz.washingagenda.datePicker.DatePickerStatus
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
    val event: EventModel = state.events.find { it.id == eventId}?: EventModel()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    LaunchedEffect(Unit) {
        homeViewModel.onAction(HomeInputAction.IsStartDateEventChanged(event.startDate!!))
        homeViewModel.onAction(HomeInputAction.IsEndDateEventChanged(event.endDate!!))
    }
    LaunchedEffect(homeViewModel.homeState.value.eventEnd,homeViewModel.homeState.value.eventStart ) {
        homeViewModel.getEditEventDifference()
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
                Text(text = stringResource(R.string.edit_event_txt), fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.secondary)
            },
            navigationIcon = {
                TextButton (onClick = {
                    homeViewModel.onAction(HomeInputAction.ClearDatesPicker)
                    homeViewModel.onAction(HomeInputAction.CancelEditingEvent)
                    homeViewModel.onAction(HomeInputAction.ClearPrevisualizationHour)
                    navController.popBackStack()})
                {
                    Text(text = stringResource(R.string.cancel_txt), color = MaterialTheme.colorScheme.secondary)                }
            },
            actions = {
                TextButton(onClick = {
                    homeViewModel.editEvent(onSuccess = {
                        homeViewModel.onAction(HomeInputAction.ClearDatesPicker)
                        homeViewModel.onAction(HomeInputAction.CancelEditingEvent)
                        homeViewModel.onAction(HomeInputAction.ClearPrevisualizationHour)
                        navController.popBackStack()
                    })
                })
                {
                    Text(text = stringResource(R.string.save_txt), color = MaterialTheme.colorScheme.secondary)
                }
            }
        )
    })
    {
            paddingValues ->
        Column(modifier = Modifier
            .padding(paddingValues)
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp, alignment = Alignment.Top),
            ) {
            Spacer(modifier = Modifier.height(16.dp))
            Card (modifier = Modifier
                .align(alignment = Alignment.CenterHorizontally)
                .fillMaxWidth(),
                colors = CardDefaults.cardColors(MaterialTheme.colorScheme.background),
                shape = RoundedCornerShape(16.dp)
            ){
                Column(modifier = Modifier
                    .padding(vertical = 24.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    horizontalAlignment = Alignment.Start) {
                        FullDatePicker(
                            modifier = Modifier,
                            state.eventStart.toLocalDate(),
                            state.eventStart.toLocalTime().withOutSeconds(),
                            startText = stringResource(R.string.inicia_txt),
                            onDateChanged = { newDate ->
                                val newDateTime =
                                    LocalDateTime.of(newDate, state.eventStart.toLocalTime())
                                homeViewModel.onAction(
                                    HomeInputAction.IsStartDateEventChanged(
                                        newDateTime
                                    )
                                )
                            },
                            onHourChanged = { newHour ->
                                val newDateTime =
                                    LocalDateTime.of(state.eventStart.toLocalDate(), newHour)
                                homeViewModel.onAction(
                                    HomeInputAction.IsStartDateEventChanged(
                                        newDateTime
                                    )
                                )
                            },
                            initialDatePickerStatus = DatePickerStatus.Hour
                        )
                    FullDatePicker(
                        modifier = Modifier,
                        state.eventEnd.toLocalDate(),
                        state.eventEnd.toLocalTime().withOutSeconds(),
                        startText = stringResource(R.string.end_txt),
                        onDateChanged = { newDate ->
                            val newDateTime =
                                LocalDateTime.of(newDate, state.eventEnd.toLocalTime())
                            homeViewModel.onAction(HomeInputAction.IsEndDateEventChanged(newDateTime))
                        },
                        onHourChanged = { newHour ->
                            val newDateTime =
                                LocalDateTime.of(state.eventEnd.toLocalDate(), newHour)
                            homeViewModel.onAction(HomeInputAction.IsEndDateEventChanged(newDateTime))
                        }
                    )
                    Text(modifier = Modifier.padding(all = 20.dp),text = "Horas restantes: ${state.previsualizationHour} ",style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.tertiary)
                }
                    Button (modifier = Modifier
                        .padding(start = 20.dp, top = 20.dp, end = 20.dp, bottom = 20.dp)
                        .align(Alignment.CenterHorizontally),onClick = { homeViewModel.deleteEvent {
                            navController.popBackStack()
                    } },
                        colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary))
                    {
                        Text(modifier = Modifier.padding(start = 20.dp)
                            ,text = stringResource(R.string.delete_event_txt),style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.tertiary)
                    }


            }
        }

    }
}