package com.grupo1.hoppi.createpost

import androidx.compose.ui.graphics.Color
import com.grupo1.hoppi.ui.screens.mainapp.TagItem
import org.junit.Assert.*
import org.junit.Test

class CreatePostValidatorTest {

    @Test
    fun `texto valido retorna true`() {
        val result = CreatePostValidator.validatePostText("Um post válido")
        assertTrue(result)
    }

    @Test
    fun `texto vazio retorna false`() {
        val result = CreatePostValidator.validatePostText("")
        assertFalse(result)
    }

    @Test
    fun `texto muito longo retorna false`() {
        val text = "a".repeat(301)
        val result = CreatePostValidator.validatePostText(text)
        assertFalse(result)
    }

    @Test
    fun `tag valida retorna true`() {
        val tag = TagItem("Estudo", Color.Green, 0)
        val result = CreatePostValidator.validateTag(tag)
        assertTrue(result)
    }

    @Test
    fun `tag nula retorna false`() {
        val result = CreatePostValidator.validateTag(null)
        assertFalse(result)
    }

    @Test
    fun `pode publicar com texto e tag validos`() {
        val tag = TagItem("Venda", Color.Blue, 0)
        val result = CreatePostValidator.canPublish("Post válido", tag)
        assertTrue(result)
    }

    @Test
    fun `nao pode publicar com texto invalido`() {
        val tag = TagItem("Info", Color.Red, 0)
        val result = CreatePostValidator.canPublish("", tag)
        assertFalse(result)
    }

    @Test
    fun `nao pode publicar com tag nula`() {
        val result = CreatePostValidator.canPublish("Texto válido", null)
        assertFalse(result)
    }
}