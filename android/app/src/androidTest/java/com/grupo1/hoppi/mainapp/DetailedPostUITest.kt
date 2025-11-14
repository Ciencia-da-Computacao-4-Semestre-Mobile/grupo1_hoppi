package com.grupo1.hoppi.ui.screens.mainapp

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.navigation.compose.rememberNavController
import com.grupo1.hoppi.ui.screens.home.PostsViewModel
import org.junit.Rule
import org.junit.Test

class DetailedPostUITest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun detailedPost_interactionsWork() {
        SelectedPostHolder.selectedPost = mockDetailedPost

        val postsViewModel = PostsViewModel().apply {
            addPost(
                content = mockDetailedPost.content,
                username = mockDetailedPost.username,
                isSale = false,
                tag = mockDetailedPost.tag
            )
        }

        composeTestRule.setContent {
            PostScreen(
                postId = postsViewModel.posts.first().id,
                navController = rememberNavController(),
                postsViewModel = postsViewModel
            )
        }

        val post = postsViewModel.posts.first()

        composeTestRule.onNodeWithTag("PostPrincipal").assertExists()
        composeTestRule.onNodeWithTag("PostContent_${post.id}")
            .assertExists()
            .assertTextContains(post.content.take(50), substring = true)

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
