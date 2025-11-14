package com.grupo1.hoppi.settings

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.grupo1.hoppi.ui.screens.settings.SettingsScreen
import org.junit.Rule
import org.junit.Test

class SettingsUITest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun settingsScreen_interactionsWork() {
        var editInfoClicked = false
        var editEmailClicked = false
        var changePasswordClicked = false
        var privacyClicked = false
        var aboutClicked = false
        var logoutClicked = false
        var backClicked = false

        composeRule.setContent {
            SettingsScreen(
                onEditInformationClick = { editInfoClicked = true },
                onEditEmailClick = { editEmailClicked = true },
                onChangePasswordClick = { changePasswordClicked = true },
                onPrivacyPolicyClick = { privacyClicked = true },
                onAboutUsClick = { aboutClicked = true },
                onLogoutClick = { logoutClicked = true },
                onBack = { backClicked = true }
            )
        }

        composeRule.onNodeWithContentDescription("Voltar").performClick()
        assert(backClicked)

        composeRule.onNodeWithText("Editar informações").performClick()
        assert(editInfoClicked)

        composeRule.onNodeWithText("E-mail").performClick()
        assert(editEmailClicked)

        composeRule.onNodeWithText("Alterar senha").performClick()
        assert(changePasswordClicked)

        composeRule.onNodeWithText("Política de privacidade").performClick()
        assert(privacyClicked)

        composeRule.onNodeWithText("Sobre nós").performClick()
        assert(aboutClicked)

        composeRule.onNodeWithText("Sair").performClick()
        assert(logoutClicked)
    }
}