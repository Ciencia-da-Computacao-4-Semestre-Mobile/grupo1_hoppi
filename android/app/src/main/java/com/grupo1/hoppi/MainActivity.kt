package com.grupo1.hoppi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.grupo1.hoppi.ui.screens.home.HomeScreen
import com.grupo1.hoppi.ui.screens.login.LoginScreen
import com.grupo1.hoppi.ui.screens.signup.SignUpFlow
import com.grupo1.hoppi.ui.screens.login.forgotpassword.ForgotPasswordFlow
import com.grupo1.hoppi.ui.screens.mainapp.CreatePostScreen
import com.grupo1.hoppi.ui.screens.mainapp.NotificationScreen
import com.grupo1.hoppi.ui.screens.mainapp.PostScreen
import com.grupo1.hoppi.ui.screens.mainapp.ProfileScreen
import com.grupo1.hoppi.ui.screens.mainapp.SearchScreen
import com.grupo1.hoppi.ui.theme.HoppiTheme

object Destinations {
    const val LOGIN_ROUTE = "login"
    const val SIGNUP_ROUTE = "signup"
    const val FORGOT_PASSWORD_ROUTE = "forgot_password"
    const val HOME_ROUTE = "home"
}
@Composable
fun HoppiApp() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Destinations.LOGIN_ROUTE
    ) {
        composable(Destinations.LOGIN_ROUTE) {
            LoginScreen(
                onSignUpClick = {
                    navController.navigate(Destinations.SIGNUP_ROUTE)
                },
                onForgotPasswordClick = {
                    navController.navigate(Destinations.FORGOT_PASSWORD_ROUTE)
                },
                onLoginSuccess = {
                    navController.navigate(Destinations.HOME_ROUTE) {
                        popUpTo(Destinations.LOGIN_ROUTE) { inclusive = true }
                    }
                }
            )
        }

        composable(Destinations.SIGNUP_ROUTE) {
            SignUpFlow(
                onLoginClick = {
                    navController.navigate(Destinations.LOGIN_ROUTE)
                }
            )
        }

        composable(Destinations.FORGOT_PASSWORD_ROUTE) {
            ForgotPasswordFlow(
                onBackToLogin = {
                    navController.popBackStack(
                        Destinations.LOGIN_ROUTE,
                        inclusive = false
                    )
                }
            )
        }

        composable(Destinations.HOME_ROUTE) {
            HomeScreen(rootNavController = navController)
        }

        composable("main/search") {
            SearchScreen(navController = navController)
        }

        composable("main/communities") {
            NotificationScreen(navController = navController)
        }

        composable("main/create_community") {
            NotificationScreen(navController = navController)
        }

        composable("main/notifications") {
            NotificationScreen(navController = navController)
        }

        composable("main/post_open/{postId}") { backStackEntry ->
            val postId = backStackEntry.arguments?.getString("postId")?.toIntOrNull()
            PostScreen(postId = postId, navController = navController)
        }

        composable("main/create_post") {
            CreatePostScreen(navController = navController)
        }
    }
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HoppiTheme {
                HoppiApp()
            }
        }
    }
}