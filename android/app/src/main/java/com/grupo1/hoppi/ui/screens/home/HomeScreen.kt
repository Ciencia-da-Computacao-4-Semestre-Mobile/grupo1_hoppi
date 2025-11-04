package com.grupo1.hoppi.ui.screens.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.grupo1.hoppi.Destinations
import com.grupo1.hoppi.ui.components.mainapp.BottomNavBar
import com.grupo1.hoppi.ui.screens.mainapp.*

object MainAppDestinations {
    const val FEED_ROUTE = "main/feed"
    const val COMMUNITY_ROUTE = "main/communities"
    const val CREATE_COMMUNITY_ROUTE = "main/create_community"
    const val COMMUNITY_DETAIL_ROUTE = "main/community_detail/{communityName}"
    const val SEARCH_ROUTE = "main/search"
    const val CREATE_POST_ROUTE = "main/create_post"
    const val PROFILE_ROUTE = "main/profile"
    const val NOTIFICATIONS_ROUTE = "main/notifications"
    const val POST_OPEN_ROUTE = "main/post_open/{postId}"
}

@Composable
fun HomeScreen(rootNavController: NavHostController) {
    val bottomNavController = rememberNavController()
    val navBackStackEntry by bottomNavController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val systemUiController = rememberSystemUiController()
    val useDarkIcons = false

    val showBottomBar = currentRoute != MainAppDestinations.SEARCH_ROUTE &&
            currentRoute != MainAppDestinations.CREATE_COMMUNITY_ROUTE
    val showFab = currentRoute != MainAppDestinations.SEARCH_ROUTE &&
            currentRoute != MainAppDestinations.NOTIFICATIONS_ROUTE &&
            currentRoute != MainAppDestinations.COMMUNITY_ROUTE &&
            currentRoute != MainAppDestinations.CREATE_COMMUNITY_ROUTE

    LaunchedEffect(navBackStackEntry) {
        when (currentRoute) {
            MainAppDestinations.FEED_ROUTE,
            MainAppDestinations.PROFILE_ROUTE,
            MainAppDestinations.COMMUNITY_ROUTE,
            MainAppDestinations.SEARCH_ROUTE -> {
                systemUiController.setStatusBarColor(color = Pink, darkIcons = useDarkIcons)
            }
            MainAppDestinations.NOTIFICATIONS_ROUTE -> {
                systemUiController.setStatusBarColor(color = Pink, darkIcons = useDarkIcons)
            }
            MainAppDestinations.CREATE_POST_ROUTE -> {
                systemUiController.setStatusBarColor(color = Pink, darkIcons = useDarkIcons)
            }
            else -> {
                systemUiController.setStatusBarColor(color = Color.White, darkIcons = true)
            }
        }
    }

    Scaffold(
        bottomBar = {
            if (showBottomBar) BottomNavBar(bottomNavController)
        },
        floatingActionButton = {
            if (showFab) {
                FloatingActionButton(
                    onClick = { rootNavController.navigate(MainAppDestinations.CREATE_POST_ROUTE) },
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
        Box(modifier = Modifier.padding(paddingValues)) {
            NavHost(
                navController = bottomNavController,
                startDestination = MainAppDestinations.FEED_ROUTE
            ) {
                composable(MainAppDestinations.FEED_ROUTE) {
                    FeedScreen(
                        onPostClick = { postId ->
                            rootNavController.navigate("main/post_open/$postId")
                        },
                        onNotificationsClick = {
                            rootNavController.navigate("main/notifications")
                        },
                        onProfileClick = {
                            bottomNavController.navigate(MainAppDestinations.PROFILE_ROUTE)
                        }
                    )
                }

                composable(MainAppDestinations.NOTIFICATIONS_ROUTE) {
                    NotificationScreen(navController = bottomNavController)
                }

                composable(MainAppDestinations.PROFILE_ROUTE) {
                    ProfileScreen(
                        navController = bottomNavController,
                        onPostClick = { postId ->
                            rootNavController.navigate("main/post_open/$postId")
                        },
                        onSettingsClick = {
                            rootNavController.navigate(Destinations.SETTINGS_ROUTE)
                        }
                    )
                }

                composable(MainAppDestinations.COMMUNITY_ROUTE) {
                    CommunitiesScreen(navController = bottomNavController)
                }

                composable(MainAppDestinations.CREATE_COMMUNITY_ROUTE) {
                    CreateCommunityScreen(navController = bottomNavController)
                }

                composable(
                    MainAppDestinations.COMMUNITY_DETAIL_ROUTE,
                    arguments = listOf(navArgument("communityName") { type = NavType.StringType })
                ) { backStackEntry ->
                    val communityName = backStackEntry.arguments?.getString("communityName") ?: ""

                    CommunityDetailScreen(
                        navController = bottomNavController,
                        communityId = communityName
                    )
                }

                composable(MainAppDestinations.SEARCH_ROUTE) {
                    SearchScreen(navController = bottomNavController)
                }

                composable(MainAppDestinations.CREATE_POST_ROUTE) {
                    CreatePostScreen(navController = bottomNavController)
                }

                composable(MainAppDestinations.POST_OPEN_ROUTE) { backStackEntry ->
                    val postId = backStackEntry.arguments?.getString("postId")?.toIntOrNull()
                    PostScreen(postId = postId, navController = bottomNavController)
                }
            }
        }
    }
}
