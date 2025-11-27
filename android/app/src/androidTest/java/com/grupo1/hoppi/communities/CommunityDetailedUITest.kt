package com.grupo1.hoppi.communities

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.*
import androidx.navigation.NavController
import androidx.navigation.testing.TestNavHostController
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences
import com.grupo1.hoppi.ui.screens.home.PostsViewModel
import com.grupo1.hoppi.ui.screens.home.UserViewModel
import com.grupo1.hoppi.ui.screens.mainapp.CommunityDetailScreen
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import org.junit.Rule
import org.junit.Test

class CommunityDetailedUITest {

    @get:Rule
    val rule = createAndroidComposeRule<ComponentActivity>()

    class FakeDataStore : DataStore<Preferences> {
        override val data: Flow<Preferences> = flowOf(emptyPreferences())
        override suspend fun updateData(transform: suspend (Preferences) -> Preferences): Preferences {
            return transform(emptyPreferences())
        }
    }

    private fun fakeNavController(): NavController {
        return TestNavHostController(rule.activity)
    }

    @Test
    fun communityDetailScreen_showsCommunityName() {
        val fakeUser = UserViewModel(FakeDataStore())
        val postsViewModel = PostsViewModel()
        val nav = fakeNavController()

        rule.setContent {
            CommunityDetailScreen(
                navController = nav,
                communityId = 1,
                userViewModel = fakeUser,
                postsViewModel = postsViewModel
            )
        }

        rule.onNodeWithText("Comunidade Não Encontrada").assertDoesNotExist()
        rule.onNodeWithText("Comunidade de Estudantes").assertExists()
    }

    @Test
    fun communityDetailScreen_showsFollowButtonWhenNotMember() {
        val fakeUser = UserViewModel(FakeDataStore())
        val postsViewModel = PostsViewModel()
        val nav = fakeNavController()

        rule.setContent {
            CommunityDetailScreen(
                navController = nav,
                communityId = 8,
                userViewModel = fakeUser,
                postsViewModel = postsViewModel
            )
        }

        rule.onNodeWithText("Seguir").assertExists()
    }

    @Test
    fun communityDetailScreen_showsPostsWhenHasAccess() {
        val fakeUser = UserViewModel(FakeDataStore())
        val postsViewModel = PostsViewModel()
        postsViewModel.addCommunityPost(
            content = "Primeiro post!",
            username = "Caio",
            isSale = false,
            tag = null,
            communityId = 8
        )

        val nav = fakeNavController()

        rule.setContent {
            CommunityDetailScreen(
                navController = nav,
                communityId = 8,
                userViewModel = fakeUser,
                postsViewModel = postsViewModel
            )
        }

        rule.onNodeWithText("Primeiro post!").assertExists()
    }

    @Test
    fun communityDetailScreen_privateCommunity_showsAccessDenied() {
        val fakeUser = UserViewModel(FakeDataStore())
        val postsViewModel = PostsViewModel()
        val nav = fakeNavController()

        rule.setContent {
            CommunityDetailScreen(
                navController = nav,
                communityId = 10,
                userViewModel = fakeUser,
                postsViewModel = postsViewModel
            )
        }

        rule.onNodeWithContentDescription("Acesso Negado").assertExists()
    }
}