package com.grupo1.hoppi.mainapp

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.*
import androidx.navigation.compose.rememberNavController
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.grupo1.hoppi.ui.screens.home.Post
import com.grupo1.hoppi.ui.screens.home.PostsViewModel
import com.grupo1.hoppi.ui.screens.mainapp.ProfileScreen
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ProfileUITest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val fakePosts = listOf(
        Post(id = 1, username = "Fulano", content = "Post de teste 1", isLiked = false),
        Post(id = 2, username = "Ciclano", content = "Post de teste 2", isLiked = true)
    )

    private fun createViewModelWithPosts(posts: List<Post>): PostsViewModel {
        val vm = PostsViewModel()
        val field = PostsViewModel::class.java.getDeclaredField("_posts")
        field.isAccessible = true
        val mutableList = field.get(vm) as MutableList<Post>
        mutableList.clear()
        mutableList.addAll(posts)
        return vm
    }

    @Test
    fun profileScreen_displaysHeaderAndPosts_andHandlesClicks() {
        val viewModel = createViewModelWithPosts(fakePosts)
        var clickedPostId: Int? = null
        var settingsClicked = false

        composeTestRule.setContent {
            val navController = rememberNavController()
            ProfileScreen(
                navController = navController,
                postsViewModel = viewModel,
                onPostClick = { clickedPostId = it },
                onSettingsClick = { settingsClicked = true }
            )
        }

        composeTestRule.onNodeWithContentDescription("Voltar").assertExists()
        composeTestRule.onNodeWithContentDescription("Mais opções").assertExists()
        composeTestRule.onNodeWithContentDescription("Profile Picture").assertExists()
        composeTestRule.onAllNodesWithText("@fulan.tal")[0].assertExists()
        composeTestRule.onAllNodesWithText("Fulano de Tal")[0].assertExists()
        composeTestRule.onAllNodesWithText("Seguidores")[0].assertExists()
        composeTestRule.onAllNodesWithText("Seguindo")[0].assertExists()
        composeTestRule.onAllNodesWithText("Posts")[0].assertExists()
        composeTestRule.onAllNodesWithText("Post de teste 1")[0].assertExists()
        composeTestRule.onAllNodesWithText("Post de teste 2")[0].assertExists()
        composeTestRule.onAllNodesWithText("Post de teste 1")[0].performClick()
        assert(clickedPostId == 1)
        composeTestRule.onNodeWithContentDescription("Mais opções").performClick()
        composeTestRule.onNodeWithText("Configurações").performClick()
        assert(settingsClicked)
    }
}