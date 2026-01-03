package com.jesusruiz.washingagenda.items

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.ParentDataModifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import com.jesusruiz.washingagenda.models.EventModel
import com.jesusruiz.washingagenda.ui.theme.WashingAgendaTheme
import java.time.LocalDate
import java.time.LocalTime
import java.time.temporal.ChronoUnit
import kotlin.math.roundToInt


@Composable
fun Schedule(
    events: List<EventModel>,
    modifier: Modifier = Modifier,
    eventContent: @Composable (EventModel) -> Unit = { BasicEvent(event = it)},
    minDate: LocalDate = events.minByOrNull (EventModel::startDate)!!.startDate.toLocalDate(),
    maxDate: LocalDate = events.maxByOrNull(EventModel::endDate)!!.endDate.toLocalDate(),


) {
    val dayWidth = 256.dp
    val numDays = ChronoUnit.DAYS.between(minDate, maxDate).toInt() + 1
    val hourHeight = 64.dp
    Layout(content = {
        events.sortedBy(EventModel::startDate).forEach { event ->
            Box(modifier = Modifier.eventData(event)) {
                eventContent(event)
            }
        }
    }, modifier = Modifier
        .verticalScroll(rememberScrollState())
        .horizontalScroll(rememberScrollState())
    )
    { measurables, constraints ->
        val width = dayWidth.roundToPx() * numDays
        val height = hourHeight.roundToPx() * 24
        val placeablesWithEvents = measurables.map { measurable ->
            val event = measurable.parentData as EventModel
            val eventDurationMinutes = ChronoUnit.MINUTES.between(event.startDate, event.endDate)
            val eventHeight = ((eventDurationMinutes / 60f) + hourHeight.toPx()).roundToInt()
            val placeable = measurable.measure(constraints.copy(minWidth = dayWidth.roundToPx(), maxWidth = dayWidth.roundToPx(),minHeight = eventHeight,maxHeight = eventHeight))
            Pair(placeable, event)
        }
        layout(width , height) {
            placeablesWithEvents.forEach { (placeable, event) ->
                val eventOffsetMinutes = ChronoUnit.MINUTES.between(LocalTime.MIN, event.startDate.toLocalTime())
                val eventY = ((eventOffsetMinutes / 60f) * hourHeight.toPx()).roundToInt()
                val eventOffsetDays = ChronoUnit.DAYS.between(minDate, event.startDate.toLocalDate()).toInt()
                val eventX = eventOffsetDays * dayWidth.roundToPx()
                placeable.place(eventX,eventY)

            }
        }
    }
}

private class EventDataModifier( val event: EventModel): ParentDataModifier {
    override fun Density.modifyParentData(parentData: Any?) = event
}
private fun Modifier.eventData(event: EventModel) = this.then(EventDataModifier(event))



@Preview(showBackground = true)
@Composable
fun SchedulePreview() {
    WashingAgendaTheme() {
        Schedule(sampleEvents)
    }
}