package com.grupo1.hoppi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.grupo1.hoppi.ui.screens.login.LoginScreen
import com.grupo1.hoppi.ui.screens.signup.SignUpFlow
import com.grupo1.hoppi.ui.screens.login.forgotpassword.ForgotPasswordFlow
import com.grupo1.hoppi.ui.theme.HoppiTheme

object Destinations {
    const val LOGIN_ROUTE = "login"
    const val SIGNUP_ROUTE = "signup"
    const val FORGOT_PASSWORD_ROUTE = "forgot_password"
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
                onBackToLogin = { navController.popBackStack(Destinations.LOGIN_ROUTE, inclusive = false) }
            )
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