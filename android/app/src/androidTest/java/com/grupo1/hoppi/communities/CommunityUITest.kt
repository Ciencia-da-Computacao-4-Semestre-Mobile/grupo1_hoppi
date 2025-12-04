package com.grupo1.hoppi.communities

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.navigation.compose.rememberNavController
import com.grupo1.hoppi.ui.screens.mainapp.AppCommunityManager
import com.grupo1.hoppi.ui.screens.mainapp.CommunitiesScreen
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class CommunityUITest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Before
    fun setup() {
        AppCommunityManager.allCommunities.clear()
        AppCommunityManager.followedCommunities.clear()

        AppCommunityManager.allCommunities.addAll(
            listOf(
                com.grupo1.hoppi.ui.screens.mainapp.Community(
                    name = "Comunidade de Estudantes",
                    description = "desc",
                    id = 1
                ),
                com.grupo1.hoppi.ui.screens.mainapp.Community(
                    name = "Comunidade Alimentação",
                    description = "comidas",
                    id = 2
                )
            )
        )

        AppCommunityManager.followedCommunities.add("Comunidade de Estudantes")

        composeTestRule.setContent {
            val nav = rememberNavController()
            CommunitiesScreen(navController = nav)
        }
    }

    @Test
    fun followCommunity_fromExplore_changesState() {
        composeTestRule.onNodeWithTag("tab_Explorar").performClick()

        composeTestRule.onNodeWithTag("follow_Comunidade Alimentação").performClick()

        assert(AppCommunityManager.isFollowing("Comunidade Alimentação"))
    }

    @Test
    fun unfollowCommunity_fromHome_changesState() {
        composeTestRule.onNodeWithTag("follow_Comunidade de Estudantes").performClick()

        assert(!AppCommunityManager.isFollowing("Comunidade de Estudantes"))
    }

    @Test
    fun searchInExplore_filtersCorrectly() {
        composeTestRule.onNodeWithTag("tab_Explorar").performClick()

        composeTestRule.onNodeWithContentDescription("Search").performClick()

        composeTestRule
            .onNodeWithText("Buscar Comunidades...")
            .performTextInput("alimen")

        composeTestRule.onNodeWithTag("community_Comunidade Alimentação").assertExists()
        composeTestRule.onNodeWithTag("community_Comunidade de Estudantes").assertDoesNotExist()
    }

    @Test
    fun searchInHome_filtersCorrectly() {
        // Já está na Home

        composeTestRule.onNodeWithContentDescription("Search").performClick()

        composeTestRule
            .onNodeWithText("Buscar Comunidades...")
            .performTextInput("estud")

        composeTestRule.onNodeWithTag("community_Comunidade de Estudantes").assertExists()
        composeTestRule.onNodeWithTag("community_Comunidade Alimentação").assertDoesNotExist()
    }
}
