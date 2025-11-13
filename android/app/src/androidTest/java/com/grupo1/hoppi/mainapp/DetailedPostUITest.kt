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

        // ===== Verifica post principal =====
        composeTestRule.onNodeWithTag("PostPrincipal").assertExists()
        composeTestRule.onNodeWithTag("PostContent_${post.id}")
            .assertExists()
            .assertTextContains(post.content.take(50), substring = true)

        // ===== Likes =====
        composeTestRule.onAllNodes(
            hasText(post.likes.toString()) and hasClickAction()
        ).onFirst().performClick()

        // ===== Comentários =====
        val commentField = composeTestRule.onAllNodes(hasSetTextAction()).onFirst()
        commentField.performTextInput("Comentário de teste")

        composeTestRule.onAllNodes(hasText("Enviar") and hasClickAction())
            .get(0)
            .performClick()

        composeTestRule.onNodeWithText("Comentário de teste").assertExists()

        // ===== Denunciar / Excluir =====
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
