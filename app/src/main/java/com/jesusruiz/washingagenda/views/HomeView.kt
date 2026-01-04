package com.jesusruiz.washingagenda.views



import Schedule
import android.util.Log
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import com.jesusruiz.washingagenda.models.EventModel
import java.time.LocalDateTime


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeView(navController: NavController, )
{
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
            startDate = LocalDateTime.of(2026, 1, 2, 13, 0),
            endDate = LocalDateTime.of(2026, 1, 2, 14, 0),
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
                    .fillMaxSize()
            )
        }
    }
}

