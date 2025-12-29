package com.jesusruiz.washingagenda.viewModel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldPath
import kotlinx.coroutines.tasks.await
import com.google.firebase.firestore.FirebaseFirestore
import com.jesusruiz.washingagenda.models.UserModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch


import javax.inject.Inject

@HiltViewModel
class AdminViewModel @Inject constructor(
    private val auth: FirebaseAuth,
   private val firestore: FirebaseFirestore
) : ViewModel() {

    data class AdminUiState(
        val isLoading : Boolean = false,
        var users: List<UserModel> = emptyList(),
        var editUser: UserModel = UserModel(),
        var adminBuildings : Map<String, String> = emptyMap()
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


    fun getUserById(idUser: String){
        viewModelScope.launch{
            try{
                val doc = firestore
                    .collection("Users")
                    .document(idUser)
                    .get()
                    .await()
                val user = doc.toObject(UserModel::class.java)
                if(user != null)
                {
                    adminState = adminState.copy(editUser = user)
                }


                Log.d("user id", idUser)
            }
            catch (e: Exception)
            {
                Log.e("Error", e.message ?: "Error getting users")
            }
    }
        }


    fun getUsers(){
        val uid = auth.currentUser?.uid
        adminState = adminState.copy(isLoading = true)
        viewModelScope.launch{
            try{
                val doc = firestore
                    .collection("Users")
                    .document(uid.toString())
                    .get()
                    .await()
                val user = doc.toObject(UserModel::class.java) ?: return@launch
                val adminBuildings = user.adminBuilding
                if (adminBuildings.isEmpty()) return@launch
                getUsersByBuilding(adminBuildings)
                loadBuildingNames(adminBuildings)

            }
            catch (e: Exception)
            {
                Log.e("Error", e.message ?: "Error getting users")
            }
        }


    }

    suspend fun loadBuildingNames(adminBuildings: List<String>) {
        val buildingMaps = hashMapOf<String, String>()
            try {
                adminBuildings.chunked(10).forEach { chunk ->
                    val query = firestore
                        .collection("Building")
                        .whereIn(FieldPath.documentId(), chunk)
                        .get()
                        .await()

                   query.documents.forEach { doc ->
                       val id = doc.id
                       val name = doc.getString("name") ?: "Sin nombre"
                       buildingMaps[id] = name
                   }
                    adminState = adminState.copy(adminBuildings = buildingMaps)

                }
            } catch (e: Exception)
            {

            }
        finally {
            adminState = adminState.copy(isLoading = false)
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