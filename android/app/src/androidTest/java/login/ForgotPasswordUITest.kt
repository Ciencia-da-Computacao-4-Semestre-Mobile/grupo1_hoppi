package login

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.hasSetTextAction
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.performClick
import com.grupo1.hoppi.ui.screens.login.forgotpassword.ForgotPasswordFlow
import org.junit.Rule
import org.junit.Test

class ForgotPasswordFlowTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun forgotPassword_flow_transitionsCorrectly() {
        var backToLoginCalled = false

        composeTestRule.setContent {
            ForgotPasswordFlow(
                onBackToLogin = { backToLoginCalled = true }
            )
        }

        // Step 1: Seleciona método de recuperação
        composeTestRule.onNodeWithText("Ful*****@*****.com").performClick()
        composeTestRule.onNodeWithText("Digite o código").assertExists()

        // Step 2: Digita o código
        val codeFields = composeTestRule.onAllNodes(hasSetTextAction())
        codeFields[0].performTextInput("1")
        codeFields[1].performTextInput("2")
        codeFields[2].performTextInput("3")
        codeFields[3].performTextInput("4")

        composeTestRule.onNodeWithText("Validar").performClick()
        composeTestRule.onNodeWithText("Digite sua nova senha").assertExists()

        val passwordFields = composeTestRule.onAllNodes(hasSetTextAction())
        passwordFields[0].performTextInput("Senha123")
        passwordFields[1].performTextInput("Senha123")

        composeTestRule.onNodeWithText("Cadastrar").performClick()

        composeTestRule.onNodeWithText("Voltar", useUnmergedTree = true).performClick()

        assert(backToLoginCalled)
    }
}