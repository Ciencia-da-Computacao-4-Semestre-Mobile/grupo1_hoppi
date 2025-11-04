package com.grupo1.hoppi.ui.screens.mainapp.settings

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.grupo1.hoppi.ui.screens.settings.SettingsScreen

object SettingsDestinations {
    const val SETTINGS_MAIN_SCREEN = "settings_main"
    const val EDIT_USERNAME_ROUTE = "settings/edit_username"
    const val EDIT_EMAIL_PHONE_ROUTE = "settings/edit_email_phone"
    const val CHANGE_PASSWORD_ROUTE = "settings/change_password"
    const val NOTIFICATIONS_ROUTE = "settings/notifications"
    const val PRIVACY_POLICY_ROUTE = "settings/privacy_policy"
    const val ABOUT_US_ROUTE = "settings/about_us"
}

@Composable
fun SettingsNavGraph(rootNavController: NavController, onLogout: () -> Unit) {
    val settingsNavController = rememberNavController()

    NavHost(
        navController = settingsNavController,
        startDestination = SettingsDestinations.SETTINGS_MAIN_SCREEN
    ) {
        composable(SettingsDestinations.SETTINGS_MAIN_SCREEN) {
            SettingsScreen(
                navController = settingsNavController,
                bottomNavController = rootNavController,
                onEditUsernameClick = { settingsNavController.navigate(SettingsDestinations.EDIT_USERNAME_ROUTE) },
                onEditEmailPhoneClick = { settingsNavController.navigate(SettingsDestinations.EDIT_EMAIL_PHONE_ROUTE) },
                onChangePasswordClick = { settingsNavController.navigate(SettingsDestinations.CHANGE_PASSWORD_ROUTE) },
                onNotificationsClick = { settingsNavController.navigate(SettingsDestinations.NOTIFICATIONS_ROUTE) },
                onPrivacyPolicyClick = { settingsNavController.navigate(SettingsDestinations.PRIVACY_POLICY_ROUTE) },
                onAboutUsClick = { settingsNavController.navigate(SettingsDestinations.ABOUT_US_ROUTE) },
                onLogoutClick = onLogout
            )
        }

        composable(SettingsDestinations.EDIT_USERNAME_ROUTE) {
            // EditUsernameScreen(navController = settingsNavController)
        }

        composable(SettingsDestinations.EDIT_EMAIL_PHONE_ROUTE) {
            // EditEmailPhoneScreen(navController = settingsNavController)
        }

        composable(SettingsDestinations.CHANGE_PASSWORD_ROUTE) {
            // ChangePasswordScreen(navController = settingsNavController)
        }

        composable(SettingsDestinations.NOTIFICATIONS_ROUTE) {
            // NotificationSettingsScreen(navController = settingsNavController)
        }

        composable(SettingsDestinations.PRIVACY_POLICY_ROUTE) {
            // PrivacyPolicyScreen(navController = settingsNavController)
        }

        composable(SettingsDestinations.ABOUT_US_ROUTE) {
            // AboutUsScreen(navController = settingsNavController)
        }
    }
}