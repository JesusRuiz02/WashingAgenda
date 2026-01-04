package com.jesusruiz.washingagenda.schedule

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.jesusruiz.washingagenda.ui.theme.WashingAgendaTheme

import java.time.LocalDate
import java.time.temporal.ChronoUnit

@Composable
fun ScheduleHeader(
    minDate: LocalDate,
    maxDate: LocalDate,
    dayWidth: Dp,
    modifier: Modifier = Modifier,
    dayHeader: @Composable (day: LocalDate) -> Unit = {BasicDayHeader(day = it )}
    ){
    Row(modifier = modifier) {
        val numDays = ChronoUnit.DAYS.between(minDate,maxDate).toInt() + 1
        repeat(numDays) { i -> Box(modifier = Modifier.width(dayWidth)){
                dayHeader(minDate.plusDays(i.toLong()))
             }
        }
    }

}

@Preview(showBackground = true)
@Composable
fun ScheduleHeaderPreview(){
    WashingAgendaTheme {
        ScheduleHeader(
            minDate = LocalDate.now(),
            maxDate = LocalDate.now().plusDays(5),
            dayWidth = 256.dp
        )
    }
}