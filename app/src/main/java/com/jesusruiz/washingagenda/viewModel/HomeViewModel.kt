package com.jesusruiz.washingagenda.viewModel


import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.snapshots
import com.jesusruiz.washingagenda.CeilToNextSlot
import com.jesusruiz.washingagenda.models.EventModel
import com.jesusruiz.washingagenda.models.EventStatus
import com.jesusruiz.washingagenda.models.UserModel
import com.jesusruiz.washingagenda.toComposeColor
import com.jesusruiz.washingagenda.toDate
import com.jesusruiz.washingagenda.toHexString
import com.jesusruiz.washingagenda.toLocalDateTime
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.time.Duration
import java.time.LocalDateTime
import javax.inject.Inject

data class HomeUIState(
    var isAddingEvent: Boolean = false,
    var user : UserModel = UserModel(),
    var userUID: String = "",
    val eventStart: LocalDateTime = LocalDateTime.now().CeilToNextSlot(),
    val eventEnd: LocalDateTime = LocalDateTime.now().plusHours(1).CeilToNextSlot(),
    val selectedColor: Color = Color.Blue,
    var isLoading: Boolean = false,
    val editingEvent: EventModel? = null,
    val events: List<EventModel> = emptyList(),
    val errorMessage: String? = null,
)

sealed class HomeInputAction{
    data class IsAddingEventChange(val value: Boolean): HomeInputAction()
    data class IsStartDateEventChanged(val value: LocalDateTime): HomeInputAction()
    data class IsEndDateEventChanged(val value: LocalDateTime): HomeInputAction()
    data class LoadingEventsChanged(val value: Boolean): HomeInputAction()
    data class EditingEventsChanged(val value: EventModel): HomeInputAction()
    object ClearDatesPicker: HomeInputAction()

