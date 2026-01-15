package com.jesusruiz.washingagenda.viewModel

import android.R
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.jesusruiz.washingagenda.models.EventModel
import com.jesusruiz.washingagenda.models.UserModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore,

): ViewModel() {

    val currentTime: LocalDateTime = LocalDateTime.now()
    val maxTime: LocalDateTime = LocalDateTime.now().plusDays(6)

    data class HomeUIState(
        var isAddingEvent: Boolean = false,
        var user : UserModel = UserModel(),
        var userUID: String = "",
        val eventStart: LocalDateTime = LocalDateTime.now(),
        val eventEnd: LocalDateTime = LocalDateTime.now(),
        val event: EventModel = EventModel(color = Color.DarkGray, endDate = LocalDateTime.now().plusDays(6), startDate =  LocalDateTime.now())
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
    init {
        getUserUID()
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
    fun getUserUID()
    {
        homeState = homeState.copy(userUID = auth.currentUser?.uid ?: "" )
    }
    fun addEvent(onSuccess: () -> Unit ){
        viewModelScope.launch {
            try {
                var userToSave:UserModel = homeState.user
                if (userToSave.userID.isEmpty() && homeState.userUID.isNotEmpty()) {
                    userToSave = getUserById(homeState.userUID) ?: UserModel()
                }
               if(userToSave.userID.isNotEmpty()){
                   saveEvent(userToSave)
                   onSuccess()
               }else{
                   Log.d("Error", "No se pudo obtener un usuario válido para guardar el evento.")
               }
            }
            catch (e: Exception)
            {
                Log.d("Error", "The user could not be added ${e.message}")
            }
        }
    }
    suspend fun getUserById(idUser: String): UserModel?{
         return try{
                val doc = firestore
                    .collection("Users")
                    .document(idUser)
                    .get()
                    .await()
                val user = doc.toObject(UserModel::class.java)
                if(user != null) {
                    homeState = homeState.copy(user = user)
                    Log.d("user ", user.toString())
                }
                 user
            }
            catch (e: Exception)
            {
                Log.e("Error", e.message ?: "Error getting users")
                null
            }
        }
    private suspend fun saveEvent( user: UserModel)
    {
        val eventId = "${user.userID}_${homeState.eventStart}"
        val event = EventModel( userID = user.userID, startDate = homeState.eventStart, endDate = homeState.eventEnd,color = homeState.event.color, departmentN = user.departmentN, building = user.building, id = eventId)
        firestore
            .collection("Events")
            .document(eventId)
            .set(event)
            .await()
        Log.d("Success", "User is saved")
    }


}