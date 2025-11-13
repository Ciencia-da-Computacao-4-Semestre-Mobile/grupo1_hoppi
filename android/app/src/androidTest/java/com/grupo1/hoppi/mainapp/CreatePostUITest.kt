package com.grupo1.hoppi.mainapp

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.grupo1.hoppi.ui.screens.mainapp.CreatePostScreen
import androidx.activity.ComponentActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import androidx.navigation.compose.rememberNavController
import com.grupo1.hoppi.ui.screens.home.PostsViewModel

@RunWith(AndroidJUnit4::class)
class CreatePostUITest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun createPostScreen_displaysAndPublishesPost() {
        val viewModel = PostsViewModel()
        composeTestRule.setContent {
            CreatePostScreen(
                navController = rememberNavController(),
                postsViewModel = viewModel
            )
        }

        val textNode = composeTestRule
            .onNodeWithText("O que você está pensando?", useUnmergedTree = false)
            .assertExists()

        textNode.performClick()
        textNode.performTextInput("Novo post de teste")

        composeTestRule.onNodeWithText("Adicionar tag", useUnmergedTree = true)
            .performClick()

        composeTestRule.onNodeWithText("Estudo", useUnmergedTree = true)
            .performClick()

        composeTestRule.onNodeWithText("Publicar", useUnmergedTree = true)
            .performClick()

        assert(viewModel.posts.any { it.content == "Novo post de teste" })
    }

    @Test
    fun createPostScreen_canDismissTag() {
        val viewModel = PostsViewModel()
        composeTestRule.setContent {
            CreatePostScreen(
                navController = rememberNavController(),
                postsViewModel = viewModel
            )
        }

        composeTestRule.onNodeWithText("Adicionar tag", useUnmergedTree = true)
            .performClick()

        composeTestRule.onNodeWithText("Venda", useUnmergedTree = true)
            .performClick()

        composeTestRule.onNodeWithContentDescription("Remover Tag", useUnmergedTree = true)
            .performClick()

        composeTestRule.onAllNodesWithContentDescription("Remover Tag", useUnmergedTree = true)
            .assertCountEquals(0)
    }

    @Test
    fun createPostScreen_closeButtonWorks() {
        composeTestRule.setContent {
            CreatePostScreen(
                navController = rememberNavController(),
                postsViewModel = PostsViewModel()
            )
        }

        composeTestRule.onNodeWithContentDescription("Fechar", useUnmergedTree = true)
            .assertExists()
            .performClick()
    }
}