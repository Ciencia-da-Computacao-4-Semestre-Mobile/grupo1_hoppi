package com.grupo1.hoppi.mainapp

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.grupo1.hoppi.ui.screens.home.Post
import com.grupo1.hoppi.ui.screens.home.PostsViewModel
import com.grupo1.hoppi.ui.screens.mainapp.FeedScreen
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class FeedUITest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val fakePosts = listOf(
        Post(id = 1, username = "Usuário 1", content = "Primeiro post!", isLiked = false, tag = "Estudo"),
        Post(id = 2, username = "Usuário 2", content = "Adoro programar.", isLiked = true, tag = "Info")
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
    fun feedScreen_displaysPosts_andRespondsToClicks() {
        val viewModel = createViewModelWithPosts(fakePosts)
        var clickedPostId: Int? = null
        var notificationsClicked = false
        var profileClicked = false

        composeTestRule.setContent {
            FeedScreen(
                modifier = androidx.compose.ui.Modifier,
                postsViewModel = viewModel,
                onPostClick = { clickedPostId = it },
                onNotificationsClick = { notificationsClicked = true },
                onProfileClick = { profileClicked = true }
            )
        }

        composeTestRule.onNodeWithContentDescription("Perfil").assertExists()
        composeTestRule.onNodeWithContentDescription("Logo Hoppi").assertExists()
        composeTestRule.onNodeWithContentDescription("Notificações").assertExists()
        composeTestRule.onNodeWithText("Usuário 1").assertExists()
        composeTestRule.onNodeWithText("Usuário 2").assertExists()
        composeTestRule.onNodeWithText("Primeiro post!").performClick()
        assert(clickedPostId == 1)
        composeTestRule.onNodeWithContentDescription("Notificações").performClick()
        assert(notificationsClicked)
        composeTestRule.onNodeWithContentDescription("Perfil").performClick()
        assert(profileClicked)
    }
}