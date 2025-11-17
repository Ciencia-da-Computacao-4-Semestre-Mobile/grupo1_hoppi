package com.grupo1.hoppi

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.core.view.WindowCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.grupo1.hoppi.ui.components.mainapp.BottomNavBar
import com.grupo1.hoppi.ui.screens.home.HomeScreen
import com.grupo1.hoppi.ui.screens.home.MainAppDestinations
import com.grupo1.hoppi.ui.screens.home.PostsViewModel
import com.grupo1.hoppi.ui.screens.home.UserViewModel
import com.grupo1.hoppi.ui.screens.home.UserViewModelFactory
import com.grupo1.hoppi.ui.screens.login.LoginScreen
import com.grupo1.hoppi.ui.screens.signup.SignUpFlow
import com.grupo1.hoppi.ui.screens.login.forgotpassword.ForgotPasswordFlow
import com.grupo1.hoppi.ui.screens.mainapp.*
import com.grupo1.hoppi.ui.screens.mainapp.settings.SettingsNavGraph
import com.grupo1.hoppi.ui.theme.HoppiTheme
import com.grupo1.hoppi.ui.util.SetStatusBarIcons

object Destinations {
    const val LOGIN_ROUTE = "login"
    const val SIGNUP_ROUTE = "signup"
    const val FORGOT_PASSWORD_ROUTE = "forgot_password"
    const val MAIN_APP = "main_app"
    const val SETTINGS_FLOW = "settings_flow"
}

