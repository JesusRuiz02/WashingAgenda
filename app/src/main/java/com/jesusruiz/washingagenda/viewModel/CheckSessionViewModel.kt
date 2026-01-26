package com.jesusruiz.washingagenda.viewModel

import android.util.Log
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
class CheckSessionViewModel @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
): ViewModel() {
    fun checkSession(isAdmin:() -> Unit, isUser: () -> Unit, notSessionInit: () -> Unit, onEmailNotVerified: () -> Unit){
        val currentUser = auth.currentUser
        if(currentUser == null)
        {
            notSessionInit()
            return
        }
        val uid = auth.currentUser?.uid
        viewModelScope.launch {
            try {
                currentUser.reload().await()
                val refreshedUser = auth.currentUser!!
                if(refreshedUser.isEmailVerified){
                    Log.d("CheckSession", "El usuario ${refreshedUser.email} ha iniciado sesión pero no ha verificado su email.")
                    onEmailNotVerified()
                    return@launch
                }
                val doc = firestore
                    .collection("Users")
                    .document(uid.toString())
                    .get()
                    .await()
                val user = doc.toObject(UserModel::class.java) ?: return@launch
                when (user.role) {
                    "admin" -> isAdmin()
                    "user"  -> isUser()
                    else    -> notSessionInit()
                }
            }
            catch (e: Exception)
            {
                Log.d("Error", e.message ?: "Error getting users")
                notSessionInit()
            }
        }

    }
}