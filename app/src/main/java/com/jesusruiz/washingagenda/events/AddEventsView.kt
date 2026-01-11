package com.jesusruiz.washingagenda.events
import com.commandiron.wheel_picker_compose.WheelTimePicker
import com.commandiron.wheel_picker_compose.core.WheelPickerDefaults
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TimeInput
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.commandiron.wheel_picker_compose.WheelDateTimePicker
import com.commandiron.wheel_picker_compose.core.TimeFormat
import com.jesusruiz.washingagenda.R
import com.jesusruiz.washingagenda.viewModel.HomeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEventsView(navController: NavController, homeViewModel: HomeViewModel){
    val state = homeViewModel.homeState
    Scaffold(topBar = {
        TopAppBar(title = {
            Text(text ="Nuevo Evento" )},
            navigationIcon = {
                IconButton(onClick = {
                    homeViewModel.onAction(HomeViewModel.HomeInputAction.IsAddingEventChange(!state.isAddingEvent))}){
                    Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                }
            })
    })
    {
        paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {
            Box(modifier = Modifier
                .align(alignment = Alignment.CenterHorizontally)
                .background(color = colorResource(R.color.dark_green))
                .fillMaxWidth()
                .height(150.dp)

            ){
                Text(modifier = Modifier.padding(top = 10.dp),
                    text = "Horas restantes: 5",
                    color = Color.White,
                    style = MaterialTheme.typography.titleMedium)
                WheelDateTimePicker(startDateTime = homeViewModel.homeState.startDateTime,
                    minDateTime = homeViewModel.homeState.startDateTime,
                    maxDateTime = homeViewModel.homeState.endDateTime,
                    timeFormat = TimeFormat.HOUR_24,
                    textColor = Color.DarkGray,
                    rowCount = 5,
                    selectorProperties = WheelPickerDefaults.selectorProperties(
                        enabled = true,
                        shape = RoundedCornerShape(6.dp),
                        color = Color.White,
                    ),)
            }
        }

    }
}
