package com.grupo1.hoppi

import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
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
import com.grupo1.hoppi.ui.screens.home.LikesViewModel
import com.grupo1.hoppi.ui.screens.home.MainAppDestinations
import com.grupo1.hoppi.ui.screens.home.PostsViewModel
import com.grupo1.hoppi.ui.screens.home.UsersViewModel
import com.grupo1.hoppi.ui.screens.home.UserViewModelFactory
import com.grupo1.hoppi.ui.screens.login.LoginScreen
import com.grupo1.hoppi.ui.screens.login.auth.LoginViewModel
import com.grupo1.hoppi.ui.screens.signup.SignUpFlow
import com.grupo1.hoppi.ui.screens.login.forgotpassword.ForgotPasswordFlow
import com.grupo1.hoppi.ui.screens.mainapp.*
import com.grupo1.hoppi.ui.screens.mainapp.settings.SettingsNavGraph
import com.grupo1.hoppi.ui.screens.settings.HoppiOrange
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
    val context = LocalContext.current
    val userViewModel: UsersViewModel = viewModel(
        factory = UserViewModelFactory(context.dataStore)
    )

    NavHost(
        navController = rootNavController,
        startDestination = Destinations.LOGIN_ROUTE
    ) {
        composable(Destinations.LOGIN_ROUTE) {
            LoginScreen(
                userViewModel = userViewModel,
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
                bottomNavController = bottomNavController,
                userViewModel = userViewModel
            )
        }

        composable(Destinations.SETTINGS_FLOW) {
            SettingsNavGraph(
                rootNavController = rootNavController,
                onLogout = {
                    rootNavController.navigate(Destinations.LOGIN_ROUTE) {
                        popUpTo(0) { inclusive = true }
                    }
                },
                usersViewModel = userViewModel
            )
        }
    }
}

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_prefs")

@Composable
fun MainApp(
    rootNavController: NavHostController,
    bottomNavController: NavHostController,
    userViewModel: UsersViewModel
) {
    val postsViewModel: PostsViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
    val likesViewModel: LikesViewModel = androidx.lifecycle.viewmodel.compose.viewModel()

    val context = LocalContext.current

    val currentDestination = bottomNavController.currentBackStackEntryFlow.collectAsState(initial = null).value?.destination?.route

    val hideBottomBar = currentDestination == MainAppDestinations.SEARCH_ROUTE ||
            currentDestination == MainAppDestinations.CREATE_POST_ROUTE  ||
            currentDestination?.startsWith(MainAppDestinations.CREATE_POST_COMMUNITY_ROUTE) == true ||
            currentDestination == "${MainAppDestinations.EDIT_COMMUNITY_ROUTE}/{communityId}" ||
            currentDestination == MainAppDestinations.CREATE_COMMUNITY_ROUTE
    val hideFab = currentDestination == MainAppDestinations.SEARCH_ROUTE ||
            currentDestination == MainAppDestinations.NOTIFICATIONS_ROUTE ||
            currentDestination == MainAppDestinations.COMMUNITY_ROUTE ||
            currentDestination == MainAppDestinations.CREATE_POST_ROUTE ||
            currentDestination == MainAppDestinations.POST_OPEN_ROUTE ||
            currentDestination == MainAppDestinations.COMMUNITY_DETAIL_ROUTE  ||
            currentDestination == MainAppDestinations.CREATE_COMMUNITY_ROUTE  ||
            currentDestination == "${MainAppDestinations.EDIT_COMMUNITY_ROUTE}/{communityId}"  ||
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
    ) { padding ->
        NavHost(
            navController = bottomNavController,
            startDestination = MainAppDestinations.FEED_ROUTE,
            modifier = Modifier.padding(padding)
        ) {

            composable(
                route = "${MainAppDestinations.PROFILE_ROUTE}/{userId}",
                arguments = listOf(navArgument("userId") { type = NavType.StringType })
            ) { backStackEntry ->
                val userId = backStackEntry.arguments?.getString("userId") ?: ""
                val token = userViewModel.token.collectAsState(initial = "").value ?: ""

                if (token.isNotEmpty()) {
                    ProfileScreen(
                        navController = bottomNavController,
                        postsViewModel = postsViewModel,
                        userViewModel = userViewModel,
                        userId = userId,
                        likesViewModel = likesViewModel,
                        onPostClick = { postId ->
                            bottomNavController.navigate("main/post_open/$postId")
                        },
                        onSettingsClick = {
                            rootNavController.navigate(Destinations.SETTINGS_FLOW)
                        },
                        token = token
                    )
                } else {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = HoppiOrange)
                    }
                }
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
                    postsViewModel = postsViewModel,
                    userViewModel = userViewModel
                )
            }

            composable(MainAppDestinations.COMMUNITY_ROUTE) {
                CommunitiesScreen(navController = bottomNavController)
            }

            composable(MainAppDestinations.CREATE_COMMUNITY_ROUTE) {
                CreateCommunityScreen(
                    navController = bottomNavController,
                    usersViewModel = userViewModel
                    )
            }

            composable(MainAppDestinations.COMMUNITY_DETAIL_ROUTE,
                arguments = listOf(navArgument("communityId") { type = NavType.StringType })
            ) { backStackEntry ->
                val communityId = backStackEntry.arguments?.getString("communityId") ?: ""
                CommunityDetailScreen(
                    navController = bottomNavController,
                    communityId = communityId,
                    userViewModel = userViewModel,
                    postsViewModel = postsViewModel,
                    communitiesViewModel = viewModel(),
                    likesViewModel = likesViewModel
                )
            }

            composable(
                "${MainAppDestinations.CREATE_POST_COMMUNITY_ROUTE}/{communityId}",
                arguments = listOf(navArgument("communityId") { type = NavType.StringType })
            ) { backStackEntry ->
                val communityId = backStackEntry.arguments?.getString("communityId") ?: ""

                CreatePostCommunityScreen(
                    navController = bottomNavController,
                    userViewModel = userViewModel,
                    postsViewModel = postsViewModel,
                    communityId = communityId
                )
            }

            composable(
                "${MainAppDestinations.EDIT_COMMUNITY_ROUTE}/{communityId}",
                arguments = listOf(navArgument("communityId") { type = NavType.StringType })
            ) { backStackEntry ->
                val communityId = backStackEntry.arguments?.getString("communityId") ?: ""
                EditCommunityScreen(
                    navController = bottomNavController,
                    communityId = communityId,
                    usersViewModel = userViewModel,
                    communitiesViewModel = viewModel()
                )
            }

            composable(MainAppDestinations.SEARCH_ROUTE) {
                SearchScreen(
                    navController = bottomNavController,
                    usersViewModel = userViewModel
                )
            }

            composable(MainAppDestinations.POST_OPEN_ROUTE) { backStackEntry ->
                val postId = backStackEntry.arguments?.getString("postId")
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    PostScreen(
                        postId = postId,
                        navController = bottomNavController,
                        userViewModel = userViewModel,
                        postsViewModel = postsViewModel,
                    )
                } else {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("Requisição de visualização completa de Post requer Android 8+. Mostrando versão reduzida.")
                    }
                }
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