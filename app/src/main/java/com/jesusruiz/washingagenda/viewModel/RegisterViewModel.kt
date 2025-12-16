package com.jesusruiz.washingagenda.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.jesusruiz.washingagenda.models.UserModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) : ViewModel() {
    fun addUser(name: String, email: String, password: String, department: String, building: String, onSuccess: () -> Unit ){
         viewModelScope.launch {
             try {
                 val result = auth.createUserWithEmailAndPassword(email, password).await()
                 if(result.user != null)
                 {
                     saveUser(name, department, building)
                     onSuccess()
                     Log.d("Success","The user was added")
                 }
             }
             catch (e: Exception)
             {
                 Log.d("Error", "The user could not be added ${e.message}")
             }
         }
    }

    private suspend fun saveUser(name: String, department: String, building: String)
    {
        val id = auth.currentUser?.uid ?: return
        val email = auth.currentUser?.email ?: return

                val user = UserModel(userID = id.toString(),
                    email = email,
                    name = name,
                    building = building,
                    departmentN = department,
                    hours = 10,
                    userType = "host",
                    adminBuildingsIds = emptyList())
                    firestore
                    .collection("Users")
                    .document(id)
                    .set(user)
                    .await()
                Log.d("Success", "User is saved")
    }


}