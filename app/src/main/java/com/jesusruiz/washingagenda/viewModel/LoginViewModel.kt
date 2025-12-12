package com.jesusruiz.washingagenda.viewModel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
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
        data class isAdminChanged(val value: Boolean) : LoginInputAction()
    }

    fun onAction(action : LoginInputAction){
        when(action){
            is LoginInputAction.isAdminChanged -> {
                print("hola")
            }
        }
    }

    fun alternativeLogin(user: String, onSuccess: () -> Unit)
    {
        if (user == "Admin"){
            uiState.isAdmin = true
        }
        else{
            uiState.isAdmin = false
        }
        onSuccess()
    }


    fun login(user: String, password: String, onSuccess: () -> Unit)
    {
        viewModelScope.launch{
            try {
                auth.signInWithEmailAndPassword(user,password)
                    .addOnCompleteListener {
                        task ->
                        if(task.isSuccessful)
                        {

                            if(user == "admin")
                            {
                                uiState.isAdmin = true
                                Log.d("admin", "el admin inicio sesión")
                            }
                             onSuccess()
                        }
                    }
                    .addOnFailureListener {
                            Log.d("Error", "No se pudo iniciar sesión" )
                    }
            }
            catch (e : Exception)
            {

            }
        }
    }
}