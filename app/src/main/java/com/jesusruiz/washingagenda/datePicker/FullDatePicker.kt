package com.jesusruiz.washingagenda.datePicker

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import com.commandiron.wheel_picker_compose.WheelTimePicker
import com.jesusruiz.washingagenda.ui.theme.WashingAgendaTheme
import java.time.LocalDate
import java.time.LocalTime
import com.jesusruiz.washingagenda.longToLocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FullDatePicker(modifier: Modifier = Modifier,
                   date: LocalDate, hour: LocalTime,
                   onDateChanged: (LocalDate) -> Unit = {}, onHourChanged: (LocalTime) -> Unit = {},
                   startText:String = "Inicia",
                   initialDatePickerStatus: DatePickerStatus = DatePickerStatus.Empty
){
    var datePickerStatus by remember { mutableStateOf(DatePickerStatus.Empty) }
    LaunchedEffect(Unit) {
        datePickerStatus = initialDatePickerStatus
    }
    Card (modifier = modifier
        .fillMaxWidth()
        .padding()
        .background(MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(16.dp)
       ){
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
               ) {
                Text(modifier = Modifier
                    .padding(start = 20.dp, end = 20.dp) ,
                    text= startText,
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.secondary)
                Button(shape = RoundedCornerShape(8.dp),onClick = {
                    datePickerStatus = if(datePickerStatus == DatePickerStatus.Date){
                        DatePickerStatus.Empty
                    } else{
                        DatePickerStatus.Date
                    }
                },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                    modifier = Modifier.padding(start = 5.dp)) {
                    Text(text = date.toString(), color = MaterialTheme.colorScheme.secondary)
                }
                Button(shape = RoundedCornerShape(8.dp) ,onClick = {
                    datePickerStatus = if(datePickerStatus == DatePickerStatus.Hour){
                        DatePickerStatus.Empty
                    } else{
                        DatePickerStatus.Hour
                    }
                }, colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                    modifier = Modifier.padding(end = 20.dp)) {

                    Text(text = hour.toString(), color = MaterialTheme.colorScheme.secondary)
                }

            }
            AnimatedVisibility(
                visible = datePickerStatus == DatePickerStatus.Date,
                enter = fadeIn() + slideInVertically(initialOffsetY = { it }),
                exit = fadeOut() + slideOutVertically(targetOffsetY = { it })
            ) {
                val datePickerState = rememberDatePickerState()

                DatePickerDialog(
                    onDismissRequest = {},
                    confirmButton = {
                        TextButton(onClick = {
                            datePickerState.selectedDateMillis?.let {
                                onDateChanged(datePickerState.selectedDateMillis!!.longToLocalDate())

                            }
                            datePickerStatus = DatePickerStatus.Empty

                        }) {
                            Text("OK")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { datePickerStatus = DatePickerStatus.Empty }) {
                            Text("Cancel")
                        }
                    }
                ) {
                    DatePicker(state = datePickerState)
                }
            }
            AnimatedVisibility(
                visible = datePickerStatus == DatePickerStatus.Hour,
                enter = fadeIn() + slideInVertically(initialOffsetY = { it }),
                exit = fadeOut() + slideOutVertically(targetOffsetY = { it })
            ) {
                WheelTimePicker(modifier = Modifier
                    .padding(horizontal = 10.dp, vertical = 10.dp)
                    .align(alignment = androidx.compose.ui.Alignment.CenterHorizontally),
                    startTime = hour,
                    size = DpSize(300.dp, 150.dp),
                    textColor = MaterialTheme.colorScheme.secondary){
                    snapDateTime -> onHourChanged(snapDateTime)
                }
            }

        }

    }

}


enum class DatePickerStatus {Date, Empty, Hour}

val SampleDate = LocalDate.now()
val hour = LocalTime.now()


@Preview(showBackground = true)
@Composable
fun BasicFullDatePreview() {
    WashingAgendaTheme {
        FullDatePicker(modifier = Modifier,SampleDate, hour )
    }
}