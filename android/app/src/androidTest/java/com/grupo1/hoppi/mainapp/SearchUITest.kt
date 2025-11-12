package com.grupo1.hoppi.ui.screens.mainapp

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SearchUITest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private lateinit var navController: TestNavHostController

    @Before
    fun setup() {
        navController = TestNavHostController(ApplicationProvider.getApplicationContext())

        composeTestRule.setContent {
            SearchScreen(navController = navController)
        }
    }

    @Test
    fun searchScreen_isDisplayed_and_searchField_isFocused() {
        composeTestRule.onNodeWithText("Pesquisa").assertIsDisplayed()
        composeTestRule.onNodeWithTag("SearchTextField").assertIsDisplayed()
        composeTestRule.onNodeWithTag("SearchTextField").assertIsFocused()
    }

    @Test
    fun searchScreen_typing_in_searchField_updates_text() {
        val searchText = "Novo Item"
        composeTestRule.onNodeWithTag("SearchTextField").performTextInput(searchText)
        composeTestRule.onNodeWithTag("SearchTextField").assert(hasText(searchText))
    }

    @Test
    fun searchScreen_initial_history_items_are_displayed() {
        for (item in mockSearchItems) {
            composeTestRule.onNodeWithText(item).assertIsDisplayed()
        }
    }

    @Test
    fun searchScreen_removing_item_updates_history() {
        val itemToRemove = "Item 3"

        composeTestRule.onNodeWithText(itemToRemove)
            .assertIsDisplayed()

        composeTestRule.onNodeWithTag("RemoveButton_Item 3").performClick()

        composeTestRule.onNodeWithText(itemToRemove).assertDoesNotExist()

        for (item in mockSearchItems.filter { it != itemToRemove }) {
            composeTestRule.onNodeWithText(item).assertIsDisplayed()
        }
    }

    @Test
    fun searchScreen_backButton_is_displayed() {
        composeTestRule.onNodeWithContentDescription("Voltar").assertIsDisplayed()
    }
}