package com.grupo1.hoppi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.grupo1.hoppi.ui.screens.Login.SignUpScreen
import com.grupo1.hoppi.ui.screens.LoginScreen
import com.grupo1.hoppi.ui.theme.HoppiTheme

object Destinations {
    const val LOGIN_ROUTE = "login"
    const val SIGNUP_ROUTE = "signup"
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
                }
            )
        }

        composable(Destinations.SIGNUP_ROUTE) {
            SignUpScreen(
                onLoginClick = {
                    navController.navigate(Destinations.LOGIN_ROUTE)
                }
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