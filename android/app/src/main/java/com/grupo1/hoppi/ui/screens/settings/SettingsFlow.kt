package com.grupo1.hoppi.ui.screens.mainapp.settings

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.grupo1.hoppi.ui.screens.settings.SettingsScreen
import com.grupo1.hoppi.ui.screens.settings.account.EditEmailScreen
import com.grupo1.hoppi.ui.screens.settings.account.EditInformationScreen
import com.grupo1.hoppi.ui.screens.settings.AboutUsScreen
import com.grupo1.hoppi.ui.screens.settings.PrivacyPolicyScreen
import com.grupo1.hoppi.ui.screens.home.MainAppDestinations

object SettingsDestinations {
    const val SETTINGS_MAIN_SCREEN = "settings_main"
    const val EDIT_INFORMATION_ROUTE = "settings/edit_information"
    const val EDIT_EMAIL_ROUTE = "settings/edit_email"
    const val CHANGE_PASSWORD_ROUTE = "settings/change_password"
    const val PRIVACY_POLICY_ROUTE = "settings/privacy_policy"
    const val ABOUT_US_ROUTE = "settings/about_us"
}

@Composable
fun SettingsNavGraph(
    rootNavController: NavController,
    onLogout: () -> Unit
) {
    val settingsNavController = rememberNavController()

    NavHost(
        navController = settingsNavController,
        startDestination = SettingsDestinations.SETTINGS_MAIN_SCREEN
    ) {
        composable(SettingsDestinations.SETTINGS_MAIN_SCREEN) {
            SettingsScreen(
                onEditInformationClick = { settingsNavController.navigate(SettingsDestinations.EDIT_INFORMATION_ROUTE) },
                onEditEmailClick = { settingsNavController.navigate(SettingsDestinations.EDIT_EMAIL_ROUTE) },
                onChangePasswordClick = { settingsNavController.navigate(SettingsDestinations.CHANGE_PASSWORD_ROUTE) },
                onPrivacyPolicyClick = { settingsNavController.navigate(SettingsDestinations.PRIVACY_POLICY_ROUTE) },
                onAboutUsClick = { settingsNavController.navigate(SettingsDestinations.ABOUT_US_ROUTE) },
                onLogoutClick = onLogout,
                onBack = { rootNavController.popBackStack() }
            )
        }

        composable(SettingsDestinations.EDIT_INFORMATION_ROUTE) {
            EditInformationScreen(navController = settingsNavController)
        }

        composable(SettingsDestinations.EDIT_EMAIL_ROUTE) {
            EditEmailScreen(navController = settingsNavController)
        }

        composable(SettingsDestinations.CHANGE_PASSWORD_ROUTE) {
            // ChangePasswordScreen(navController = settingsNavController)
        }

        composable(SettingsDestinations.PRIVACY_POLICY_ROUTE) {
            PrivacyPolicyScreen(navController = settingsNavController)
        }

        composable(SettingsDestinations.ABOUT_US_ROUTE) {
            AboutUsScreen(navController = settingsNavController)
        }
    }
}
