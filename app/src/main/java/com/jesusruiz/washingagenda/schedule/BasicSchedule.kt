package com.jesusruiz.washingagenda.schedule


import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
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
import com.jesusruiz.washingagenda.toLocalDateTime
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.temporal.ChronoUnit
import kotlin.math.roundToInt




private data class EventParentData(
    val event: EventModel,
    var widthFraction: Float,
    var xOffsetFraction: Float
)

private class ScheduleDataModifier(val data: EventParentData) : ParentDataModifier {
    override fun Density.modifyParentData(parentData: Any?): Any = this@ScheduleDataModifier.data
}



private fun Modifier.scheduleEvent(event: EventModel,
                                   widthFraction: Float = 1f,
                                   xOffsetFraction: Float = 0f) =
    this.then(
        ScheduleDataModifier(
            EventParentData(
    event = event,
    widthFraction = widthFraction,
    xOffsetFraction = xOffsetFraction)))


@Composable
fun BasicSchedule(
    events: List<EventModel>,
    modifier: Modifier = Modifier,
    onEventClick: (EventModel) -> Unit = {},
    eventContent: @Composable (event: EventModel) -> Unit = { BasicEvent(event = it) },
    dayWidth: Dp = 256.dp,
    hourHeight: Dp = 64.dp,
    pastDaysPreview: Long = 4,
    postDaysPreview: Long = 6,
    enableOverlapCalculations: Boolean = true
)
{
    val today = LocalDate.now()
    val minDateCalendar = today.minusDays(pastDaysPreview)
    val maxDateCalendar = today.plusDays(postDaysPreview)
    val numDays = ChronoUnit.DAYS.between(minDateCalendar, maxDateCalendar).toInt() + 1

    val dividerColor = if(androidx.compose.foundation.isSystemInDarkTheme()) Color.DarkGray else Color.LightGray

    val visualEvents = remember(events,enableOverlapCalculations) {
        if(enableOverlapCalculations){
            arrangeEvents(events)
        }
        else{
            events.map { VisualEvent(it) }
        }
    }
    Layout(content = {
        visualEvents.forEach { visualEvent ->
            Box(modifier = Modifier.scheduleEvent(visualEvent.event,visualEvent.widthFraction, visualEvent.xOffsetFraction))
            {
                BasicEvent(
                    event = visualEvent.event,
                    onClick = onEventClick
                )
            }
        }
    },
        modifier = modifier
            .drawBehind{
                val dayWidthPx = dayWidth.toPx()
                val hourHeightPx = hourHeight.toPx()
                repeat(23){
                    val y = (it + 1) * hourHeightPx
                    drawLine(dividerColor, start = Offset(0f, y), end = Offset(size.width , y), strokeWidth = 1.dp.toPx())
                }
                repeat(numDays -1)
                {
                    val x =   (it + 1) * dayWidthPx
                    drawLine(dividerColor, start = Offset(x, 0f), end = Offset(x, size.height), strokeWidth = 1.dp.toPx())
                }
            }
    ) { measurables, constraints ->
        val dayWidthPx = dayWidth.toPx()
        val hourHeightPx = hourHeight.toPx()

        val eventMeasurable = measurables.filter{ it.parentData is EventParentData}

        val eventPlaceables = eventMeasurable.map{
            measurable ->
            val eventData = measurable.parentData as EventParentData
            val event = eventData.event
            val eventWidthPx = (dayWidthPx * eventData.widthFraction).roundToInt()
            val durationMinutes = ChronoUnit.MINUTES.between(event.startDate!!.toLocalDateTime(), event.endDate!!.toLocalDateTime())
            val eventHeightPx= ((durationMinutes/60) * hourHeightPx).roundToInt().coerceAtLeast(0)
            val placeable = measurable.measure(
                constraints.copy(minWidth = eventWidthPx, maxWidth = eventWidthPx, minHeight = eventHeightPx, maxHeight = eventHeightPx)
            )
            Triple(placeable,event,eventData)
        }

        val contentWidth = (dayWidthPx * numDays).roundToInt()
        val contentHeight = (hourHeightPx * 24).roundToInt()

        layout(contentWidth,contentHeight){
            eventPlaceables.forEach{(placeable, event, eventData ) ->
                val eventStartDateTime = event.startDate?.toLocalDateTime() ?: LocalDateTime.now()
                val offsetMinutes = ChronoUnit.MINUTES.between(LocalTime.MIN, eventStartDateTime.toLocalTime())
                val offsetDays = ChronoUnit.DAYS.between(minDateCalendar, eventStartDateTime.toLocalDate()).toInt()
                val eventY = ((offsetMinutes / 60f) * hourHeightPx).roundToInt()
                val eventX = (offsetDays * dayWidthPx).roundToInt()
                val xOffsetPx = (dayWidthPx * eventData.xOffsetFraction).roundToInt()
                placeable.place(
                   x = eventX + xOffsetPx,
                    y= eventY
                )
            }
        }

    }
}