package com.grupo1.hoppi.communities

import com.grupo1.hoppi.ui.screens.mainapp.AppCommunityManager
import com.grupo1.hoppi.ui.screens.mainapp.Community
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class CreateCommunityValidatorTest {

    @Before
    fun setup() {
        AppCommunityManager.allCommunities.clear()
        AppCommunityManager.followedCommunities.clear()
        AppCommunityManager.userCreatedCommunities.clear()
        AppCommunityManager.pendingRequests.clear()

        AppCommunityManager.allCommunities.add(
            Community(
                id = 1,
                name = "Existente",
                description = "Teste",
                creatorUsername = "Alice"
            )
        )
    }

    @Test
    fun `criação válida retorna true`() {
        val result = CommunityValidator.canCreate(
            name = "Nova Comunidade",
            description = "Desc",
            privacy = "Público",
            creator = "UserX"
        )
        assertTrue(result)
    }

    @Test
    fun `nome nulo retorna false`() {
        val result = CommunityValidator.canCreate(
            name = null,
            description = "Desc",
            privacy = "Público",
            creator = "UserX"
        )
        assertFalse(result)
    }

    @Test
    fun `nome vazio retorna false`() {
        val result = CommunityValidator.canCreate(
            name = "",
            description = "Desc",
            privacy = "Privado",
            creator = "UserX"
        )
        assertFalse(result)
    }

    @Test
    fun `nome com menos de 3 caracteres retorna false`() {
        val result = CommunityValidator.canCreate(
            name = "Oi",
            description = "Teste",
            privacy = "Público",
            creator = "UserX"
        )
        assertFalse(result)
    }

    @Test
    fun `descricao nula é aceita`() {
        val result = CommunityValidator.canCreate(
            name = "Comunidade Legal",
            description = null,
            privacy = "Público",
            creator = "UserX"
        )
        assertTrue(result)
    }

    @Test
    fun `privacidade inválida retorna false`() {
        val result = CommunityValidator.canCreate(
            name = "Comunidade",
            description = "Desc",
            privacy = "XPTO",
            creator = "UserX"
        )
        assertFalse(result)
    }

    @Test
    fun `creator nulo retorna false`() {
        val result = CommunityValidator.canCreate(
            name = "Comunidade",
            description = "Desc",
            privacy = "Privado",
            creator = null
        )
        assertFalse(result)
    }

    @Test
    fun `creator vazio retorna false`() {
        val result = CommunityValidator.canCreate(
            name = "Comunidade",
            description = "Desc",
            privacy = "Privado",
            creator = ""
        )
        assertFalse(result)
    }

    @Test
    fun `criação falha quando nome já existe`() {
        val result = CommunityValidator.canCreate(
            name = "Existente",
            description = "Qualquer",
            privacy = "Público",
            creator = "UserX"
        )
        assertFalse(result)
    }

    @Test
    fun `criação falha quando nome existente é igual ignorando case`() {
        val result = CommunityValidator.canCreate(
            name = "existente",
            description = "Qualquer",
            privacy = "Privado",
            creator = "UserX"
        )
        assertFalse(result)
    }
}