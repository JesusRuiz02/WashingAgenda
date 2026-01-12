package com.jesusruiz.washingagenda.events
import com.commandiron.wheel_picker_compose.core.WheelPickerDefaults
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.DpSize
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
        TopAppBar(
            title = {
            Text(text ="Nuevo Evento", fontWeight = FontWeight.Bold, color = Color.White )
                    },
            navigationIcon = {
                TextButton (onClick = {
                    homeViewModel.onAction(HomeViewModel.HomeInputAction.IsAddingEventChange(!state.isAddingEvent))})
                {
                    Text(text = "Cancelar", color = Color.White)                }
            },
            actions = {
                TextButton(onClick = {
                    homeViewModel.onAction(HomeViewModel.HomeInputAction.IsAddingEventChange(!state.isAddingEvent))
                })
                {
                    Text(text = "Guardar", color = Color.White)
                }
            }
        )
    })
    {
        paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {
            Box(modifier = Modifier
                .align(alignment = Alignment.CenterHorizontally)
                .background(color = colorResource(R.color.dark_green))
                .fillMaxWidth()
                .height(200.dp)

            ){
                Column {
                    Row(modifier = Modifier
                        .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically) {
                        Text(modifier = Modifier
                            .padding(start = 20.dp, end = 40.dp) ,
                            text= "Inicio",
                            style = MaterialTheme.typography.titleLarge,
                            color = Color.White)
                        WheelDateTimePicker(startDateTime = homeViewModel.homeState.startDateTime,
                            minDateTime = homeViewModel.homeState.startDateTime,
                            maxDateTime = homeViewModel.homeState.endDateTime,
                            timeFormat = TimeFormat.HOUR_24,
                            textColor = Color.DarkGray,
                            rowCount = 5,
                            size = DpSize(200.dp, 100.dp),
                            selectorProperties = WheelPickerDefaults.selectorProperties(
                                enabled = true,
                                shape = RoundedCornerShape(6.dp),
                                color = Color.White,
                            ),)
                    }
                    Row(modifier = Modifier
                        .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically) {
                        Text(modifier = Modifier
                            .padding(start = 20.dp, end = 40.dp) ,
                            text= "Fin",
                            style = MaterialTheme.typography.titleLarge,
                            color = Color.White)
                        WheelDateTimePicker(startDateTime = homeViewModel.homeState.startDateTime,
                            minDateTime = homeViewModel.homeState.startDateTime,
                            maxDateTime = homeViewModel.homeState.endDateTime,
                            timeFormat = TimeFormat.HOUR_24,
                            textColor = Color.DarkGray,
                            rowCount = 5,
                            size = DpSize(200.dp, 100.dp),
                            selectorProperties = WheelPickerDefaults.selectorProperties(
                                enabled = true,
                                shape = RoundedCornerShape(6.dp),
                                color = Color.White,
                            ),)
                    }



                }


            }
        }

    }
}
