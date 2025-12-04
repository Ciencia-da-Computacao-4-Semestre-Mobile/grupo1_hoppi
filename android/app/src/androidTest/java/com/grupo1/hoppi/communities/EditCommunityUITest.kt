package com.grupo1.hoppi.communities

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.navigation.compose.rememberNavController
import com.grupo1.hoppi.ui.screens.mainapp.AppCommunityManager
import com.grupo1.hoppi.ui.screens.mainapp.Community
import com.grupo1.hoppi.ui.screens.mainapp.EditCommunityScreen
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class EditCommunityUITest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Before
    fun setup() {
        AppCommunityManager.allCommunities.clear()
        AppCommunityManager.userCreatedCommunities.clear()

        val community = Community(
            name = "Antigo Nome",
            description = "Antiga descrição",
            id = 99,
            isPrivate = false,
            creatorUsername = "Caio"
        )

        AppCommunityManager.allCommunities.add(community)
        AppCommunityManager.userCreatedCommunities.add(community)

        composeTestRule.setContent {
            val nav = rememberNavController()
            EditCommunityScreen(navController = nav, communityId = 99)
        }
    }

    @Test
    fun editCommunity_updatesDataCorrectly() {
        composeTestRule.onNodeWithText("Nome da Comunidade").performTextClearance()
        composeTestRule.onNodeWithText("Nome da Comunidade").performTextInput("Novo Nome")

        composeTestRule.onNodeWithText("Descrição").performTextClearance()
        composeTestRule.onNodeWithText("Descrição").performTextInput("Nova descrição modificada")

        composeTestRule.onNodeWithTag("privacy_toggle").performClick()

        composeTestRule.waitForIdle()

        composeTestRule.waitUntil(
            timeoutMillis = 5_000
        ) {
            composeTestRule.onAllNodesWithTag("privacy_private").fetchSemanticsNodes().isNotEmpty()
        }

        composeTestRule.onNodeWithTag("privacy_private").performClick()

        composeTestRule.onNodeWithText("Salvar").performClick()

        val updated = AppCommunityManager.allCommunities.find { it.id == 99 }!!

        assert(updated.isPrivate)
        assert(updated.name == "Novo Nome")
        assert(updated.description == "Nova descrição modificada")
    }
}
