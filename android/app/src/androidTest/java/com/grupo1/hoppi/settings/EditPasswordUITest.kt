package com.grupo1.hoppi.settings

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasSetTextAction
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import com.grupo1.hoppi.ui.screens.settings.EditPasswordContent
import org.junit.Rule
import org.junit.Test

class EditPasswordUITest {

    @get:Rule
    val rule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun typePasswords_correctlyUpdatesFields() {

        rule.setContent {
            EditPasswordContent(
                onSave = {},
                onCancel = {},
                onForgottenPasswordClick = {}
            )
        }

        rule.onNodeWithText("Sua senha atual *").assertIsDisplayed()

        rule.onAllNodes(hasSetTextAction())[0]
            .performTextInput("SenhaAntiga123")

        rule.onNodeWithText("Sua senha nova *").assertIsDisplayed()

        rule.onAllNodes(hasSetTextAction())[1]
            .performTextInput("NovaSenhaA1")

        rule.onNodeWithText("Confirme a senha *").assertIsDisplayed()

        rule.onAllNodes(hasSetTextAction())[2]
            .performTextInput("NovaSenhaA1")
    }

    @Test
    fun clickForgotPassword_callsLambda() {
        var clicked = false

        rule.setContent {
            EditPasswordContent(
                onSave = {},
                onCancel = {},
                onForgottenPasswordClick = { clicked = true }
            )
        }

        rule.onNodeWithText("Esqueceu a senha?").performClick()

        assert(clicked)
    }

    @Test
    fun clickSave_callsLambda() {
        var saved = false

        rule.setContent {
            EditPasswordContent(
                onSave = { saved = true },
                onCancel = {},
                onForgottenPasswordClick = {}
            )
        }

        rule.onNodeWithText("Salvar alterações").performClick()

        assert(saved)
    }

    @Test
    fun clickCancel_callsLambda() {
        var cancelled = false

        rule.setContent {
            EditPasswordContent(
                onSave = {},
                onCancel = { cancelled = true },
                onForgottenPasswordClick = {}
            )
        }

        rule.onNodeWithText("Cancelar").performClick()

        assert(cancelled)
    }
}
