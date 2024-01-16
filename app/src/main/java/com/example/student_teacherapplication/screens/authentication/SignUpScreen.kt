package com.example.student_teacherapplication.screens.authentication

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.student_teacherapplication.R
import com.example.student_teacherapplication.components.TextPasswordField
import com.example.student_teacherapplication.screens.MainViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpScreen(viewModel: MainViewModel, navigateToHomeScreen: () -> Unit) {
    var name by remember { mutableStateOf("") }
    var lastname by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.secondary)
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "Logo",
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .scale(0.8f)
        )

        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Name") },
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Next
            ),
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        )

        TextField(
            value = lastname,
            onValueChange = { lastname = it },
            label = { Text("Lastname") },
            keyboardOptions = KeyboardOptions.Default.copy(

                imeAction = ImeAction.Next
            ),
            singleLine = true,

            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        )

        TextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("E-mail") },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            ),
            singleLine = true,

            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        )

        TextPasswordField(password = password, onValueChange = { password = it })

        Spacer(modifier = Modifier.height(8.dp))

        TextPasswordField(password = confirmPassword, onValueChange = { confirmPassword = it })

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                viewModel.registerUser(email, password, name, lastname, onComplete = navigateToHomeScreen)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Register", fontSize = 16.sp)
        }
    }
}


