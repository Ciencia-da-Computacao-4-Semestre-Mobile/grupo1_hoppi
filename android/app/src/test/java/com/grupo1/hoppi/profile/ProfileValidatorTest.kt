package com.grupo1.hoppi.profile

import com.grupo1.hoppi.ui.screens.home.Post
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ProfileValidatorTest {

    private val validUsername = "@fulan.tal"
    private val validName = "Fulano de Tal"
    private val validBio = "Texto de bio de teste"

    private val samplePosts = listOf(
        Post(id = 1, username = "Caio", content = "Primeiro post", isLiked = false),
        Post(id = 2, username = "Benson", content = "Segundo post", isLiked = true)
    )

    @Test
    fun `validateProfile retorna true para perfil válido`() {
        val result = ProfileValidator.validateProfile(validUsername, validName, validBio)
        assertTrue(result)
    }

    @Test
    fun `validateProfile retorna false se houver campos vazios`() {
        val result = ProfileValidator.validateProfile("", validName, validBio)
        assertFalse(result)
    }

    @Test
    fun `validatePosts retorna true para posts válidos`() {
        val result = ProfileValidator.validatePosts(samplePosts)
        assertTrue(result)
    }

    @Test
    fun `validatePosts retorna false para lista vazia`() {
        val result = ProfileValidator.validatePosts(emptyList())
        assertFalse(result)
    }

    @Test
    fun `toggleLike inverte corretamente o estado de curtida`() {
        val updated = ProfileValidator.toggleLike(samplePosts, 1)
        val toggledPost = updated.first { it.id == 1 }
        assertTrue(toggledPost.isLiked)
    }

    @Test
    fun `toggleLike não altera outros posts`() {
        val updated = ProfileValidator.toggleLike(samplePosts, 1)
        val untouchedPost = updated.first { it.id == 2 }
        assertEquals(true, untouchedPost.isLiked)
    }
}