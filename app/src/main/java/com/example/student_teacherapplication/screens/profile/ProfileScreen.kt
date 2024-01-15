package com.example.student_teacherapplication.screens.profile

import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.student_teacherapplication.components.ErrorComponent
import com.example.student_teacherapplication.components.ProfileImage
import com.example.student_teacherapplication.data.UserModel
import com.example.student_teacherapplication.screens.MainViewModel
import com.example.student_teacherapplication.screens.ProfileScreenState

@Composable
fun ProfileScreen(viewModel: MainViewModel, onSignOut: () -> Unit) {

    LaunchedEffect(Unit) {
        viewModel.getUser()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        when (val state = viewModel.profileScreenState) {
            is ProfileScreenState.Content -> ProfileScreenContent(
                userModel = state.user,
                onSignOut = {
                    onSignOut.invoke()
                    viewModel.signOut()
                }, updateProfilePicture = {
                    viewModel.updateProfilePicture(it)
                })

            ProfileScreenState.Error -> ErrorComponent { viewModel.getUser() }
            ProfileScreenState.Loading -> CircularProgressIndicator()
        }

    }
}

@Composable
fun ProfileScreenContent(
    userModel: UserModel,
    onSignOut: () -> Unit,
    updateProfilePicture: (Uri) -> Unit
) {
    var imageUri: Uri? by remember { mutableStateOf(null) }
    val bitmap = remember { mutableStateOf<Bitmap?>(null) }
    val context = LocalContext.current


    val launcher = rememberLauncherForActivityResult(
        contract =
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        imageUri = uri
    }

    LaunchedEffect(key1 = imageUri) {
        if (imageUri == null) return@LaunchedEffect

        if (Build.VERSION.SDK_INT < 28) {
            bitmap.value = MediaStore.Images
                .Media.getBitmap(context.contentResolver, imageUri)

        } else {
            val source = ImageDecoder
                .createSource(context.contentResolver, imageUri!!)
            bitmap.value = ImageDecoder.decodeBitmap(source)
        }
    }

    Column(
        Modifier
            .fillMaxSize()
            .padding(top = 50.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ProfileImage(
            imageBitmap = bitmap.value,
            url = userModel.pictureUrl,
            size = DpSize(width = 220.dp, height = 220.dp),
            onClick = {
                launcher.launch("image/*")
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            "Hello ${userModel.name}",
            fontSize = 40.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.surface
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                imageUri?.let(updateProfilePicture)
            },
            enabled = imageUri != null,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Green,
                contentColor = Color.Black,
                disabledContentColor = Color.Black.copy(alpha = 0.1f),
                disabledContainerColor = Color.Green.copy(alpha = 0.1f)
            )
        ) {
            Text(text = "Update Profile", color = Color.Black, fontSize = 16.sp)
        }

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = onSignOut,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Red,
                contentColor = Color.White
            )
        ) {
            Text(text = "Sign Out", color = Color.White, fontSize = 16.sp)
        }
    }
}
