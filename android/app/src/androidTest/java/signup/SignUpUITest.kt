package signup

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import com.grupo1.hoppi.ui.screens.signup.SignUpFlow
import com.grupo1.hoppi.ui.screens.signup.SignUpState
import com.grupo1.hoppi.ui.screens.signup.SignUpViewModel
import org.junit.Rule
import org.junit.Test

class TestSignUpViewModel(
    initialState: SignUpState = SignUpState()
) : SignUpViewModel() {
    init {
        _state.value = initialState
    }
}

class SignUpFlowUiTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun signUpFlow_successfulSignUp_callsFinish() {
        var finishCalled = false

        val mockState = SignUpState(
            name = "Nome Usuário",
            birthDate = "01/01/2000",
            institution = "UCLA",
            email = "email@example.com",
            password = "Senha123",
            acceptedTerms = true,
            isButtonEnabled = true
        )
        val testViewModel = TestSignUpViewModel(initialState = mockState)

        composeTestRule.setContent {
            SignUpFlow(
                onLoginClick = { },
                onFinish = { finishCalled = true },
                mockViewModel = testViewModel
            )
        }

        composeTestRule.onNodeWithTag("SignUpButton")
            .performScrollTo()
            .performClick()

        composeTestRule.onNodeWithTag("FinishSignUp").assertExists()

        composeTestRule.onNodeWithTag("Avatar0")
            .performScrollTo()
            .performClick()
        composeTestRule.onNodeWithTag("FinishSignUp")
            .performScrollTo()
            .performClick()

        assert(finishCalled)
    }

    @Test
    fun signUpFlow_loginClick_worksCorrectly() {
        var loginClicked = false

        composeTestRule.setContent {
            SignUpFlow(
                onLoginClick = { loginClicked = true },
                onFinish = { }
            )
        }

        composeTestRule.onNodeWithTag("AlreadyRegistered")
            .performScrollTo()
            .performClick()

        assert(loginClicked)
    }
}