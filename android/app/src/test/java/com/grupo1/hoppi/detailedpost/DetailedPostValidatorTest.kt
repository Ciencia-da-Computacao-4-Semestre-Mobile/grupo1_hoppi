package com.grupo1.hoppi.detailedpost

import com.grupo1.hoppi.ui.screens.mainapp.mockComments
import com.grupo1.hoppi.ui.screens.mainapp.mockDetailedPost
import org.junit.Assert.*
import org.junit.Test

class DetailedPostValidatorTest {

    @Test
    fun `post válido retorna true`() {
        val post = mockDetailedPost
        val result = DetailedPostValidator.isValidPost(post)
        assertTrue(result)
    }

    @Test
    fun `post nulo retorna false`() {
        val result = DetailedPostValidator.isValidPost(null)
        assertFalse(result)
    }

    @Test
    fun `post com conteúdo vazio retorna false`() {
        val post = mockDetailedPost.copy(content = "")
        val result = DetailedPostValidator.isValidPost(post)
        assertFalse(result)
    }

    @Test
    fun `tag válida retorna true`() {
        assertTrue(DetailedPostValidator.isValidTag("Estudo"))
        assertTrue(DetailedPostValidator.isValidTag("Venda"))
        assertTrue(DetailedPostValidator.isValidTag("Info"))
    }

    @Test
    fun `tag inválida retorna false`() {
        assertFalse(DetailedPostValidator.isValidTag("Outro"))
        assertFalse(DetailedPostValidator.isValidTag(null))
    }

    @Test
    fun `comentário válido retorna true`() {
        val comment = mockComments.first()
        val result = DetailedPostValidator.isValidComment(comment)
        assertTrue(result)
    }

    @Test
    fun `comentário nulo retorna false`() {
        val result = DetailedPostValidator.isValidComment(null)
        assertFalse(result)
    }

    @Test
    fun `comentário com texto vazio retorna false`() {
        val comment = mockComments.first().copy(content = "")
        val result = DetailedPostValidator.isValidComment(comment)
        assertFalse(result)
    }
}