@Composable
fun HoppiApp() {
    SetStatusBarIcons()
    val rootNavController = rememberNavController()
    val bottomNavController = rememberNavController()

    NavHost(
        navController = rootNavController,
        startDestination = Destinations.LOGIN_ROUTE
    ) {
        composable(Destinations.LOGIN_ROUTE) {
            LoginScreen(
                onSignUpClick = { rootNavController.navigate(Destinations.SIGNUP_ROUTE) },
                onForgotPasswordClick = { rootNavController.navigate(Destinations.FORGOT_PASSWORD_ROUTE) },
                onLoginSuccess = {
                    rootNavController.navigate(Destinations.MAIN_APP) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }

        composable(Destinations.SIGNUP_ROUTE) {
            SignUpFlow(
                onLoginClick = { rootNavController.popBackStack() },
                onFinish = {
                    rootNavController.navigate(Destinations.MAIN_APP) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }

        composable(Destinations.FORGOT_PASSWORD_ROUTE) {
            ForgotPasswordFlow(onBackToLogin = { rootNavController.popBackStack() })
        }

        composable(Destinations.MAIN_APP) {
            MainApp(
                rootNavController = rootNavController,
                bottomNavController = bottomNavController
            )
        }

        composable("${Destinations.SETTINGS_FLOW}/{pass}") {
            SettingsNavGraph(
                rootNavController = rootNavController,
                onLogout = {
                    rootNavController.navigate(Destinations.LOGIN_ROUTE) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }
    }
}

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_prefs")

@Composable
fun MainApp(
    rootNavController: NavHostController,
    bottomNavController: NavHostController
) {
    val postsViewModel: PostsViewModel = androidx.lifecycle.viewmodel.compose.viewModel()

    val context = LocalContext.current
    val userViewModel: UserViewModel = viewModel(
        factory = UserViewModelFactory(context.dataStore)
    )

    val currentDestination = bottomNavController.currentBackStackEntryFlow.collectAsState(initial = null).value?.destination?.route

    val hideBottomBar = currentDestination == MainAppDestinations.SEARCH_ROUTE ||
            currentDestination == MainAppDestinations.CREATE_POST_ROUTE  ||
            currentDestination?.startsWith(MainAppDestinations.CREATE_POST_COMMUNITY_ROUTE) == true
    val hideFab = currentDestination == MainAppDestinations.SEARCH_ROUTE ||
            currentDestination == MainAppDestinations.NOTIFICATIONS_ROUTE ||
            currentDestination == MainAppDestinations.COMMUNITY_ROUTE ||
            currentDestination == MainAppDestinations.CREATE_POST_ROUTE ||
            currentDestination == MainAppDestinations.POST_OPEN_ROUTE ||
            currentDestination == MainAppDestinations.COMMUNITY_DETAIL_ROUTE  ||
            currentDestination == MainAppDestinations.CREATE_COMMUNITY_ROUTE  ||
            currentDestination == MainAppDestinations.EDIT_COMMUNITY_ROUTE  ||
            currentDestination?.startsWith(MainAppDestinations.CREATE_POST_COMMUNITY_ROUTE) == true

    Scaffold(
        bottomBar = {
            if (!hideBottomBar) {
                BottomNavBar(
                    bottomNavController = bottomNavController,
                    rootNavController = rootNavController
                )
            }
        },
        floatingActionButton = {
            if (!hideFab) {
                FloatingActionButton(
                    onClick = { bottomNavController.navigate(MainAppDestinations.CREATE_POST_ROUTE) },
                    shape = CircleShape,
                    containerColor = Color(0xFFEC8445)
                ) {
                    Icon(Icons.Filled.Add, contentDescription = "Criar Post", tint = Color.White)
                }
            }
        },
        floatingActionButtonPosition = FabPosition.End
    ) { padding ->
        NavHost(
            navController = bottomNavController,
            startDestination = MainAppDestinations.FEED_ROUTE,
            modifier = Modifier.padding(padding)
        ) {

            composable(MainAppDestinations.PROFILE_ROUTE) {
                ProfileScreen(
                    navController = bottomNavController,
                    postsViewModel = postsViewModel,
                    userViewModel = userViewModel,
                    onPostClick = { postId -> bottomNavController.navigate("main/post_open/$postId") },
                    onSettingsClick = {
                        rootNavController.navigate(Destinations.SETTINGS_FLOW + "/pass")
                    }
                )
            }

            composable(MainAppDestinations.CREATE_POST_ROUTE) {
                CreatePostScreen(
                    navController = bottomNavController,
                    userViewModel = userViewModel,
                    postsViewModel = postsViewModel
                )
            }

            composable(MainAppDestinations.FEED_ROUTE) {
                HomeScreen(
                    bottomNavController = bottomNavController,
                    postsViewModel = postsViewModel
                )
            }

            composable(MainAppDestinations.COMMUNITY_ROUTE) {
                CommunitiesScreen(navController = bottomNavController)
            }

            composable(MainAppDestinations.CREATE_COMMUNITY_ROUTE) {
                CreateCommunityScreen(navController = bottomNavController)
            }

            composable(MainAppDestinations.COMMUNITY_DETAIL_ROUTE,
                arguments = listOf(navArgument("communityId") { type = NavType.IntType })
            ) { backStackEntry ->
                val communityId = backStackEntry.arguments?.getInt("communityId") ?: -1
                CommunityDetailScreen(
                    navController = bottomNavController,
                    communityId = communityId,
                    userViewModel = userViewModel,
                    postsViewModel = postsViewModel
                )
            }

            composable(
                "${MainAppDestinations.CREATE_POST_COMMUNITY_ROUTE}/{communityId}",
                arguments = listOf(navArgument("communityId") { type = NavType.IntType })
            ) { backStackEntry ->
                val communityId = backStackEntry.arguments?.getInt("communityId") ?: -1

                CreatePostCommunityScreen(
                    navController = bottomNavController,
                    userViewModel = userViewModel,
                    postsViewModel = postsViewModel,
                    communityId = communityId
                )
            }

            composable(
                "${MainAppDestinations.EDIT_COMMUNITY_ROUTE}/{communityId}",
                arguments = listOf(navArgument("communityId") { type = NavType.IntType })
            ) { backStackEntry ->
                val communityId = backStackEntry.arguments?.getInt("communityId") ?: 0
                EditCommunityScreen(
                    navController = bottomNavController,
                    communityId = communityId
                )
            }

            composable(MainAppDestinations.SEARCH_ROUTE) {
                SearchScreen(navController = bottomNavController)
            }

            composable(MainAppDestinations.POST_OPEN_ROUTE) { backStackEntry ->
                val postId = backStackEntry.arguments?.getString("postId")?.toIntOrNull()
                PostScreen(
                    postId = postId,
                    navController = bottomNavController,
                    userViewModel = userViewModel,
                    postsViewModel = postsViewModel
                )
            }

            composable(MainAppDestinations.NOTIFICATIONS_ROUTE) {
                NotificationScreen(navController = bottomNavController)
            }
        }
    }
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(android.graphics.Color.TRANSPARENT),
            navigationBarStyle = SystemBarStyle.dark(android.graphics.Color.TRANSPARENT)
        )

        setContent {
            HoppiTheme(dynamicColor = false) {
                HoppiApp()
            }
        }
    }
}