package com.example.student_teacherapplication.navigation

import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.student_teacherapplication.screens.MainViewModel
import com.example.student_teacherapplication.screens.home.HomeScreen
import com.example.student_teacherapplication.screens.messages.MessagesScreen
import com.example.student_teacherapplication.screens.profile.ProfileScreen
import com.example.student_teacherapplication.screens.search.AppointmentScreen
import com.example.student_teacherapplication.screens.search.CommentsScreen
import com.example.student_teacherapplication.screens.search.SearchScreen


// ana ekranımızda bulunan bottom navigation componenti
@Composable
fun NavigationHost(
    viewModel: MainViewModel,
    navController: NavHostController,
    onSignOut: () -> Unit,
) {
    NavHost(navController, startDestination = BottomNavItem.Home.route) {
        composable(BottomNavItem.Home.route) { HomeScreen(viewModel = viewModel) }
        composable(BottomNavItem.Messages.route) { MessagesScreen() }
        composable(BottomNavItem.Search.route) {
            SearchScreen(
                viewModel = viewModel,
                navigateToAppointment = {
                    navController.navigate(MainDirection.Appointment.route)
                }
            )
        }
        composable(BottomNavItem.Profile.route) {
            ProfileScreen(viewModel = viewModel, onSignOut = onSignOut)
        }

        composable(MainDirection.Appointment.route) {
            AppointmentScreen(viewModel = viewModel, navigateToComments = {
                navController.navigate(MainDirection.Comments.route)
            })
        }

        composable(MainDirection.Comments.route) {
            CommentsScreen(viewModel = viewModel)
        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavController) {
    fun getColor(route: String): Color {
        return if (navController.currentBackStackEntry?.destination?.route.equals(route))
            Color.Blue
        else
            Color.Black
    }

    BottomNavigation(backgroundColor = Color.White, elevation = 8.dp) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        BottomNavigationItem(
            selected = currentRoute == BottomNavItem.Home.route,
            onClick = {
                navController.navigate(BottomNavItem.Home.route) {
                    popUpTo(navController.graph.startDestinationId)
                    launchSingleTop = true
                }
            },
            icon = {
                Icon(
                    BottomNavItem.Home.icon,
                    tint = getColor(BottomNavItem.Home.label),
                    contentDescription = null
                )
            },
            label = { Text(BottomNavItem.Home.label) }
        )
        BottomNavigationItem(
            selected = currentRoute == BottomNavItem.Messages.route,
            onClick = {
                navController.navigate(BottomNavItem.Messages.route) {
                    popUpTo(navController.graph.startDestinationId)
                    launchSingleTop = true
                }
            },
            icon = {
                Icon(
                    BottomNavItem.Messages.icon,
                    tint = getColor(BottomNavItem.Messages.label),
                    contentDescription = null
                )
            },
            label = { Text(BottomNavItem.Messages.label) }
        )
        BottomNavigationItem(
            selected = currentRoute == BottomNavItem.Search.route,
            onClick = {
                navController.navigate(BottomNavItem.Search.route) {
                    popUpTo(navController.graph.startDestinationId)
                    launchSingleTop = true
                }
            },
            icon = {
                Icon(
                    BottomNavItem.Search.icon,
                    tint = getColor(BottomNavItem.Search.label),
                    contentDescription = null
                )
            },
            label = { Text(BottomNavItem.Search.label) }
        )
        BottomNavigationItem(
            selected = currentRoute == BottomNavItem.Profile.route,
            onClick = {
                navController.navigate(BottomNavItem.Profile.route) {
                    popUpTo(navController.graph.startDestinationId)
                    launchSingleTop = true
                }
            },
            icon = {
                Icon(
                    BottomNavItem.Profile.icon,
                    tint = getColor(BottomNavItem.Profile.label),
                    contentDescription = null
                )
            },
            label = { Text(BottomNavItem.Profile.label) }
        )
    }

}