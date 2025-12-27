package com.jesusruiz.washingagenda.viewModel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.jesusruiz.washingagenda.models.UserModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class AdminViewModel @Inject constructor(
    private val auth: FirebaseAuth,
   private val firestore: FirebaseFirestore
) : ViewModel() {

    data class AdminUiState(
        var users: List<UserModel> = emptyList(),
        var editUser: UserModel = UserModel()
    )

    var adminState by mutableStateOf(AdminUiState())
        private set

    sealed class AdminInputAction {
        data class UsersChanged(val value: List<UserModel>) : AdminInputAction()
        data class NameChanged(val value: String) : AdminInputAction()
        data class DepartmentChanged(val value: String) : AdminInputAction()
        data class HoursChanged(val value: Int) : AdminInputAction()
        data class BuildingChanged(val value: String) : AdminInputAction()


    }

    fun onAction(action: AdminInputAction) {
        when (action) {
            is AdminInputAction.UsersChanged -> {
                adminState = adminState.copy(users = action.value)
            }
            is AdminInputAction.NameChanged -> {
                adminState = adminState.copy(
                    editUser = adminState.editUser.copy(name = action.value)
                )
            }
            is AdminInputAction.DepartmentChanged -> {
                adminState = adminState.copy(
                    editUser = adminState.editUser.copy(departmentN = action.value)
                )
            }
            is AdminInputAction.HoursChanged -> {
                adminState = adminState.copy(
                    editUser = adminState.editUser.copy(hours = action.value)
                )
            }
            is AdminInputAction.BuildingChanged -> {
                adminState = adminState.copy(
                    editUser = adminState.editUser.copy(building = action.value)
                )
            }

        }
    }

    fun editUser(idUser: String){

    }

    fun getUserById(idUser: String){
        viewModelScope.launch{
            try{
                val doc = firestore
                    .collection("Users")
                    .document(idUser)
                    .get()
                    .await()
                adminState.editUser = doc.toObject(UserModel::class.java) ?: return@launch
            }
            catch (e: Exception)
            {
                Log.e("Error", e.message ?: "Error getting users")
            }
    }
        }


    fun getUsers(){
        val uid = auth.currentUser?.uid
        viewModelScope.launch{
            try{
                val doc = firestore
                    .collection("Users")
                    .document(uid.toString())
                    .get()
                    .await()
                val user = doc.toObject(UserModel::class.java) ?: return@launch
                val adminBuildings = user.adminBuilding.orEmpty()
                Log.d("users", user.adminBuilding.toString())
                if (adminBuildings.isEmpty()) return@launch
                getUsersByBuilding(adminBuildings)
            }
            catch (e: Exception)
            {
                Log.e("Error", e.message ?: "Error getting users")
            }
        }


    }

    fun logOut(){
        auth.signOut()
    }

    private suspend fun getUsersByBuilding(buildingsIds: List<String>){
        val users = mutableListOf<UserModel>()
        buildingsIds.chunked(10).forEach {
            chunk ->
            val query = firestore
                .collection("Users")
                .whereIn("building", chunk)
                .get()
                .await()
            users += query.toObjects(UserModel::class.java)
        }

        adminState = adminState.copy(users = users)
    }
}