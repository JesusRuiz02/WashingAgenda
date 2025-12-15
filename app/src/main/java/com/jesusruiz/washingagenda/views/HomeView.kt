package com.jesusruiz.washingagenda.views


import kotlin.time.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import com.himanshoe.kalendar.Kalendar
import com.himanshoe.kalendar.KalendarEvents
import com.himanshoe.kalendar.KalendarType
import com.himanshoe.kalendar.foundation.action.OnDaySelectionAction
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.todayIn

@Composable
fun HomeView(navController: NavController)
{
    /**Column (modifier = Modifier.wrapContentSize().background(Color.LightGray)) {
        Kalendar(
            currentDay = Clock.System.todayIn(TimeZone.currentSystemDefault()),
            modifier = Modifier.fillMaxWidth(),
            events = KalendarEvents(),
            showLabel = true,
            kalendarType = KalendarType.Oceanic,
            onDayClick = { date , events ->
                println("Day selected: $date")

            },
        )
    }**/
}
