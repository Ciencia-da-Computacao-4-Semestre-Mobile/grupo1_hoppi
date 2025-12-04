package com.grupo1.hoppi.ui.screens.settings

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.core.app.ApplicationProvider
import androidx.navigation.testing.TestNavHostController
import org.junit.Rule
import org.junit.Test

class NewPasswordUITest {

    @get:Rule
    val rule = createComposeRule()

    private fun passwordFields() =
        rule.onAllNodes(hasSetTextAction())

    @Test
    fun savesPassword() {
        var finished = false

        val nav = TestNavHostController(ApplicationProvider.getApplicationContext())

        rule.setContent {
            NewPasswordScreen(
                navController = nav,
                onFinishFlow = { finished = true }
            )
        }

        passwordFields()[0].performTextInput("Senha123")
        passwordFields()[1].performTextInput("Senha123")

        rule.onNodeWithTag("btn_save_password").performClick()

        assert(finished)
    }

    @Test
    fun cancelsFlow_callsCancelCallback() {
        var canceled = false

        val nav = TestNavHostController(ApplicationProvider.getApplicationContext())

        rule.setContent {
            NewPasswordContent(
                onSave = {},
                onCancel = { canceled = true }
            )
        }

        rule.onNodeWithText("Cancelar").performClick()

        assert(canceled)
    }
}
