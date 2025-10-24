package com.grupo1.hoppi.ui.screens.home

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.grupo1.hoppi.ui.components.mainapp.BottomNavBar
import com.grupo1.hoppi.ui.screens.mainapp.FeedScreen
import com.grupo1.hoppi.ui.screens.mainapp.NotificationScreen
import com.grupo1.hoppi.ui.screens.mainapp.SearchScreen

object MainAppDestinations {
    const val FEED_ROUTE = "main/feed"
    const val PEOPLE_ROUTE = "main/people"
    const val SEARCH_ROUTE = "main/search"
    const val CREATE_POST_ROUTE = "main/create_post"
    const val PROFILE_ROUTE = "main/profile"
    const val NOTIFICATIONS_ROUTE = "main/notifications"
}

@Composable fun ProfileScreen() { Text("Perfil") }
@Composable fun CreatePostScreen() { Text("Criar Post") }

@Composable
fun HomeScreen(rootNavController: NavHostController) {
    val bottomNavController = rememberNavController()
    val navBackStackEntry by bottomNavController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val showBottomBarAndFab = currentRoute != MainAppDestinations.SEARCH_ROUTE

    Scaffold(
        bottomBar = {
            if (showBottomBarAndFab) {
                BottomNavBar(bottomNavController)
            }
        },
        floatingActionButton = {
            if (showBottomBarAndFab) {
                FloatingActionButton(
                    onClick = {
                        rootNavController.navigate(MainAppDestinations.CREATE_POST_ROUTE)
                    },
                    containerColor = Color(0xFFEC8445),
                    shape = FloatingActionButtonDefaults.extendedFabShape,
                    modifier = Modifier.clip(CircleShape)
                ) {
                    Icon(
                        Icons.Filled.Add,
                        contentDescription = "Criar Post",
                        tint = Color.White,
                        modifier = Modifier.size(30.dp)
                    )
                }
            }
        },
        floatingActionButtonPosition = FabPosition.End
    ) { paddingValues ->

        NavHost(
            navController = bottomNavController,
            startDestination = MainAppDestinations.FEED_ROUTE,
            modifier = Modifier.padding(paddingValues)
        ) {
            composable(MainAppDestinations.FEED_ROUTE) {
                FeedScreen(
                    onPostClick = { postId ->
                        rootNavController.navigate("main/post_open/$postId")
                    },
                    onNotificationsClick = {
                        bottomNavController.navigate(MainAppDestinations.NOTIFICATIONS_ROUTE)
                    },
                )
            }

            composable(MainAppDestinations.NOTIFICATIONS_ROUTE) {
                NotificationScreen(
                    navController = bottomNavController
                )
            }
            composable(MainAppDestinations.PEOPLE_ROUTE) {
                Text("Comunidade")
            }
            composable(MainAppDestinations.SEARCH_ROUTE) {
                SearchScreen(
                    navController = bottomNavController
                )
            }
            composable(MainAppDestinations.CREATE_POST_ROUTE) {
                CreatePostScreen()
            }
            composable(MainAppDestinations.PROFILE_ROUTE) {
                ProfileScreen()
            }
        }
    }
}