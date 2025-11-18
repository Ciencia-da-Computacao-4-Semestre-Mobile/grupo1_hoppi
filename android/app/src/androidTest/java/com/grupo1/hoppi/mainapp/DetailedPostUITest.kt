package com.grupo1.hoppi.ui.screens.mainapp

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.navigation.compose.rememberNavController
import com.grupo1.hoppi.mainapp.FakeDataStore
import com.grupo1.hoppi.ui.screens.home.PostsViewModel
import com.grupo1.hoppi.ui.screens.home.UserViewModel
import org.junit.Rule
import org.junit.Test

class DetailedPostUITest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val testPost = DetailedPost(
        id = 1,
        username = "Fulano de Tal",
        userHandle = "@fulan.tal",
        content = "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s.",
        timestamp = "21:30",
        date = "19 set 2025",
        likes = "10 K",
        commentsCount = "1,5 K",
        shares = "2 K"
    )

    private val fakeDataStore = FakeDataStore()
    private val userViewModel = UserViewModel(fakeDataStore)

    @Test
    fun detailedPost_interactionsWork() {
        val postsViewModel = PostsViewModel().apply {
            addPost(
                content = testPost.content,
                username = testPost.username,
                isSale = false,
                tag = testPost.tag
            )
        }

        composeTestRule.setContent {
            PostScreen(
                postId = postsViewModel.posts.first().id,
                navController = rememberNavController(),
                postsViewModel = postsViewModel,
                userViewModel = userViewModel
            )
        }

        val post = postsViewModel.posts.first()

        composeTestRule.onNodeWithTag("PostPrincipal").assertExists()
        composeTestRule.onNodeWithTag("PostContent_${post.id}")
            .assertExists()
            .assertTextEquals(post.content)

        composeTestRule.onAllNodes(
            hasText(post.likes.toString()) and hasClickAction()
        ).onFirst().performClick()

        composeTestRule.onAllNodes(
            hasText(post.comments.toString()) and hasClickAction()
        ).get(0).performClick()

        composeTestRule.waitUntil(timeoutMillis = 5000) {
            composeTestRule.onAllNodesWithTag("CommentField").fetchSemanticsNodes().isNotEmpty()
        }

        composeTestRule.onNodeWithTag("CommentField")
            .assertExists()
            .performTextInput("Comentário de teste")

        composeTestRule.onNodeWithTag("SendCommentButton")
            .performClick()

        composeTestRule.onNodeWithText("Comentário de teste").assertExists()

        composeTestRule.onAllNodes(
            hasContentDescription("Mais opções") and hasClickAction()
        ).get(0).performClick()

        composeTestRule.onAllNodes(hasText("Denunciar Post") and hasClickAction())
            .get(0)
            .performClick()

        composeTestRule.onNodeWithText("Por que você quer denunciar?").assertExists()

        composeTestRule.onAllNodes(
            hasContentDescription("Mais opções") and hasClickAction()
        ).get(0).performClick()

        composeTestRule.onAllNodes(hasText("Excluir Post") and hasClickAction())
            .get(0)
            .performClick()

        composeTestRule.onNodeWithText("Tem certeza que deseja excluir este post?").assertExists()
    }
}