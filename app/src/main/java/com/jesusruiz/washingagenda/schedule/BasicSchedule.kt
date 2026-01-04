package com.jesusruiz.washingagenda.schedule


import android.util.Log
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.ParentDataModifier
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.jesusruiz.washingagenda.models.EventModel
import java.time.LocalDate
import java.time.LocalTime
import java.time.temporal.ChronoUnit
import kotlin.math.roundToInt



@Composable
fun BasicSchedule(
    events: List<EventModel>,
    modifier: Modifier = Modifier,
    eventContent: @Composable (EventModel) -> Unit = { BasicEvent(event =  it) },
    minDate: LocalDate = events.minByOrNull (EventModel::startDate)!!.startDate.toLocalDate(),
    maxDate: LocalDate = events.maxByOrNull(EventModel::endDate)!!.endDate.toLocalDate(),
    dayWidth: Dp,
    hourHeight: Dp
) {
    val numDays = ChronoUnit.DAYS.between(minDate, maxDate).toInt() + 1
    val divideColor = if(!isSystemInDarkTheme()) Color.LightGray else Color.DarkGray
    Layout(content = {
        events.sortedBy(EventModel::startDate).forEach { event ->
            Box(modifier = Modifier.eventData(event)) {
                eventContent(event)
            }
        }
    }, modifier = modifier
        .drawBehind
        {
            repeat(23){
                drawLine(
                    divideColor,
                    start = Offset(0f,(it +1) * hourHeight.toPx()),
                    end = Offset(size.width, (it+1) * hourHeight.toPx()),
                    strokeWidth = 1.dp.toPx()
                )
            }
            repeat(numDays - 1){
                drawLine(
                    divideColor,
                    start = Offset((it + 1) * dayWidth.toPx(), 0f),
                    end = Offset((it + 1) * dayWidth.toPx(), size.height),
                    strokeWidth = 1.dp.toPx()
                )
            }
        }
    )
    { measurables, constraints ->
        val width = dayWidth.roundToPx() * numDays
        val height = hourHeight.roundToPx() * 24
        val placeablesWithEvents = measurables.map { measurable ->
            val event = measurable.parentData as EventModel
            val eventDurationMinutes = ChronoUnit.MINUTES.between(event.startDate, event.endDate)
            val eventHeight = ((eventDurationMinutes / 60f) * hourHeight.toPx()).roundToInt()
            val placeable = measurable.measure(constraints.copy(minWidth = dayWidth.roundToPx(), maxWidth = dayWidth.roundToPx(),minHeight = eventHeight,maxHeight = eventHeight))
            Pair(placeable, event)
        }
        layout(width , height) {
            placeablesWithEvents.forEach { (placeable, event) ->
                val eventOffsetMinutes = ChronoUnit.MINUTES.between(LocalTime.MIN, event.startDate.toLocalTime())
                Log.d("hora", (eventOffsetMinutes / 60f).toString())
                val eventY = ((eventOffsetMinutes / 60f) * hourHeight.toPx()).roundToInt()
                Log.d("hora", eventY.toString())
                val eventOffsetDays = ChronoUnit.DAYS.between(minDate, event.startDate.toLocalDate()).toInt()
                val eventX = eventOffsetDays * dayWidth.roundToPx()
                Log.d("OFFSET", "Evento X: $eventX, Event Y: $eventY")
                placeable.place(eventX,eventY)

            }
        }
    }
}

private class EventDataModifier( val event: EventModel): ParentDataModifier {
    override fun Density.modifyParentData(parentData: Any?) = event
}
private fun Modifier.eventData(event: EventModel) = this.then(EventDataModifier(event))



