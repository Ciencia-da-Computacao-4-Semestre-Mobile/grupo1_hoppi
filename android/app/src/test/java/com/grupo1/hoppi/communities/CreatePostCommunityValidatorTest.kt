package com.grupo1.hoppi.communities

import com.grupo1.hoppi.ui.screens.mainapp.AppCommunityManager
import com.grupo1.hoppi.ui.screens.mainapp.Community
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class CreatePostCommunityValidatorTest {

    @Before
    fun setup() {
        AppCommunityManager.allCommunities.clear()
        AppCommunityManager.allCommunities.add(
            Community(
                id = 1,
                name = "Comunidade Existente",
                description = "descricao",
                creatorUsername = "User123",
                iconColor = androidx.compose.ui.graphics.Color.Red,
                isOfficial = false
            )
        )
    }

    @Test
    fun `canCreate retorna true quando todos os campos são válidos e nome ainda não existe`() {
        val result = CommunityValidator.canCreate(
            name = "Nova Comunidade",
            description = "Sobre algo",
            privacy = "Público",
            creator = "Fulano"
        )
        assertTrue(result)
    }

    @Test
    fun `canCreate retorna false quando nome é nulo`() {
        val result = CommunityValidator.canCreate(
            name = null,
            description = "abc",
            privacy = "Público",
            creator = "Fulano"
        )
        assertFalse(result)
    }

    @Test
    fun `canCreate retorna false quando nome é muito curto`() {
        val result = CommunityValidator.canCreate(
            name = "Oi",
            description = "abc",
            privacy = "Público",
            creator = "Fulano"
        )
        assertFalse(result)
    }

    @Test
    fun `canCreate retorna false quando privacidade é inválida`() {
        val result = CommunityValidator.canCreate(
            name = "Comunidade Boa",
            description = "desc",
            privacy = "Protegido",
            creator = "Fulano"
        )
        assertFalse(result)
    }

    @Test
    fun `canCreate retorna false quando creator é nulo`() {
        val result = CommunityValidator.canCreate(
            name = "Comunidade Legal",
            description = "desc",
            privacy = "Público",
            creator = null
        )
        assertFalse(result)
    }

    @Test
    fun `canCreate retorna false quando já existe comunidade com mesmo nome`() {
        val result = CommunityValidator.canCreate(
            name = "Comunidade Existente",
            description = "aaa",
            privacy = "Público",
            creator = "Fulano"
        )
        assertFalse(result)
    }

    @Test
    fun `canCreate ignora maiúsculas e minúsculas ao verificar nome duplicado`() {
        val result = CommunityValidator.canCreate(
            name = "comunidade existente",
            description = "desc",
            privacy = "Privado",
            creator = "Fulano"
        )
        assertFalse(result)
    }
}