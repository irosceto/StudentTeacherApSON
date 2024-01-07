package com.example.student_teacherapplication.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.graphics.vector.ImageVector

// ana ekranımızda bulunan bottom navigation itemları
sealed class BottomNavItem(val route: String, val icon: ImageVector, val label: String) {
    object Home : BottomNavItem("home", Icons.Default.Home, "Home")
    object Search : BottomNavItem("search", Icons.Default.Search, "Search")
    object Messages: BottomNavItem("messages", Icons.Default.MailOutline, "Messages")
    object Profile : BottomNavItem("profile", Icons.Default.Person, "Profile")
}
