package com.grupo1.hoppi.mainapp

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.grupo1.hoppi.ui.screens.home.PostsViewModel
import com.grupo1.hoppi.ui.screens.home.UserViewModel
import com.grupo1.hoppi.ui.screens.mainapp.CreatePostScreen
import androidx.navigation.compose.rememberNavController
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class CreatePostUITest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    private val fakeDataStore = FakeDataStore()
    private val userViewModel = UserViewModel(fakeDataStore)

    @Test
    fun createPostScreen_displaysAndPublishesPost() {
        val postsViewModel = PostsViewModel()

        composeTestRule.setContent {
            CreatePostScreen(
                navController = rememberNavController(),
                postsViewModel = postsViewModel,
                userViewModel = userViewModel
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

        assert(postsViewModel.posts.any { it.content == "Novo post de teste" && it.tag == "Estudo" })
    }

    @Test
    fun createPostScreen_canDismissTag() {
        val postsViewModel = PostsViewModel()

        composeTestRule.setContent {
            CreatePostScreen(
                navController = rememberNavController(),
                postsViewModel = postsViewModel,
                userViewModel = userViewModel
            )
        }

        composeTestRule.onNodeWithText("Adicionar tag", useUnmergedTree = true).performClick()
        composeTestRule.onNodeWithText("Venda", useUnmergedTree = true).performClick()

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
                postsViewModel = PostsViewModel(),
                userViewModel = userViewModel
            )
        }

        composeTestRule.onNodeWithContentDescription("Fechar", useUnmergedTree = true)
            .assertExists()
            .performClick()
    }
}