package com.grupo1.hoppi.search

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class SearchValidatorTest {

    @Test
    fun `retorna false quando a busca é vazia`() {
        val result = SearchValidator.isValidSearch("")
        assertFalse(result)
    }

    @Test
    fun `retorna false quando a busca tem menos de 2 caracteres`() {
        val result = SearchValidator.isValidSearch("a")
        assertFalse(result)
    }

    @Test
    fun `retorna true quando a busca é válida`() {
        val result = SearchValidator.isValidSearch("Item")
        assertTrue(result)
    }

    @Test
    fun `filtra itens corretamente quando a busca é válida`() {
        val items = listOf("Item 1", "Item 2", "Outro")
        val result = SearchValidator.filterItems(items, "Item")
        assertEquals(listOf("Item 1", "Item 2"), result)
    }

    @Test
    fun `retorna lista vazia quando a busca é inválida`() {
        val items = listOf("Item 1", "Item 2", "Outro")
        val result = SearchValidator.filterItems(items, " ")
        assertTrue(result.isEmpty())
    }
}