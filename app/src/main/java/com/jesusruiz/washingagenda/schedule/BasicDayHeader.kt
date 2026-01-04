package com.jesusruiz.washingagenda.schedule

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jesusruiz.washingagenda.ui.theme.WashingAgendaTheme
import java.time.LocalDate
import java.time.format.DateTimeFormatter

val DayFormatter = DateTimeFormatter.ofPattern("EEE, d MMM")
@Composable
fun BasicDayHeader(
    day: LocalDate,
    modifier: Modifier = Modifier,
){
    Text(text = day.format(DayFormatter),
        textAlign = TextAlign.Center,
        modifier = modifier
            .fillMaxWidth()
            .padding(4.dp))

}

@Preview(showBackground = true)
@Composable
fun BasicDayHeaderPreview(){
    WashingAgendaTheme {
        BasicDayHeader(day = LocalDate.now())
    }
}