    object CancelEditingEvent: HomeInputAction()
}

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore,

): ViewModel() {

    val currentTime: LocalDateTime = LocalDateTime.now()
    val maxTime: LocalDateTime = LocalDateTime.now().plusDays(6)

    private val _homeState = mutableStateOf(HomeUIState())
    val homeState: State<HomeUIState> = _homeState


    fun signOut(){
        auth.signOut()
    }
    init {
        getUserUID()
    }

    fun onAction(action: HomeInputAction) {
        when(action){
            is HomeInputAction.IsAddingEventChange ->{
               _homeState.value = _homeState.value.copy( isAddingEvent = action.value)
            }
            is HomeInputAction.IsStartDateEventChanged ->
            {
              val value = action.value.CeilToNextSlot()
                _homeState.value = _homeState.value.copy(eventStart = value)
            }
            is HomeInputAction.IsEndDateEventChanged ->{
                val value = action.value.CeilToNextSlot()
                _homeState.value = _homeState.value.copy(eventEnd = value)
            }
            is HomeInputAction.LoadingEventsChanged ->{
                _homeState.value = _homeState.value.copy(isLoading = action.value)
            }
            is HomeInputAction.EditingEventsChanged ->{
                _homeState.value = _homeState.value.copy(editingEvent = action.value)
            }
            is HomeInputAction.CancelEditingEvent ->{
                _homeState.value = _homeState.value.copy(editingEvent = null)
            }
            is HomeInputAction.ClearDatesPicker ->{
                _homeState.value = _homeState.value.copy(eventStart =LocalDateTime.now().CeilToNextSlot() ,
                    eventEnd = LocalDateTime.now().plusHours(1).CeilToNextSlot())
            }

        }
    }


    fun onErrorMessageShown(){
        _homeState.value = _homeState.value.copy(errorMessage = null)
    }



  private suspend fun userHaveHoursAvailable(): Boolean{
            try {
                if(_homeState.value.userUID.isEmpty())
                    getUserUID()
                if (_homeState.value.userUID.isNotEmpty() && _homeState.value.user.building.isEmpty()){
                    getUserById(_homeState.value.userUID)
                }
                val user = _homeState.value.user
                val hourDifference = getHourDifference(homeState.value.eventStart, homeState.value.eventEnd)
                if(hourDifference > user.hours){
                    _homeState.value = _homeState.value.copy(errorMessage = "No tienes horas disponibles")
                    return false
                }
                return true
            }
            catch (e: Exception){
                Log.d("Error", e.toString())
                _homeState.value = _homeState.value.copy(errorMessage = "Ocurrio un error inesperado")
                return false
            }
    }

    private fun isEventAvailable(isEditing: Boolean = false): Boolean {
            try {
                val events = _homeState.value.events
                val eventStart = _homeState.value.eventStart
                val eventEnd = _homeState.value.eventEnd
                if(eventEnd.isBefore(eventStart)|| eventEnd.isEqual(eventStart)){
                    _homeState.value = _homeState.value.copy(errorMessage = "Los horarios no son válidos.")
                    return false
                }
                for (event in events) {
                    if (isEditing && event.id == _homeState.value.editingEvent?.id)
                        continue
                    if (eventStart.isBefore(event.endDate) && eventEnd.isAfter(event.startDate)) {
                        _homeState.value = _homeState.value.copy(errorMessage = "El horario seleccionado ya está ocupado.")
                        return false
                    }

                }
                return true

            }catch (e: Exception){
                Log.e("Error", e.toString())
                _homeState.value = _homeState.value.copy(errorMessage = "Ocurrio un error inesperado intente de nuevo")
                return false
            }
    }

    fun gettingEventById(id: String){
        for (event in _homeState.value.events){
            if(event.id == id){
                _homeState.value = _homeState.value.copy(editingEvent = event)
            }
        }
    }

    fun getHourDifference(startHour: LocalDateTime, endHour: LocalDateTime): Long{
        val difference = Duration.between(startHour, endHour)
        var differenceHour = difference.toMinutes()
        differenceHour /= 60
        return differenceHour

    }

   fun deleteEvent(onSuccess: () -> Unit){
        viewModelScope.launch {
            val user = _homeState.value.user
            val event = _homeState.value.editingEvent ?: return@launch
            val hourDifference = getHourDifference(event.startDate!!, event.endDate!!)
            try {
                val status = hashMapOf(
                    "status" to "Canceled"
                )
                firestore.collection("Events").document(event.id)
                    .update(status as Map<String, Any>).await()
              val userDifference = firestore.collection("Users").document(event.userID)
                  userDifference.update("hours", FieldValue.increment(hourDifference.toDouble()))
                onSuccess()
            } catch (e: Exception) {
                _homeState.value = _homeState.value.copy(errorMessage = "El evento no se pudo eliminar.")
                Log.d("Error", e.toString())
            }
        }
    }

    fun editEvent(onSuccess: () -> Unit){
        viewModelScope.launch {
            if(!isEventAvailable(isEditing = true)) return@launch
            if(!userHaveHoursAvailable()) return@launch
            val oldEvent = _homeState.value.editingEvent?.id
            if(oldEvent.isNullOrEmpty()) return@launch
            try{
                val differenceHour = getHourDifference(_homeState.value.eventStart, _homeState.value.eventEnd)
                val eventDifference = getHourDifference(_homeState.value.editingEvent!!.startDate!!, _homeState.value.editingEvent!!.endDate!!)
                val difference = eventDifference - differenceHour
                val user = _homeState.value.user
                val userRef = firestore.collection("Users").document(user.userID)
                firestore.collection("Events").document(oldEvent)
                    .update("status", "Edited").await()
                saveEvent(user)
                    userRef.update("hours", FieldValue.increment(difference)).await()
            _homeState.value = _homeState.value.copy(editingEvent = null)
            onSuccess()
            }
            catch (e: Exception){
                _homeState.value = _homeState.value.copy(errorMessage = "Ocurrió un error al guardar los cambios.")
                Log.e("Error", e.toString())
            }

        }

    }

    fun getEvents(){
        viewModelScope.launch {
            if(_homeState.value.userUID.isEmpty())
                getUserUID()
            if (_homeState.value.userUID.isNotEmpty() && _homeState.value.user.building.isEmpty()){
                getUserById(_homeState.value.userUID)
            }
            val userBuilding = _homeState.value.user.building
            if(userBuilding.isEmpty())
            {
                Log.d("Events", "El edificio esta vacio")
                return@launch
            }
            _homeState.value = _homeState.value.copy(isLoading = true)
            firestore
                .collection("Events")
                .whereEqualTo("building", userBuilding)
                .whereLessThanOrEqualTo("endDate", maxTime.toDate())
                .whereEqualTo("status", "Active")
                .snapshots()
                .collect {
                    querySnapshot ->
                    try{
                        val eventList = querySnapshot.documents.mapNotNull {
                            document ->
                            val start = document.getDate("startDate")?.toLocalDateTime()
                            val end = document.getDate("endDate")?.toLocalDateTime()
                            val color = document.getString("color")?.toComposeColor() ?: Color.Blue
                            EventModel(
                                id = document.id,
                                userID = document.getString("userID") ?: "",
                                departmentN = document.getString("departmentN") ?: "",
                                building = document.getString("building") ?: "",
                                startDate = start,
                                endDate = end,
                                color = color,
                                status = EventStatus.valueOf(document.getString("status") ?: "Active")
                            )
                        }
                            _homeState.value = _homeState.value.copy(events = eventList,
                                isLoading = false)
                            Log.d("Events", "Eventos actualizados ${eventList}")

                    }
                    catch (e: Exception){
                        _homeState.value = _homeState.value.copy(isLoading = false)
                        Log.e("Events", "No se pudieron actualizar los eventos: ${e.message}")
                    }
                }
        }
    }
    fun getUserUID()
    {
        _homeState.value = _homeState.value.copy(userUID = auth.currentUser?.uid ?: "" )
    }
    fun addEvent(onSuccess: () -> Unit ){
                viewModelScope.launch {
                    if(!isEventAvailable()) return@launch
                    if(!userHaveHoursAvailable()) return@launch
                        try {
                            if (_homeState.value.userUID.isEmpty())
                                getUserUID()
                            var userToSave: UserModel = _homeState.value.user
                            val differenceHour = getHourDifference(homeState.value.eventStart, homeState.value.eventEnd)
                            if (userToSave.userID.isEmpty() && _homeState.value.userUID.isNotEmpty()) {
                                userToSave = getUserById(_homeState.value.userUID) ?: UserModel()
                            }
                            if (userToSave.userID.isNotEmpty()) {
                                saveEvent(userToSave)
                              val reference =  firestore.collection("Users").document(userToSave.userID)
                                reference.update("hours", FieldValue.increment(-differenceHour)).await()
                                onSuccess()
                            } else {
                                Log.d(
                                    "Error",
                                    "No se pudo obtener un usuario válido para guardar el evento."
                                )
                            }
                        } catch (e: Exception) {
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
                    _homeState.value = _homeState.value.copy(user = user)
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
        val eventId = "${user.userID}_${_homeState.value.eventStart}"
       val data = mapOf(
           "id" to eventId,
           "userID" to user.userID,
           "startDate" to _homeState.value.eventStart.toDate(),
           "endDate" to _homeState.value.eventEnd.toDate(),
           "color" to _homeState.value.selectedColor.toHexString(),
           "building" to user.building,
           "departmentN" to user.departmentN,
           "status" to "Active"
       )
        firestore
            .collection("Events")
            .document(eventId)
            .set(data)
            .await()
        Log.d("Success", "User is saved")
    }


}