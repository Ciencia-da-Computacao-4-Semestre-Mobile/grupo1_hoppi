package com.grupo1.hoppi.ui.components.mainapp

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState

data class BottomNavItem(
    val route: String,
    val icon: ImageVector,
    val contentDescription: String
)

private val items = listOf(
    BottomNavItem("main/feed", Icons.Default.Home, "Home"),
    BottomNavItem("main/communities", Icons.Default.People, "Comunidades"),
    BottomNavItem("main/search", Icons.Default.Search, "Buscar"),
)

@Composable
fun BottomNavBar(
    bottomNavController: NavController
) {
    val navBackStackEntry by bottomNavController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    NavigationBar(
        containerColor = Color.White
    ) {
        items.forEach { item ->
            val isSelected = currentRoute == item.route

            NavigationBarItem(
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.contentDescription,
                        tint = if (isSelected) Color(0xFFEC8445) else Color.Black,
                        modifier = Modifier.size(28.dp)
                    )
                },
                selected = isSelected,
                onClick = {
                    if (currentRoute != item.route) {

                        if (item.route == "main/communities") {
                            bottomNavController.navigate(item.route) {
                                popUpTo("main/communities") { inclusive = true }
                                launchSingleTop = true
                            }

                        } else {
                            bottomNavController.navigate(item.route) {
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    }
                },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = Color.White
                )
            )
        }
    }
}