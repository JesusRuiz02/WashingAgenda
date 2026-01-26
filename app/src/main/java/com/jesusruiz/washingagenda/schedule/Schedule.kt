import android.util.Log
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.jesusruiz.washingagenda.schedule.BasicDayHeader
import com.jesusruiz.washingagenda.schedule.BasicEvent
import com.jesusruiz.washingagenda.schedule.ScheduleHeader
import com.jesusruiz.washingagenda.schedule.ScheduleSideBar
import com.jesusruiz.washingagenda.schedule.sampleEvents
import com.jesusruiz.washingagenda.models.EventModel
import com.jesusruiz.washingagenda.schedule.BasicSchedule
import com.jesusruiz.washingagenda.ui.theme.WashingAgendaTheme
import java.time.LocalDate
import java.time.temporal.ChronoUnit


@Composable
fun Schedule(
    events: List<EventModel>,
    modifier: Modifier = Modifier,
    onEventClick: (EventModel) -> Unit = {},
    eventContent: @Composable (event: EventModel) -> Unit = { BasicEvent(event = it) },
    dayHeader: @Composable (day: LocalDate) -> Unit = { BasicDayHeader(day = it) },
    dayWidth: Dp = 256.dp,
    hourHeight: Dp = 64.dp,
    pastDaysPreview: Long = 4,
    enableOverlapCalculations: Boolean = true,
    postDaysPreview: Long = 6
) {
    val today = LocalDate.now()
    val minDateCalendar = today.minusDays(pastDaysPreview)
    val maxDateCalendar = today.plusDays(postDaysPreview)
    val verticalScrollState = rememberScrollState()
    val horizontalScrollState = rememberScrollState()

    val density = LocalDensity.current
    LaunchedEffect(Unit) {
        val sideBarWidth = 56.dp

        val scrollPositionPx = with(density){
            val sideBarWidthPx = sideBarWidth.toPx()
            val dayWidthPx = dayWidth.toPx()
            (pastDaysPreview * dayWidthPx) - sideBarWidthPx
        }


        horizontalScrollState.scrollTo(scrollPositionPx.toInt())
    }

    Column(modifier = Modifier.fillMaxHeight()){
        Row{
            Spacer(modifier = Modifier.width(56.dp))
            ScheduleHeader(minDate = minDateCalendar,
                maxDate = maxDateCalendar,
                dayWidth = dayWidth,
                dayHeader = dayHeader,
               modifier = Modifier.horizontalScroll(horizontalScrollState))
        }
        Row()
        {
            ScheduleSideBar(hourHeight = hourHeight,
              modifier =  Modifier.verticalScroll(verticalScrollState)
                  .width(56.dp)
            )
            BasicSchedule(events = events,
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(verticalScrollState)
                    .horizontalScroll(horizontalScrollState),
                eventContent = eventContent,
                onEventClick = onEventClick,
                dayWidth = dayWidth,
                hourHeight = hourHeight,
                pastDaysPreview = pastDaysPreview,
                postDaysPreview = postDaysPreview,
                enableOverlapCalculations = enableOverlapCalculations
            )
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