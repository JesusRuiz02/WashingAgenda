package com.jesusruiz.washingagenda.viewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
): ViewModel() {

    data class HomeUIState(
        var isAddingEvent: Boolean = false,
        var currentTime: LocalDateTime = LocalDateTime.now(),
        val startDateTime: LocalDateTime = LocalDateTime.now(),
        val endDateTime: LocalDateTime = currentTime.plusDays(6),
        val eventStart: LocalDateTime = LocalDateTime.now(),
        val eventEnd: LocalDateTime = LocalDateTime.now()
        )

    sealed class HomeInputAction{
        data class IsAddingEventChange(val value: Boolean): HomeInputAction()
        data class IsStartDateEventChanged(val value: LocalDateTime): HomeInputAction()
        data class IsEndDateEventChanged(val value: LocalDateTime): HomeInputAction()
    }


    var homeState by mutableStateOf(HomeUIState())
            private set

    fun signOut(){
        auth.signOut()
    }

    fun onAction(action: HomeInputAction) {
        when(action){
            is HomeInputAction.IsAddingEventChange ->{
               homeState = homeState.copy( isAddingEvent = action.value)
            }
            is HomeInputAction.IsStartDateEventChanged ->
            {
                homeState = homeState.copy(eventStart = action.value)
            }
            is HomeInputAction.IsEndDateEventChanged ->{
                homeState = homeState.copy(eventEnd = action.value)

            }

        }
    }

}