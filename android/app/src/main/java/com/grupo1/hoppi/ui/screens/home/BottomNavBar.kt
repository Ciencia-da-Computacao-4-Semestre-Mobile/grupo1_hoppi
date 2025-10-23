package com.grupo1.hoppi.ui.components.mainapp

import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.grupo1.hoppi.ui.screens.home.MainAppDestinations

data class BottomNavItem(
    val route: String,
    val icon: ImageVector,
    val contentDescription: String
)

val items = listOf(
    BottomNavItem(MainAppDestinations.FEED_ROUTE, Icons.Default.Home, "Feed"),
    BottomNavItem(MainAppDestinations.PEOPLE_ROUTE, Icons.Default.People, "Comunidade"),
    BottomNavItem(MainAppDestinations.SEARCH_ROUTE, Icons.Default.Search, "Busca")
)

@Composable
fun BottomNavBar(navController: NavController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    NavigationBar(
        containerColor = Color.White
    ) {
        // Divider(color = Color.Black.copy(alpha = 1.0f), thickness = 1.dp)
        items.forEach { item ->
            val isSelected = currentRoute == item.route

            NavigationBarItem(
                icon = {
                    Icon(
                        item.icon,
                        contentDescription = item.contentDescription,
                        tint = if (isSelected) Color(0xFFEC8445) else Color.Black,
                        modifier = Modifier.size(35.dp, 35.dp)
                    )
                },
                selected = isSelected,
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = Color.White
                )
            )
        }
    }
}