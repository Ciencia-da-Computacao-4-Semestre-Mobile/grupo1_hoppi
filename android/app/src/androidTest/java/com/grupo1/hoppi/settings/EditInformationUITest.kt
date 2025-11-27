package com.grupo1.hoppi.settings

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.grupo1.hoppi.ui.screens.settings.account.EditInformationContent
import com.grupo1.hoppi.ui.screens.settings.account.EditInformationTopBar
import org.junit.Rule
import org.junit.Test

class EditInformationUITest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun editInformation_interactionsWork() {
        var saveCalled = false
        var cancelCalled = false

        composeRule.setContent {
            EditInformationContent(
                onSave = { saveCalled = true },
                onCancel = { cancelCalled = true }
            )
        }

        composeRule.onNodeWithText("Alterar dados").assertExists()

        composeRule.onAllNodes(isEditable())[0].performTextInput("Novo Nome")
        composeRule.onAllNodes(isEditable())[1].performTextInput("@novo_username")
        composeRule.onAllNodes(isEditable())[2].performTextInput("10/10/2010")
        composeRule.onAllNodes(isEditable())[3].performTextInput("Nova Instituição")

        composeRule.onNodeWithText("Salvar alterações").performClick()
        assert(saveCalled)

        composeRule.onNodeWithText("Cancelar").performClick()
        assert(cancelCalled)
    }

    @Test
    fun editInformation_backButtonWorks() {
        var backCalled = false

        composeRule.setContent {
            val navController = rememberNavController()

            navController.addOnDestinationChangedListener { _, _, _ ->
                backCalled = true
            }

            NavHost(
                navController = navController,
                startDestination = "edit"
            ) {
                composable("edit") {
                    EditInformationTopBar(navController = navController)
                }
                composable("home") { /* vazio mesmo */ }
            }
        }

        composeRule.onNodeWithContentDescription("Voltar").performClick()

        assert(backCalled)
    }
}