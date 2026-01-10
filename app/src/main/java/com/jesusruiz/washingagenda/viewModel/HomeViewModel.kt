package com.jesusruiz.washingagenda.viewModel

import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.LocalTime
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(

): ViewModel() {

    data class HomeUIState(
        var isAddingEvent: Boolean = false,
        var currentTime: LocalTime = LocalTime.now(),
        val startPickerHour : Int  = LocalTime.now().hour,
        var startPickerMinute : Int = LocalTime.now().minute
    )

    sealed class HomeInputAction{
        data class IsAddingEventChange(val value: Boolean): HomeInputAction()
        data class IsStartTimeChanged(val hour: Int, val minutes: Int): HomeInputAction()
    }


    var homeState by mutableStateOf(HomeUIState())
            private set

    fun onAction(action: HomeInputAction) {
        when(action){
            is HomeInputAction.IsAddingEventChange ->{
               homeState = homeState.copy( isAddingEvent = action.value)
            }
            is HomeInputAction.IsStartTimeChanged ->{
                homeState = homeState.copy(startPickerHour = action.hour, startPickerMinute = action.minutes)
            }
        }
    }

}