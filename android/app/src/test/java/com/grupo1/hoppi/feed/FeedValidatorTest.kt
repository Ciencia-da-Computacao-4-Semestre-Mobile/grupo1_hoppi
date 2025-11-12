package com.grupo1.hoppi.ui.screens.mainapp

import com.grupo1.hoppi.ui.screens.home.Post
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class FeedValidatorTest {

    private val samplePosts = listOf(
        Post(id = 1, username = "Caio", content = "Post de teste", isLiked = false),
        Post(id = 2, username = "Benson", content = "Outro post", isLiked = true)
    )

    @Test
    fun `validatePosts retorna true para posts válidos`() {
        val result = FeedValidator.validatePosts(samplePosts)
        assertTrue(result)
    }

    @Test
    fun `validatePosts retorna false para lista vazia`() {
        val result = FeedValidator.validatePosts(emptyList())
        assertFalse(result)
    }

    @Test
    fun `validatePosts retorna false se houver campos vazios`() {
        val invalidPosts = listOf(Post(id = 1, username = "", content = "", isLiked = false))
        val result = FeedValidator.validatePosts(invalidPosts)
        assertFalse(result)
    }

    @Test
    fun `toggleLike inverte o estado de curtida corretamente`() {
        val updatedPosts = FeedValidator.toggleLike(samplePosts, 1)
        val toggledPost = updatedPosts.first { it.id == 1 }
        assertTrue(toggledPost.isLiked)
    }

    @Test
    fun `toggleLike não altera outros posts`() {
        val updatedPosts = FeedValidator.toggleLike(samplePosts, 1)
        val untouchedPost = updatedPosts.first { it.id == 2 }
        assertEquals(true, untouchedPost.isLiked)
    }
}