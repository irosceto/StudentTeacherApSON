package com.example.student_teacherapplication.screens.main

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.student_teacherapplication.navigation.BottomNavItem
import com.example.student_teacherapplication.navigation.BottomNavigationBar
import com.example.student_teacherapplication.navigation.MainDirection
import com.example.student_teacherapplication.navigation.NavigationHost
import com.example.student_teacherapplication.screens.MainViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    viewModel: MainViewModel,
    onSignOut: () -> Unit
) {
    val navController = rememberNavController()
    var title by remember { mutableStateOf("") }
    var canPop by remember { mutableStateOf(false) }


    DisposableEffect(navController) {
        val listener = NavController.OnDestinationChangedListener { controller, _, _ ->
            val isOnTopScreen = when (navController.currentDestination?.route) {
                BottomNavItem.Home.route -> true
                BottomNavItem.Messages.route -> true
                BottomNavItem.Search.route -> true
                BottomNavItem.Profile.route -> true
                else -> false
            }

            canPop = controller.previousBackStackEntry != null && !isOnTopScreen
        }
        navController.addOnDestinationChangedListener(listener)
        onDispose {
            navController.removeOnDestinationChangedListener(listener)
        }
    }

    // bulunan ekran routuna  göre appbar daki title güncellenir
    LaunchedEffect(navController.currentBackStackEntryFlow) {
        navController.currentBackStackEntryFlow.collect {
            title = when (it.destination.route) {
                BottomNavItem.Home.route -> "Appointments"
                BottomNavItem.Messages.route -> "Messages"
                BottomNavItem.Search.route -> "Search"
                BottomNavItem.Profile.route -> "Profile"
                MainDirection.Appointment.route -> "Appointment"
                MainDirection.Comments.route -> "Comments"
                else -> ""
            }
        }
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = title,
                        style = TextStyle(
                            color = MaterialTheme.colorScheme.secondary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 24.sp
                        )
                    )
                },
                navigationIcon = {
                    if (canPop) {
                        IconButton(onClick = { navController.navigateUp() }) {
                            Icon(
                                imageVector = Icons.Filled.ArrowBack,
                                contentDescription = "Back"
                            )
                        }
                    }

                }
            )
        },
        bottomBar = { BottomNavigationBar(navController = navController) },
        content = {
            Box(
                Modifier
                    .padding(top = it.calculateTopPadding(), bottom = it.calculateBottomPadding())
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.secondary)
            ) {
                NavigationHost(viewModel, navController, onSignOut = onSignOut)
            }
        })

}