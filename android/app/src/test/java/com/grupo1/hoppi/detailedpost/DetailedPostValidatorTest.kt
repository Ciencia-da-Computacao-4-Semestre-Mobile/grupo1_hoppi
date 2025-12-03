package com.grupo1.hoppi.detailedpost

import androidx.compose.ui.graphics.Color
import com.grupo1.hoppi.ui.screens.mainapp.Comment
import com.grupo1.hoppi.ui.screens.mainapp.DetailedPost
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
    fun `post com username vazio retorna false`() {
        val post = mockDetailedPost.copy(username = "")
        assertFalse(DetailedPostValidator.isValidPost(post))
    }

    @Test
    fun `post com userHandle vazio retorna false`() {
        val post = mockDetailedPost.copy(userHandle = "")
        assertFalse(DetailedPostValidator.isValidPost(post))
    }

    @Test
    fun `post com likes vazio retorna false`() {
        val post = mockDetailedPost.copy(likes = "")
        assertFalse(DetailedPostValidator.isValidPost(post))
    }

    @Test
    fun `post com commentsCount vazio retorna false`() {
        val post = mockDetailedPost.copy(commentsCount = "")
        assertFalse(DetailedPostValidator.isValidPost(post))
    }

    @Test
    fun `post com shares vazio retorna false`() {
        val post = mockDetailedPost.copy(shares = "")
        assertFalse(DetailedPostValidator.isValidPost(post))
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

    @Test
    fun `comentário com username vazio retorna false`() {
        val comment = mockComments.first().copy(username = "")
        val result = DetailedPostValidator.isValidComment(comment)
        assertFalse(result)
    }

    @Test
    fun `comentário com userHandle vazio retorna false`() {
        val comment = mockComments.first().copy(userHandle = "")
        val result = DetailedPostValidator.isValidComment(comment)
        assertFalse(result)
    }

    @Test
    fun `tag vazia retorna false`() {
        assertFalse(DetailedPostValidator.isValidTag(""))
    }

    @Test
    fun `cobre construtores de data classes`() {
        val post = DetailedPost(
            id = 99,
            username = "Teste",
            userHandle = "@teste",
            content = "conteúdo",
            timestamp = "agora",
            date = "hoje",
            likes = "1",
            commentsCount = "2",
            shares = "3",
            tag = "Estudo",
            isLiked = true,
            avatarColor = Color(0xFF000000)
        )
        assertTrue(DetailedPostValidator.isValidPost(post))

        val comment = Comment(
            id = 88,
            username = "Alguém",
            userHandle = "@alguem",
            replyToHandle = "@teste",
            content = "Comentário",
            likes = "10",
            commentsCount = "0",
            shares = "1",
            isLiked = true,
            avatarColor = Color(0xFF000000)
        )
        assertTrue(DetailedPostValidator.isValidComment(comment))
    }

}