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
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) : ViewModel() {

    data class LoginUiState(
        var model: String = "",
        var isAdmin: Boolean = false,
        var isLoading: Boolean = false
    )


    var uiState by mutableStateOf(LoginUiState())
        private set

    sealed class LoginInputAction{
        data class IsAdminChanged(val value: Boolean) : LoginInputAction()
    }

    fun onAction(action : LoginInputAction){
        when(action){
            is LoginInputAction.IsAdminChanged -> {
                print("hola")
            }
        }
    }


    fun login(user: String, password: String, isAdmin: () -> Unit, isUser: () -> Unit)
    {
        viewModelScope.launch{
            try {
                auth.signInWithEmailAndPassword(user,password).await()
                           val role = getRole()
                           if(role == "admin")
                           {
                               isAdmin()
                           }
                           else
                           {
                               isUser()
                           }

            }
            catch (e : Exception)
            {
                Log.e("Login" , "Error", e)
            }
        }
    }

    private suspend fun getRole() : String?{
        val uid = auth.currentUser?.uid
        val doc = firestore
            .collection("Users")
            .document(uid.toString())
            .get()
            .await()
        val user = doc.toObject(UserModel::class.java)
        return (user?.role)
    }
}