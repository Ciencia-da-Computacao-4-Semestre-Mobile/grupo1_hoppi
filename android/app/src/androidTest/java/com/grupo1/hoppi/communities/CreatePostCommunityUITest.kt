package com.grupo1.hoppi.communities

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.navigation.compose.rememberNavController
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.grupo1.hoppi.ui.screens.home.PostsViewModel
import com.grupo1.hoppi.ui.screens.home.UserViewModel
import com.grupo1.hoppi.ui.screens.mainapp.CreatePostCommunityScreen
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CreatePostCommunityUITest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    private val fakeDataStore = FakeDataStore()
    private val userViewModel = UserViewModel(fakeDataStore)

    @Test
    fun createPostCommunityScreen_displaysAndPublishesPost() {
        val postsViewModel = PostsViewModel()

        composeTestRule.setContent {
            CreatePostCommunityScreen(
                navController = rememberNavController(),
                userViewModel = userViewModel,
                postsViewModel = postsViewModel,
                communityId = 1
            )
        }

        val textNode = composeTestRule
            .onNodeWithText("O que você está pensando?", useUnmergedTree = false)
            .assertExists()

        textNode.performClick()
        textNode.performTextInput("Post da comunidade teste")

        composeTestRule.onNodeWithText("Adicionar tag", useUnmergedTree = true)
            .performClick()

        composeTestRule.onNodeWithText("Estudo", useUnmergedTree = true)
            .performClick()

        composeTestRule.onNodeWithText("Publicar", useUnmergedTree = true)
            .performClick()

        assert(
            postsViewModel.posts.any {
                it.content == "Post da comunidade teste" &&
                        it.tag == "Estudo" &&
                        it.communityId == 1
            }
        )
    }

    @Test
    fun createPostCommunityScreen_canDismissTag() {
        val postsViewModel = PostsViewModel()

        composeTestRule.setContent {
            CreatePostCommunityScreen(
                navController = rememberNavController(),
                userViewModel = userViewModel,
                postsViewModel = postsViewModel,
                communityId = 2
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
    fun createPostCommunityScreen_closeButtonWorks() {
        composeTestRule.setContent {
            CreatePostCommunityScreen(
                navController = rememberNavController(),
                userViewModel = userViewModel,
                postsViewModel = PostsViewModel(),
                communityId = 10
            )
        }

        composeTestRule.onNodeWithContentDescription("Fechar", useUnmergedTree = true)
            .assertExists()
            .performClick()
    }
}