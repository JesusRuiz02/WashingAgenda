import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jesusruiz.washingagenda.items.schedule.BasicDayHeader
import com.jesusruiz.washingagenda.items.schedule.BasicEvent
import com.jesusruiz.washingagenda.items.schedule.BasicSchedule
import com.jesusruiz.washingagenda.items.schedule.ScheduleHeader
import com.jesusruiz.washingagenda.items.schedule.ScheduleSideBar
import com.jesusruiz.washingagenda.items.schedule.sampleEvents
import com.jesusruiz.washingagenda.models.EventModel
import com.jesusruiz.washingagenda.ui.theme.WashingAgendaTheme
import java.time.LocalDate

@Composable
fun Schedule(
    events: List<EventModel>,
    modifier: Modifier = Modifier,
    eventContent: @Composable (event: EventModel) -> Unit = { BasicEvent(event = it) },
        minDate: LocalDate = events.minByOrNull(EventModel::startDate)!!.startDate.toLocalDate(),
        maxDate: LocalDate = events.maxByOrNull(EventModel::endDate)!!.endDate.toLocalDate(),
    dayHeader: @Composable (day: LocalDate) -> Unit = { BasicDayHeader(day = it) }
){
    val dayWidth = 256.dp
    val hourHeight = 64.dp
    val verticalScrollState = rememberScrollState()
    val horizontalScrollState = rememberScrollState()
    var sidebarWidth by remember { mutableStateOf(0) }
    Column(modifier = modifier) {
        ScheduleHeader(minDate = minDate,
            maxDate = maxDate,
            dayWidth = dayWidth,
            dayHeader = dayHeader,
            modifier = Modifier
                .padding(start = with(LocalDensity.current) { sidebarWidth.toDp()})
                .horizontalScroll(horizontalScrollState))

        Row(modifier = Modifier.weight(1f)){
            ScheduleSideBar(
                hourHeight = hourHeight,
                modifier = Modifier
                    .verticalScroll(verticalScrollState)
                    .onGloballyPositioned { sidebarWidth = it.size.width}
            )
            BasicSchedule(
                events = events,
                eventContent = eventContent,
                minDate = minDate,
                maxDate = maxDate,
                dayWidth = dayWidth,
                hourHeight = hourHeight,
                modifier = Modifier
                    .weight(1f) // Fill remaining space in the column
                    .verticalScroll(verticalScrollState)
                    .horizontalScroll(horizontalScrollState)
            )
        }



    }

}
@Preview(showBackground = true)
@Composable
fun BasicSchedulePreview() {
    WashingAgendaTheme() {
        Schedule(sampleEvents)
    }
}