package com.jesusruiz.washingagenda.schedule

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.jesusruiz.washingagenda.ui.theme.WashingAgendaTheme
import java.time.LocalTime

@Composable
fun ScheduleSideBar(
    hourHeight: Dp,
    modifier: Modifier = Modifier,
    label: @Composable (time: LocalTime) -> Unit = {BasicSliderLabel(time = it)}
)
{
    Column(modifier = modifier) {
        val startTime = LocalTime.MIN
        repeat(24){
            i ->
            Box(modifier = Modifier.height(hourHeight)){
                label(startTime.plusHours(i.toLong()))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ScheduleSideBarPreview()
{
    WashingAgendaTheme {
        ScheduleSideBar(hourHeight = 64.dp)
    }
}