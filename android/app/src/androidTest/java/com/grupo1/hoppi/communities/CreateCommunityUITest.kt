package com.grupo1.hoppi.communities

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.navigation.compose.rememberNavController
import com.grupo1.hoppi.MainActivity
import com.grupo1.hoppi.ui.screens.mainapp.AppCommunityManager
import com.grupo1.hoppi.ui.screens.mainapp.CreateCommunityScreen
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class CreateCommunityUITest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Before
    fun setup() {
        AppCommunityManager.allCommunities.clear()
        AppCommunityManager.userCreatedCommunities.clear()
        AppCommunityManager.followedCommunities.clear()
    }

    @Test
    fun createCommunity_addsNewCommunity() {
        composeTestRule.setContent {
            val nav = rememberNavController()
            CreateCommunityScreen(navController = nav)
        }

        composeTestRule.onNodeWithText("Criar").assertIsNotEnabled()

        composeTestRule.onNodeWithText("Nome").performTextInput("Nova Comunidade")
        composeTestRule.onNodeWithText("Descrição").performTextInput("Uma descrição qualquer")

        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithText("Criar").assertIsEnabled()

        composeTestRule.onNodeWithText("Criar").performClick()

        assert(AppCommunityManager.allCommunities.any { it.name == "Nova Comunidade" })
        assert(AppCommunityManager.userCreatedCommunities.any { it.name == "Nova Comunidade" })
        assert(AppCommunityManager.isFollowing("Nova Comunidade"))
    }

    @Test
    fun privacyMenu_changesOption() {
        composeTestRule.setContent {
            val nav = rememberNavController()
            CreateCommunityScreen(navController = nav)
        }

        composeTestRule.onNodeWithText("Público").performClick()
        composeTestRule.onNodeWithText("Privado").performClick()

        composeTestRule.onNodeWithText("Privado").assertExists()
    }

    @Test
    fun createButton_disabledUntilFieldsFilled() {
        composeTestRule.setContent {
            val nav = rememberNavController()
            CreateCommunityScreen(navController = nav)
        }

        composeTestRule.onNodeWithText("Criar").assertIsNotEnabled()

        composeTestRule.onNodeWithText("Nome").performTextInput("Teste")
        composeTestRule.onNodeWithText("Criar").assertIsNotEnabled()

        composeTestRule.onNodeWithText("Descrição").performTextInput("Alguma descrição")
        composeTestRule.onNodeWithText("Criar").assertIsEnabled()
    }
}