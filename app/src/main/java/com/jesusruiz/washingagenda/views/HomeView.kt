package com.jesusruiz.washingagenda.views



import Schedule
import android.util.Log
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.jesusruiz.washingagenda.R
import com.jesusruiz.washingagenda.models.EventModel
import com.jesusruiz.washingagenda.viewModel.HomeViewModel
import com.jesusruiz.washingagenda.viewModel.LoginViewModel
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
            startDate = LocalDateTime.of(2026, 1, 3, 10, 0),
            endDate = LocalDateTime.of(2026, 1, 3, 14, 0),
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
            TopAppBar(
                title = { Text("Agenda")}
            )
        }
    ) {
        paddingValues ->
        Box(
            modifier = Modifier.fillMaxSize().padding(paddingValues)
        ){
            Schedule(
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
                    state.isAddingEvent = !state.isAddingEvent
                }
            ) {
                Icon(
                    modifier = Modifier.size(75.dp),
                    painter = painterResource(R.drawable.ic_add_fillded),
                    contentDescription = "Add event",

                )
            }
        }
    }
}

