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
import com.jesusruiz.washingagenda.getColorIDByStatus
import com.jesusruiz.washingagenda.models.EventModel
import com.jesusruiz.washingagenda.models.EventStatus
import com.jesusruiz.washingagenda.models.UserModel
import com.jesusruiz.washingagenda.toDate
import com.jesusruiz.washingagenda.toLocalDateTime
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.time.Duration
import java.time.LocalDateTime
import javax.inject.Inject
import kotlin.io.path.exists
import kotlin.math.roundToInt

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
    val previsualizationHour: Float = 0F,
    val differenceTimePickersDate: Float = 0F
)

sealed class HomeInputAction{
    data class IsAddingEventChange(val value: Boolean): HomeInputAction()
    data class IsStartDateEventChanged(val value: LocalDateTime): HomeInputAction()
    data class IsEndDateEventChanged(val value: LocalDateTime): HomeInputAction()
    data class LoadingEventsChanged(val value: Boolean): HomeInputAction()
    data class EditingEventsChanged(val value: EventModel): HomeInputAction()
    object ClearPrevisualizationHour: HomeInputAction()
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
        _homeState.value = _homeState.value.copy(
            isLoading = true)
        getUserUID()
        if(_homeState.value.userUID.isNotEmpty()){
            startUserListener(_homeState.value.userUID)
            getEvents()
        }


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
                _homeState.value = homeState.value.copy(differenceTimePickersDate = getHourDifference(value, homeState.value.eventEnd))
            }
            is HomeInputAction.IsEndDateEventChanged ->{
                val value = action.value.CeilToNextSlot()
                _homeState.value = _homeState.value.copy(eventEnd = value)
                _homeState.value = homeState.value.copy(differenceTimePickersDate = getHourDifference(homeState.value.eventStart, value))
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
            is HomeInputAction.ClearPrevisualizationHour -> {
                _homeState.value = homeState.value.copy(previsualizationHour = _homeState.value.user.hours)
            }

        }
    }

    fun enterEditingEvent(onSuccess: () -> Unit){
        if (_homeState.value.editingEvent == null) return
        if (_homeState.value.editingEvent?.status != EventStatus.Pending) return
        onSuccess()
    }

    fun getAddPrevisualizationHour(){
        _homeState.value = _homeState.value.copy(previsualizationHour = (_homeState.value.user.hours -_homeState.value.differenceTimePickersDate))
    }
    private fun startUserListener(userId: String) {
        viewModelScope.launch {
            firestore.collection("Users").document(userId)
                .snapshots()
                .collect { snapshot ->
                    if ( snapshot.exists()) {
                        val user = snapshot.toObject(UserModel::class.java)
                        if (user != null) {
                            _homeState.value = _homeState.value.copy(user = user)
                        }
                    }
                }
        }
    }


    fun onErrorMessageShown(){
        _homeState.value = _homeState.value.copy(errorMessage = null)
    }



  private fun userHaveHoursAvailable(): Boolean{
      val hourDifference = _homeState.value.previsualizationHour
      if(hourDifference < 0){
          _homeState.value = _homeState.value.copy(errorMessage = "No tienes horas disponibles")
          return false
      }
      return true
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
                    val overlaps = eventStart.isBefore(event.endDate) && eventEnd.isAfter(event.startDate)
                    val justTouching = eventStart.isEqual(event.endDate) || eventEnd.isEqual(event.startDate)
                    if(overlaps && !justTouching){
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

    fun getHourDifference(startHour: LocalDateTime, endHour: LocalDateTime): Float{
        val difference = Duration.between(startHour, endHour)
        val differenceHour = difference.toMinutes()
        val differenceMinutes = differenceHour.toFloat() /60.0f
        return  (differenceMinutes * 10).roundToInt() / 10F

    }

    fun getEditEventDifference(){
        val eventToEdit = homeState.value.editingEvent
        if(eventToEdit == null || eventToEdit.startDate == null || eventToEdit.endDate == null) return
        if(eventToEdit.id.isEmpty()) return
        val eventDifference = getHourDifference(eventToEdit.startDate!!, eventToEdit.endDate!!)
        val difference = eventDifference - _homeState.value.differenceTimePickersDate
        _homeState.value = _homeState.value.copy(previsualizationHour = (_homeState.value.user.hours + difference))
    }

   fun deleteEvent(onSuccess: () -> Unit){
        viewModelScope.launch {
            val event = _homeState.value.editingEvent
            Log.d("Error", "se mandi a llamar")
            if(event == null || event.endDate == null || event.startDate == null)
            {
                _homeState.value = _homeState.value.copy(errorMessage = "No se puede eliminar el evento seleccionado.")
                Log.d("Error", "No se puede eliminar el evento seleccionado.")
                return@launch
            }
            val hourDifference = getHourDifference(event.startDate!!, event.endDate!!)
            try {
                val status = hashMapOf(
                    "status" to "Canceled"
                )
                val userDifference = firestore.collection("Users").document(_homeState.value.user.userID)

                userDifference.update("hours", FieldValue.increment(hourDifference.toDouble())).await()

                firestore.collection("Events").document(event.id)
                    .update(status as Map<String, Any>).await()
                onAction(HomeInputAction.CancelEditingEvent)

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
            if(!userHaveHoursAvailable())
                return@launch
            if(_homeState.value.previsualizationHour == _homeState.value.user.hours) {
                _homeState.value.copy(errorMessage = "El evento no ha cambiado.")
                return@launch
            }
            val oldEvent = _homeState.value.editingEvent?.id
            if(oldEvent.isNullOrEmpty()) return@launch
            try{
                val user = _homeState.value.user
                val userRef = firestore.collection("Users").document(user.userID)
                userRef.update("hours", _homeState.value.previsualizationHour).await()

                firestore.collection("Events").document(oldEvent).update("startDate", homeState.value.eventStart.toDate(),
                    "endDate", homeState.value.eventEnd.toDate()).await()

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
            while (_homeState.value.user.building.isEmpty()) {
                delay(100)
            }
            if (_homeState.value.userUID.isNotEmpty() && _homeState.value.user.building.isEmpty()){
                _homeState.value = _homeState.value.copy(errorMessage = "Ocurrio un error inesperado intente más tarde")
                Log.d("error", "El usuario no tiene un edificio asignado")
                return@launch
            }
            val userBuilding = _homeState.value.user.building
            if(userBuilding.isEmpty())
            {
                Log.d("Events", "El edificio esta vacio")
                return@launch
            }
            _homeState.value = _homeState.value.copy(isLoading = true)
            Log.d("error", "Si llega")
            firestore
                .collection("Events")
                .whereEqualTo("building", userBuilding)
                .whereLessThanOrEqualTo("endDate", maxTime.toDate())
                .whereNotEqualTo("status", "Canceled")
                .snapshots()
                .collect {
                    querySnapshot ->
                    try{
                        val eventList = querySnapshot.documents.mapNotNull {
                            document ->
                            val start = document.getDate("startDate")?.toLocalDateTime()
                            val end = document.getDate("endDate")?.toLocalDateTime()
                            val status = EventStatus.valueOf(document.getString("status") ?: "Active")
                            val color = status.getColorIDByStatus()

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
                            Log.d("Events", "Eventos actualizados $eventList")
                            Log.d("error", "Llego todavía más lejos")


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
                            val userToSave: UserModel = _homeState.value.user
                            if (userToSave.userID.isEmpty() && _homeState.value.userUID.isNotEmpty()) {
                                _homeState.value = homeState.value.copy(errorMessage = "No se pudo obtener un usuario válido para guardar el evento.")
                                return@launch
                            }
                            if (userToSave.userID.isNotEmpty()) {
                                saveEvent(userToSave)
                                val reference =  firestore.collection("Users").document(userToSave.userID)
                                reference.update("hours", _homeState.value.previsualizationHour).await()
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


    private suspend fun saveEvent( user: UserModel)
    {
        try {
            val newEventRef = firestore.collection("Events").document()
             val building = _homeState.value.user.building
            if (building.isEmpty()) return
            val buildingDoc = firestore.collection("Buildings").document(building).get().await()
            if (!buildingDoc.exists()) {
                _homeState.value = _homeState.value.copy(errorMessage = "No se encontraron datos para el edificio.")
                return
            }
            val maxHoursPerWeek = buildingDoc.getDouble("maxHoursPerWeek") ?: 0.0
            var status = ""
            status = if(_homeState.value.eventStart >= LocalDateTime.now().minusHours(maxHoursPerWeek.toLong())){
                "Scheduled"
            } else{
                "Pending"
            }

            val eventId = newEventRef.id
            val data = mapOf(
                "id" to eventId,
                "userID" to user.userID,
                "startDate" to _homeState.value.eventStart.toDate(),
                "endDate" to _homeState.value.eventEnd.toDate(),
                "building" to user.building,
                "departmentN" to user.departmentN,
                "status" to status
            )
           newEventRef.set(data).await()
            Log.d("Success", "User is saved")
        }
        catch (e: Exception){
            _homeState.value = _homeState.value.copy(errorMessage = "Ocurrio un error inesperado, no se guardo el evento")
            Log.d("Error", e.toString())
        }

    }


}