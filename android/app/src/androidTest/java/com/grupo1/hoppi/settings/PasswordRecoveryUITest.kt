package com.grupo1.hoppi.settings

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.hasSetTextAction
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.navigation.createGraph
import androidx.navigation.testing.TestNavHostController
import com.grupo1.hoppi.ui.screens.settings.PasswordRecoveryScreen
import org.junit.Rule
import org.junit.Test

class PasswordRecoveryUITest {

    @get:Rule
    val rule = createAndroidComposeRule<ComponentActivity>()

    private fun codeField() = rule.onNode(hasSetTextAction())

    @Test
    fun invalidCode_doesNotNavigate() {
        val nav = TestNavHostController(rule.activity)

        rule.setContent {
            PasswordRecoveryScreen(
                navController = nav
            )
        }

        codeField().performTextInput("9999")
        rule.onNodeWithText("Validar").performClick()

        assert(nav.currentDestination?.route != "new_password")
    }

    @Test
    fun enablesButton_onlyWhenCodeIs4Digits() {
        rule.setContent {
            PasswordRecoveryScreen(
                navController = TestNavHostController(rule.activity)
            )
        }

        rule.onNodeWithText("Validar").assertIsNotEnabled()

        codeField().performTextInput("11")
        rule.onNodeWithText("Validar").assertIsNotEnabled()

        codeField().performTextInput("22")
        rule.onNodeWithText("Validar").assertIsEnabled()
    }
}