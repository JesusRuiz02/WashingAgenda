package com.jesusruiz.washingagenda.schedule

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jesusruiz.washingagenda.ui.theme.WashingAgendaTheme
import java.time.LocalTime
import java.time.format.DateTimeFormatter

private val HourFormatter = DateTimeFormatter.ofPattern("h a")
@Composable
fun BasicSliderLabel(
    time: LocalTime,
    modifier: Modifier = Modifier,
){
    Text(
        text =  time.format(HourFormatter),
        color = MaterialTheme.colorScheme.secondary,
        modifier = modifier
            .fillMaxWidth()
            .padding(4.dp)
    )

}

@Preview(showBackground = true)
@Composable
fun BasicSliderLabelPreview(){
    WashingAgendaTheme {
        BasicSliderLabel(time = LocalTime.NOON, Modifier.sizeIn(maxHeight = 64.dp))
    }
}