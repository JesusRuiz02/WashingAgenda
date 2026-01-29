package com.jesusruiz.washingagenda.schedule

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.jesusruiz.washingagenda.R
import com.jesusruiz.washingagenda.models.EventModel
import com.jesusruiz.washingagenda.toComposeColor
import com.jesusruiz.washingagenda.toDate
import com.jesusruiz.washingagenda.toHexString
import com.jesusruiz.washingagenda.toLocalDateTime
import com.jesusruiz.washingagenda.ui.theme.WashingAgendaTheme

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

val EventTimeFormatter = DateTimeFormatter.ofPattern("hh:mm a")
@Composable
fun BasicEvent(event: EventModel,
               modifier: Modifier = Modifier,
               onClick: (EventModel) -> Unit = {}){
    Column(
        modifier = modifier
            .clickable(onClick = { onClick(event) })
            .fillMaxSize()
            .padding(end = 10.dp, bottom = 2.dp)
            .background(event.color, shape = RoundedCornerShape(12.dp))
            .padding(10.dp)
    ) {
        Text(
            text = "${event.startDate!!.format(EventTimeFormatter)} - ${
                event.endDate!!.format(
                    EventTimeFormatter
                )
            }",
            style = MaterialTheme.typography.labelMedium
        )
        Text(
            text = stringResource(R.string.department_txt) + event.departmentN,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold
        )
    }
}

val sampleEvents = listOf(
    EventModel(
        startDate = LocalDateTime.parse("2026-01-02T11:00:00"),
        endDate = LocalDateTime.parse("2026-01-02T12:00:00"),
        color = Color(0xFFAFBBF2),
        departmentN = "10"
    ),
    EventModel(
        startDate = LocalDateTime.parse("2026-01-03T12:00:00"),
        endDate = LocalDateTime.parse("2026-01-03T12:00:00"),
        color = Color(0xFFAFBBF2),
        departmentN = "8"
    ),
    EventModel(
        startDate = LocalDateTime.parse("2026-01-02T13:00:00"),
        endDate = LocalDateTime.parse("2026-01-02T14:00:00"),
        color = Color(0xFFAFBBF2),
        departmentN = "10"
    )
)

class EventsProvider : PreviewParameterProvider<EventModel> {
    override val values = sampleEvents.asSequence()
}

@Preview(showBackground = true)
@Composable
fun EventPreview(
    @PreviewParameter(EventsProvider::class) event: EventModel,
) {
    WashingAgendaTheme {
        BasicEvent(event,
            modifier = Modifier.sizeIn(maxHeight = 64.dp),
            onClick = {clickedEvent ->
                println("Event clicked: $clickedEvent")
            })
    }
}
