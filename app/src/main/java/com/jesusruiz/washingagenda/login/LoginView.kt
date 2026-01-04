package com.jesusruiz.washingagenda.login

import android.util.Log
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.jesusruiz.washingagenda.R
import com.jesusruiz.washingagenda.viewModel.LoginViewModel

@Composable
fun LoginView(navController: NavController, loginVM: LoginViewModel){
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val state = loginVM.uiState
    Column(Modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Spacer(modifier = Modifier.height(100.dp))
        Image(modifier = Modifier.height(200.dp).width(200.dp).padding(20.dp),
            painter = painterResource(id = R.drawable.washing_machine), contentDescription = "Login Icon"
        )
        OutlinedTextField(modifier = Modifier.padding(start = 30.dp, end = 30.dp).fillMaxWidth(),
            value = email,
            onValueChange = {email = it},
            label = { Text(text = "Email",
                style = LocalTextStyle.current.copy(color = Color.White)
            )},
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email ),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = colorResource(id = R.color.dark_green),
                unfocusedContainerColor = colorResource(id = R.color.dark_green),
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White)
        )
        OutlinedTextField(modifier = Modifier.padding(start = 30.dp, end = 30.dp).fillMaxWidth(),
            value = password,
            onValueChange = {password = it},
            label = { Text(text = "Password"
            )},
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password ),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = colorResource(id = R.color.dark_green),
                unfocusedContainerColor = colorResource(id = R.color.dark_green),
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White))
        Button(onClick = { loginVM.login(email, password,
            { Log.d("home", "admin")
                navController.navigate("Admin")},
            { Log.d("home", "home")
                navController.navigate("Home")})
            }, modifier = Modifier.padding(top = 30.dp, start = 30.dp, end = 30.dp).fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = colorResource(id = R.color.dark_green)
            )) {
            Text(text = "Login",
                style = LocalTextStyle.current.copy(color = Color.White)
            )

        }

    }
}