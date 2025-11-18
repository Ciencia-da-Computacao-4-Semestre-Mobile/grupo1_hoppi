package com.grupo1.hoppi.mainapp

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.grupo1.hoppi.ui.screens.home.Post
import com.grupo1.hoppi.ui.screens.home.PostsViewModel
import com.grupo1.hoppi.ui.screens.home.UserViewModel
import com.grupo1.hoppi.ui.screens.mainapp.FeedScreen
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class FeedUITest {

    @get:Rule
    val composeTestRule = createComposeRule()
    val fakeDataStore = FakeDataStore()

    private val fakePosts = listOf(
        Post(id = 1, username = "Usuário 1", handle = "@user1", content = "Primeiro post!", isLiked = false, tag = "Estudo", likes = 5, comments = 2),
        Post(id = 2, username = "Usuário 2", handle = "@user2", content = "Adoro programar.", isLiked = true, tag = "Info", likes = 10, comments = 1)
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
        val postsViewModel = createViewModelWithPosts(fakePosts)
        val userViewModel = UserViewModel(fakeDataStore)
        var clickedPostId: Int? = null
        var notificationsClicked = false
        var profileClicked = false
        var likeClickedCount = 0

        composeTestRule.setContent {
            FeedScreen(
                modifier = androidx.compose.ui.Modifier,
                postsViewModel = postsViewModel,
                userViewModel = userViewModel,
                onPostClick = { clickedPostId = it },
                onNotificationsClick = { notificationsClicked = true },
                onProfileClick = { profileClicked = true }
            )
        }

        composeTestRule.onNodeWithContentDescription("Perfil").assertExists()
        composeTestRule.onNodeWithContentDescription("Logo Hoppi").assertExists()
        composeTestRule.onNodeWithContentDescription("Notificações").assertExists()

        composeTestRule.onNodeWithContentDescription("Notificações").performClick()
        assert(notificationsClicked)
        composeTestRule.onNodeWithContentDescription("Perfil").performClick()
        assert(profileClicked)

        composeTestRule.onNodeWithText("Usuário 1").assertExists()
        composeTestRule.onNodeWithText("Primeiro post!").performClick()
        assert(clickedPostId == 1)

        composeTestRule.onNodeWithText("Usuário 2").assertExists()
        composeTestRule.onNodeWithText("Adoro programar.").performClick()
        assert(clickedPostId == 2)

        composeTestRule.onAllNodes(hasClickAction() and hasTestTag("like_icon"))
            .onFirst()
            .performClick()
        likeClickedCount++
        assert(likeClickedCount == 1)

        composeTestRule.onNodeWithText("Estudo").assertExists()
        composeTestRule.onNodeWithText("Info").assertExists()

        composeTestRule.onNodeWithText("2").assertExists()
        composeTestRule.onNodeWithText("1").assertExists()
    }
}

class FakeDataStore(private val prefs: Preferences = emptyPreferences()) :
    DataStore<Preferences> {
    override val data: Flow<Preferences> = flowOf(prefs)
    override suspend fun updateData(transform: suspend (t: Preferences) -> Preferences): Preferences = prefs
}