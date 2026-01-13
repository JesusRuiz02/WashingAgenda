package com.jesusruiz.washingagenda.views



import Schedule
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.jesusruiz.washingagenda.events.AddEventsView
import com.jesusruiz.washingagenda.R
import com.jesusruiz.washingagenda.models.EventModel
import com.jesusruiz.washingagenda.viewModel.HomeViewModel
import java.time.LocalDateTime


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeView(navController: NavController, homeViewModel: HomeViewModel )
{
    val state = homeViewModel.homeState
    val events = listOf(
        EventModel(
            id = "1",
            userID = "u1",
            building = "b1",
            startDate = LocalDateTime.of(2026, 1, 2, 10, 0),
            endDate = LocalDateTime.of(2026, 1, 2, 12, 0),
            color = Color(0xFFAFBBF2),
            departmentN = "10"
        ),
        EventModel(
            id = "2",
            userID = "u2",
            building = "b1",
            startDate = LocalDateTime.of(2026, 1, 13, 10, 0),
            endDate = LocalDateTime.of(2026, 1, 13, 14, 0),
            departmentN = "8",
            color = Color(0xFFAFBBF2),
        ),
        EventModel(
            id = "4",
            userID = "u211",
            building = "b1",
            startDate = LocalDateTime.of(2026, 1, 3, 12, 0),
            endDate = LocalDateTime.of(2026, 1, 3, 14, 0),
            departmentN = "8",
            color = Color(0xFFAFBBF2),
        )

    )

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface),
                title = { Text("Agenda", color = MaterialTheme.colorScheme.secondary)},
                navigationIcon = {
                    IconButton(onClick = { homeViewModel.signOut()
                        navController.popBackStack()}){
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.secondary)
                    }
                }
            )
        }
    ) {
        paddingValues ->

        Box(
            modifier = Modifier.fillMaxSize().padding(paddingValues)
        ){
            Schedule(
                hourHeight = 80.dp,
                events = events,
                modifier = Modifier
                    .fillMaxSize(),
                pastDaysPreview = 6
            )
            IconButton(
                modifier = Modifier.align(Alignment.BottomEnd)
                    .padding(end = 20.dp, bottom = 20.dp)
                    .size(75.dp),
                onClick = {
                    homeViewModel.onAction(HomeViewModel.HomeInputAction.IsAddingEventChange(!state.isAddingEvent))
                }
            ) {
                Icon(
                    modifier = Modifier
                        .size(75.dp),
                    painter = painterResource(R.drawable.ic_add_fillded),
                    contentDescription = "Add event",

                )
            }
            AnimatedVisibility(visible = state.isAddingEvent,
                modifier = Modifier.align(Alignment.BottomCenter),
                enter = slideInVertically(
                    tween(300),
                    initialOffsetY = {fullHeight -> fullHeight}
                ),
                exit = shrinkOut(tween(500))) {
                AddEventsView(navController,homeViewModel)
            }
        }
    }
}

