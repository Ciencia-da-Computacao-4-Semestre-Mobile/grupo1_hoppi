package com.grupo1.hoppi.ui.screens.mainapp.settings

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.grupo1.hoppi.ui.screens.settings.SettingsScreen
import com.grupo1.hoppi.ui.screens.settings.account.EditEmailScreen
import com.grupo1.hoppi.ui.screens.settings.account.EditInformationScreen
import com.grupo1.hoppi.ui.screens.settings.EditPasswordScreen
import com.grupo1.hoppi.ui.screens.settings.AboutUsScreen
import com.grupo1.hoppi.ui.screens.settings.PrivacyPolicyScreen
import com.grupo1.hoppi.ui.screens.settings.PrivacyPolicyScreen
import com.grupo1.hoppi.ui.screens.settings.NewPasswordScreen
import com.grupo1.hoppi.ui.screens.settings.PasswordRecoveryScreen
import com.grupo1.hoppi.ui.screens.home.MainAppDestinations
import com.grupo1.hoppi.ui.screens.home.UsersViewModel

object SettingsDestinations {
    const val SETTINGS_MAIN_SCREEN = "settings_main"
    const val EDIT_INFORMATION_ROUTE = "settings/edit_information"
    const val EDIT_EMAIL_ROUTE = "settings/edit_email"
    const val CHANGE_PASSWORD_ROUTE = "settings/change_password"
    const val PRIVACY_POLICY_ROUTE = "settings/privacy_policy"
    const val ABOUT_US_ROUTE = "settings/about_us"
    const val RECOVER_PASSWORD_ROUTE = "settings/change_password/recover_password"
    const val NEW_PASSWORD_ROUTE = "settings/change_password/new_password"
}

@Composable
fun SettingsNavGraph(
    rootNavController: NavController,
    onLogout: () -> Unit,
    usersViewModel: UsersViewModel
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
            EditInformationScreen(
                navController = settingsNavController,
                usersViewModel = usersViewModel
            )
        }

        composable(SettingsDestinations.EDIT_EMAIL_ROUTE) {
            EditEmailScreen(
                navController = settingsNavController,
                usersViewModel = usersViewModel
            )
        }

        composable(SettingsDestinations.CHANGE_PASSWORD_ROUTE) {
            EditPasswordScreen(
                navController = settingsNavController,
                usersViewModel = usersViewModel,
                onForgottenPasswordClick = {
                    settingsNavController.navigate(SettingsDestinations.RECOVER_PASSWORD_ROUTE)
                }
            )
        }

        composable(SettingsDestinations.RECOVER_PASSWORD_ROUTE) {
            PasswordRecoveryScreen(navController = settingsNavController)
        }

        composable(SettingsDestinations.NEW_PASSWORD_ROUTE) {
            NewPasswordScreen(
                navController = settingsNavController,
                onFinishFlow = {
                    settingsNavController.popBackStack(
                        SettingsDestinations.SETTINGS_MAIN_SCREEN,
                        false
                    )
                }
            )
        }

        composable(SettingsDestinations.PRIVACY_POLICY_ROUTE) {
            PrivacyPolicyScreen(navController = settingsNavController)
        }

        composable(SettingsDestinations.ABOUT_US_ROUTE) {
            AboutUsScreen(navController = settingsNavController)
        }
    }
}
