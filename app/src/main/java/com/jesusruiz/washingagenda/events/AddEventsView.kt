package com.jesusruiz.washingagenda.events
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
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
import com.jesusruiz.washingagenda.viewModel.HomeInputAction
import com.jesusruiz.washingagenda.viewModel.HomeViewModel
import com.jesusruiz.washingagenda.withOutSeconds
import kotlinx.coroutines.launch
import java.time.LocalDateTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEventsView(navController: NavController, homeViewModel: HomeViewModel){
    val state by homeViewModel.homeState
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    LaunchedEffect(state.errorMessage) {
        val message = state.errorMessage
        if(!state.errorMessage.isNullOrEmpty()){
            scope.launch {
                snackbarHostState.showSnackbar(message!!)
                homeViewModel.onErrorMessageShown()
            }

        }

    }

    Scaffold(
        snackbarHost = { androidx.compose.material3.SnackbarHost(hostState = snackbarHostState) },
        topBar = {
        CenterAlignedTopAppBar(
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            title = {
            Text(text = stringResource(R.string.new_event_txt), fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.secondary)
                    },
            navigationIcon = {
                TextButton (onClick = {
                    homeViewModel.onAction(HomeInputAction.ClearDatesPicker)
                    homeViewModel.onAction(HomeInputAction.IsAddingEventChange(!state.isAddingEvent))
                    navController.popBackStack()
                })                {
                    Text(text = stringResource(R.string.cancel_txt), color = MaterialTheme.colorScheme.secondary)
                }
            },
            actions = {
                TextButton(onClick = {
                    homeViewModel.addEvent(onSuccess = {
                        homeViewModel.onAction(HomeInputAction.ClearDatesPicker)
                        homeViewModel.onAction(HomeInputAction.IsAddingEventChange(!state.isAddingEvent))
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
            Card(modifier = Modifier
                .align(alignment = Alignment.CenterHorizontally)
                .fillMaxWidth(),
                colors = CardDefaults.cardColors(MaterialTheme.colorScheme.background)
            ){
                Column(modifier = Modifier.padding(vertical = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    horizontalAlignment = Alignment.Start) {
                        FullDatePicker(
                            startText = stringResource(R.string.inicia_txt),
                            date = state.eventStart.toLocalDate(),
                            hour = state.eventStart.toLocalTime().withOutSeconds(),
                            onDateChanged = { newDate ->
                                val newDateTime = LocalDateTime.of(newDate, state.eventStart.toLocalTime())
                                homeViewModel.onAction(HomeInputAction.IsStartDateEventChanged(newDateTime))
                            },
                            onHourChanged = { newTime ->
                                val newDateTime = LocalDateTime.of(state.eventStart.toLocalDate(), newTime)
                                homeViewModel.onAction(HomeInputAction.IsStartDateEventChanged(newDateTime))
                            },
                            initialDatePickerStatus = DatePickerStatus.Hour
                        )
                        FullDatePicker(
                            startText = stringResource(R.string.end_txt),
                            date = state.eventEnd.toLocalDate(),
                            hour = state.eventEnd.toLocalTime().withOutSeconds(),
                            onDateChanged = { newDate ->
                                val newDateTime = LocalDateTime.of(newDate, state.eventEnd.toLocalTime())
                                homeViewModel.onAction(HomeInputAction.IsEndDateEventChanged(newDateTime))
                            },
                            onHourChanged = { newTime ->
                                val newDateTime = LocalDateTime.of(state.eventEnd.toLocalDate(), newTime)
                                homeViewModel.onAction(HomeInputAction.IsEndDateEventChanged(newDateTime))
                            }
                        )
                    Card(modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.surface),
                        shape = RoundedCornerShape(16.dp),
                        ) {
                        Text(
                            modifier = Modifier.padding(horizontal = 20.dp, vertical = 20.dp),
                            text = "${stringResource(R.string.remaining_hours_txt)}  ${state.user.hours} ",
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.tertiary,

                        )
                    }
                }


            }
        }

    }
}