package com.jesusruiz.washingagenda.views



import Schedule
import com.jesusruiz.washingagenda.events.EditEventsView
import androidx.compose.runtime.getValue
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.shrinkOut
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.jesusruiz.washingagenda.events.AddEventsView
import com.jesusruiz.washingagenda.R
import com.jesusruiz.washingagenda.navigation.Screen
import com.jesusruiz.washingagenda.viewModel.HomeInputAction
import com.jesusruiz.washingagenda.viewModel.HomeViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeView(navController: NavController, homeViewModel: HomeViewModel )
{
    LaunchedEffect(Unit) {
        homeViewModel.onAction(HomeInputAction.ClearDatesPicker)
    }
    val state by homeViewModel.homeState


    Scaffold(

        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface),
                title = { Text("Agenda", color = MaterialTheme.colorScheme.secondary)},
                navigationIcon = {
                    IconButton(onClick = { homeViewModel.signOut()
                        navController.navigate(Screen.Login.route) {
                            popUpTo(navController.graph.startDestinationId) {
                                inclusive = true
                            }
                            launchSingleTop = true
                        }
                    }){
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.secondary)
                    }
                }
            )
        }
    ) { paddingValues ->
        if (state.isLoading) {
            Box(
                Modifier.fillMaxSize()
                    .padding(paddingValues)
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        } else {
                Box(
                    modifier = Modifier.fillMaxSize().padding(paddingValues)
                ) {
                    Schedule(
                        hourHeight = 80.dp,
                        events = state.events,
                        modifier = Modifier
                            .fillMaxSize(),
                        pastDaysPreview = 2,
                        onEventClick = { clickedEvent ->
                            homeViewModel.onAction(HomeInputAction.EditingEventsChanged(clickedEvent))
                            homeViewModel.enterEditingEvent(onSuccess = {
                                navController.navigate(Screen.EditEvent.createRoute(clickedEvent.id))
                            })

                        }
                    )
                    IconButton(
                        modifier = Modifier.align(Alignment.BottomEnd)
                            .padding(end = 20.dp, bottom = 20.dp)
                            .size(75.dp),
                        onClick = {
                            homeViewModel.onAction(HomeInputAction.IsAddingEventChange(!state.isAddingEvent))
                            navController.navigate(Screen.AddEvent.route)
                        }
                    ) {
                        Icon(
                            modifier = Modifier
                                .size(75.dp),
                            painter = painterResource(R.drawable.ic_add_fillded),
                            contentDescription = "Add event",

                            )
                    }
                }
            }
    }
}

