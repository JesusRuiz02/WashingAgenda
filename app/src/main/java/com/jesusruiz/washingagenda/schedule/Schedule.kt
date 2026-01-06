import android.util.Log
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.ParentDataModifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.type.Fraction
import com.jesusruiz.washingagenda.schedule.BasicDayHeader
import com.jesusruiz.washingagenda.schedule.BasicEvent
import com.jesusruiz.washingagenda.schedule.ScheduleHeader
import com.jesusruiz.washingagenda.schedule.ScheduleSideBar
import com.jesusruiz.washingagenda.schedule.sampleEvents
import com.jesusruiz.washingagenda.models.EventModel
import com.jesusruiz.washingagenda.schedule.arrangeEvents
import com.jesusruiz.washingagenda.ui.theme.WashingAgendaTheme
import java.time.LocalDate
import java.time.LocalTime
import java.time.temporal.ChronoUnit
import kotlin.math.roundToInt


private sealed class ScheduleParentData{
    data object Header : ScheduleParentData()
    data object SideBar : ScheduleParentData()
    data class Event(val event: EventModel,
        var widthFraction: Float = 1f,
        var xOffsetFraction: Float = 0f) : ScheduleParentData()
}

private class ScheduleDataModifier(val data: ScheduleParentData) : ParentDataModifier{
    override fun Density.modifyParentData(parentData: Any?): Any = this@ScheduleDataModifier.data
}

private fun Modifier.scheduleHeader() = this.then(ScheduleDataModifier(ScheduleParentData.Header))
private fun Modifier.scheduleSideBar() = this.then(ScheduleDataModifier(ScheduleParentData.SideBar))
private fun Modifier.scheduleEvent(event: EventModel,
                                   widthFraction: Float = 1f,
                                   xOffsetFraction: Float = 0f) = this.then(ScheduleDataModifier(ScheduleParentData.Event(
    event = event,
    widthFraction = widthFraction,
    xOffsetFraction = xOffsetFraction)))



@Composable
fun Schedule(
    events: List<EventModel>,
    modifier: Modifier = Modifier,
    eventContent: @Composable (event: EventModel) -> Unit = { BasicEvent(event = it) },
    dayHeader: @Composable (day: LocalDate) -> Unit = { BasicDayHeader(day = it) },
    dayWidth: Dp = 256.dp,
    hourHeight: Dp = 64.dp,
    pastDaysPreview: Long = 4,
    postDaysPreview: Long = 6
) {
    val today = LocalDate.now()
    val minDateCalendar = today.minusDays(pastDaysPreview)
    val maxDateCalendar = today.plusDays(postDaysPreview)
    val numDays = ChronoUnit.DAYS.between(minDateCalendar, maxDateCalendar).toInt() + 1
    val verticalScrollState = rememberScrollState()
    val horizontalScrollState = rememberScrollState()
    val dividerColor = if(androidx.compose.foundation.isSystemInDarkTheme()) Color.DarkGray else Color.LightGray
    val visualEvents = remember(events) {
        arrangeEvents(events)
    }
    Layout(
        content = {
            ScheduleHeader(
                minDate = minDateCalendar,
                maxDate = maxDateCalendar,
                dayWidth = dayWidth,
                dayHeader = dayHeader,
                modifier = Modifier.scheduleHeader()
                )
            ScheduleSideBar(
                hourHeight = hourHeight,
                modifier = Modifier.scheduleSideBar()
            )
            visualEvents.forEach { visualEvent ->

                Box(modifier = Modifier.scheduleEvent(visualEvent.event, visualEvent.widthFraction, visualEvent.xOffsetFraction))
                {
                   eventContent(visualEvent.event)
                }
            }
        },
        modifier = modifier
            .verticalScroll(verticalScrollState)
            .horizontalScroll(horizontalScrollState)
            .drawBehind {
                val dayWidthPx = dayWidth.toPx()
                val hourHeightPx = hourHeight.toPx()
                val sideBarWidthPx = 56.dp.toPx()
                val headerHeightPx = 64.dp.toPx()

                repeat(23){
                    val y = headerHeightPx + (it + 1) * hourHeightPx
                    drawLine(dividerColor, start = Offset(sideBarWidthPx, y), end = Offset(size.width , y), strokeWidth = 1.dp.toPx())
                }
                repeat(numDays - 1){
                    val x = sideBarWidthPx + (it + 1) * dayWidthPx
                    drawLine(dividerColor, start = Offset(x, headerHeightPx), end = Offset(x, size.height), strokeWidth = 1.dp.toPx())
                }
            }
    ){ measurables, constraints ->
        val dayWithPx = dayWidth.roundToPx()
        val hourHeightPx = hourHeight.toPx()

        val headerMeasurable = measurables.first { it.parentData == ScheduleParentData.Header }
        val sidebarMeasurable = measurables.first { it.parentData == ScheduleParentData.SideBar}
        val eventMeasurable = measurables.filter { it.parentData is ScheduleParentData.Event}

       val sideBarPlaceable = sidebarMeasurable.measure(
           constraints.copy(minWidth = 0, minHeight = 0)
       )

       val sideBarWidthPx = sideBarPlaceable.width

       val headerPlaceable = headerMeasurable.measure(
           constraints.copy(minWidth = dayWithPx * numDays,maxWidth = dayWithPx * numDays, minHeight = 0)
       )
       val headerHeightPx = headerPlaceable.height

        val eventPlaceables = eventMeasurable.map {
            measurable ->
            val eventData = measurable.parentData as ScheduleParentData.Event
            val event = eventData.event
            Log.d("event",eventData.widthFraction.toString() )
            val eventWidthPx = (dayWithPx * eventData.widthFraction).roundToInt()
            val durationMinutes = ChronoUnit.MINUTES.between(event.startDate, event.endDate)
            val eventHeightPx = ((durationMinutes / 60f) * hourHeightPx).roundToInt().coerceAtLeast(0)
            val placeable = measurable.measure(
                constraints.copy(minWidth = eventWidthPx, maxWidth = eventWidthPx, minHeight = eventHeightPx, maxHeight = eventHeightPx)
            )

            Triple(placeable, event, eventData)
        }

        val contentWidth = dayWithPx * numDays
        val contentHeight = (hourHeightPx * 24).roundToInt()
        val totalWidth = sideBarWidthPx + contentWidth
        val totalHeight = headerHeightPx + contentHeight

        layout(totalWidth, totalHeight)
        {
            headerPlaceable.place(x = sideBarWidthPx, y = 0)
            sideBarPlaceable.place(x = 0, y = headerHeightPx)

            eventPlaceables.forEach { (placeable, event, eventData)->
                val offsetMinutes = ChronoUnit.MINUTES.between(LocalTime.MIN, event.startDate.toLocalTime())
                val offsetDays = ChronoUnit.DAYS.between(minDateCalendar, event.startDate.toLocalDate()).toInt()
                val eventY = ((offsetMinutes / 60f) * hourHeightPx).roundToInt()
                val eventX = offsetDays * dayWithPx
                val xOffsetPx = (dayWithPx * eventData.xOffsetFraction).roundToInt()
                placeable.place(
                    x = sideBarWidthPx + eventX + xOffsetPx,
                    y = headerHeightPx + eventY
                )
            }
        }
    }
}
@Preview(showBackground = true)
@Composable
fun BasicSchedulePreview() {
    WashingAgendaTheme {
        Schedule(sampleEvents)
    }
}