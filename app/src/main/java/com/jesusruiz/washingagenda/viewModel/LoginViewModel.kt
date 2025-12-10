package com.jesusruiz.washingagenda.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import kotlinx.coroutines.launch
import java.lang.Exception

class LoginViewModel: ViewModel() {

    private val auth: FirebaseAuth = Firebase.auth

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