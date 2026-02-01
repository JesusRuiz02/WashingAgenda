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
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.jesusruiz.washingagenda.R
import com.jesusruiz.washingagenda.viewModel.LoginInputAction
import com.jesusruiz.washingagenda.viewModel.LoginViewModel

@Composable
fun LoginView(navController: NavController, loginVM: LoginViewModel){
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val state = loginVM.uiState

    val focusManager = LocalFocusManager.current
    Column(Modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Spacer(modifier = Modifier.height(100.dp))
        Image(modifier = Modifier
            .height(110.dp)
            .width(110.dp)
            .padding(20.dp),
            painter = painterResource(id = R.drawable.washing_machine), contentDescription = "Login Icon"
        )
        OutlinedTextField(modifier = Modifier
            .padding(start = 30.dp, end = 30.dp, top = 10.dp)
            .fillMaxWidth(),
            value = email,
            onValueChange = {email = it},
            singleLine = true,
            keyboardActions = KeyboardActions(
                onNext = { focusManager.moveFocus(FocusDirection.Down) }
            ),
            shape = MaterialTheme.shapes.medium,
            label = { Text(text = stringResource(R.string.email_txt),)},

            keyboardOptions = KeyboardOptions(keyboardType = (KeyboardType.Email),
                    imeAction = ImeAction.Next),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = MaterialTheme.colorScheme.tertiary,
                focusedLabelColor = MaterialTheme.colorScheme.tertiary,
                unfocusedLabelColor = MaterialTheme.colorScheme.tertiary,
                unfocusedTextColor = MaterialTheme.colorScheme.tertiary,
                focusedBorderColor = MaterialTheme.colorScheme.secondary)
        )
        OutlinedTextField(modifier = Modifier
            .padding(start = 30.dp, end = 30.dp)
            .fillMaxWidth(),
            value = password,
            shape = MaterialTheme.shapes.medium,
            onValueChange = {password = it},
            label = { Text(text = stringResource(R.string.password_txt))},
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password ,
                    imeAction = ImeAction.Done),
            visualTransformation = if(state.canSeePassword) {
                VisualTransformation.None
            }
            else{
                PasswordVisualTransformation()
            },
            singleLine = true,
            trailingIcon = {
                IconButton(onClick = { loginVM.onAction(LoginInputAction.CanSeePasswordToggle)}){
                    if (state.canSeePassword){
                    Icon(
                        painter = painterResource(R.drawable.show_password),
                        contentDescription = "Go back",
                        tint = MaterialTheme.colorScheme.secondary)}
                    else{
                        Icon( painter = painterResource(R.drawable.disable_password_view),
                            contentDescription = "Go back",
                            tint = MaterialTheme.colorScheme.secondary)
                    }
                }
                },
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = MaterialTheme.colorScheme.tertiary,
                focusedLabelColor = MaterialTheme.colorScheme.tertiary,
                unfocusedLabelColor = MaterialTheme.colorScheme.tertiary,
                unfocusedTextColor = MaterialTheme.colorScheme.tertiary,
                focusedBorderColor = MaterialTheme.colorScheme.secondary))
        Button(onClick = { loginVM.login(email, password,
            { Log.d("home", "admin")
                navController.navigate("Admin")},
            { Log.d("home", "home")
                navController.navigate("Home")})
            }, modifier = Modifier
            .padding(top = 30.dp, start = 30.dp, end = 30.dp)
            .fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.secondary
            )) {
            Text(text = stringResource(R.string.login_txt),
                style = LocalTextStyle.current.copy(color = Color.White)
            )

        }

    }
}