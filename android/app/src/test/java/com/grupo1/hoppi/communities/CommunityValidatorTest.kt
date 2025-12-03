package com.grupo1.hoppi.communities

import com.grupo1.hoppi.ui.screens.mainapp.AppCommunityManager
import com.grupo1.hoppi.ui.screens.mainapp.Community
import com.grupo1.hoppi.ui.screens.mainapp.LightBlue
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class CommunityScreenValidatorTest {

    @Before
    fun resetState() {
        AppCommunityManager.allCommunities.clear()
        AppCommunityManager.userCreatedCommunities.clear()
        AppCommunityManager.followedCommunities.clear()
        AppCommunityManager.pendingRequests.clear()

        AppCommunityManager.allCommunities.addAll(
            listOf(
                Community("Home1", "Desc", 1, isOfficial = false, creatorUsername = "A"),
                Community("Home2", "Desc", 2, isOfficial = false, creatorUsername = "B"),
                Community("Explore1", "Outra", 3, isOfficial = false, creatorUsername = "C"),
                Community("Explore2", "Outra", 4, isOfficial = false, creatorUsername = "D")
            )
        )

        AppCommunityManager.followedCommunities.addAll(listOf("Home1", "Home2"))
    }

    @Test
    fun `lista Home contém apenas comunidades seguidas`() {
        val home = AppCommunityManager.allCommunities.filter {
            AppCommunityManager.isFollowing(it.name)
        }
        assertEquals(2, home.size)
        assertTrue(home.any { it.name == "Home1" })
        assertTrue(home.any { it.name == "Home2" })
    }

    @Test
    fun `lista Explorar contém apenas comunidades não seguidas`() {
        val explore = AppCommunityManager.allCommunities.filter {
            !AppCommunityManager.isFollowing(it.name)
        }
        assertEquals(2, explore.size)
        assertTrue(explore.any { it.name == "Explore1" })
        assertTrue(explore.any { it.name == "Explore2" })
    }

    @Test
    fun `busca por nome retorna comunidade correspondente`() {
        val query = "home1"
        val result = AppCommunityManager.allCommunities.filter {
            it.name.lowercase().contains(query)
        }
        assertEquals(1, result.size)
        assertEquals("Home1", result.first().name)
    }

    @Test
    fun `busca por descricao retorna comunidades corretas`() {
        val query = "outra"
        val result = AppCommunityManager.allCommunities.filter {
            it.description.lowercase().contains(query)
        }
        assertEquals(2, result.size)
        assertTrue(result.any { it.name == "Explore1" })
    }

    @Test
    fun `busca vazia retorna lista completa`() {
        val query = ""
        val result = AppCommunityManager.allCommunities.filter {
            it.name.lowercase().contains(query) ||
                    it.description.lowercase().contains(query)
        }
        assertEquals(AppCommunityManager.allCommunities.size, result.size)
    }

    @Test
    fun `pode seguir uma comunidade nao seguida`() {
        val result = CommunityValidator.canFollow(false)
        assertTrue(result)
    }

    @Test
    fun `nao pode seguir comunidade ja seguida`() {
        val result = CommunityValidator.canFollow(true)
        assertFalse(result)
    }

    @Test
    fun `pode deixar de seguir uma comunidade seguida`() {
        val result = CommunityValidator.canUnfollow(true)
        assertTrue(result)
    }

    @Test
    fun `nao pode deixar de seguir se nao segue`() {
        val result = CommunityValidator.canUnfollow(false)
        assertFalse(result)
    }

    @Test
    fun `follow realmente adiciona na lista`() {
        AppCommunityManager.unfollowCommunity("Home1")
        assertFalse(AppCommunityManager.isFollowing("Home1"))

        AppCommunityManager.followCommunity("Home1")
        assertTrue(AppCommunityManager.isFollowing("Home1"))
    }

    @Test
    fun `unfollow realmente remove da lista`() {
        assertTrue(AppCommunityManager.isFollowing("Home2"))
        AppCommunityManager.unfollowCommunity("Home2")
        assertFalse(AppCommunityManager.isFollowing("Home2"))
    }

    @Test
    fun `communityExists retorna true para id existente`() {
        assertTrue(CommunityValidator.communityExists(1))
        assertTrue(CommunityValidator.communityExists(3))
    }

    @Test
    fun `communityExists retorna false para id inexistente`() {
        assertFalse(CommunityValidator.communityExists(999))
    }

    @Test
    fun `clicar em comunidade seguida retorna followState correto`() {
        val isFollowing = AppCommunityManager.isFollowing("Home1")
        assertTrue(isFollowing)
    }

    @Test
    fun `clicar em comunidade nao seguida retorna followState correto`() {
        val isFollowing = AppCommunityManager.isFollowing("Explore1")
        assertFalse(isFollowing)
    }

    @Test
    fun `criar comunidade valida`() {
        val result = CommunityValidator.canCreate(
            name = "NovaComu",
            description = "ABC",
            privacy = "Público",
            creator = "Caio"
        )

        assertTrue(result)
    }

    @Test
    fun `nao cria com nome repetido`() {
        val result = CommunityValidator.canCreate(
            name = "Home1",
            description = "ABC",
            privacy = "Público",
            creator = "Caio"
        )

        assertFalse(result)
    }

    @Test
    fun `nao cria com nome invalido`() {
        val result = CommunityValidator.canCreate(
            name = "",
            description = "ABC",
            privacy = "Público",
            creator = "Caio"
        )

        assertFalse(result)
    }

    @Test
    fun `editar comunidade com dados validos funciona`() {
        val original = AppCommunityManager.allCommunities.first()

        val result = CommunityValidator.canEdit(
            original = original,
            newName = "HomeRenomeada",
            newDescription = "Nova desc",
            newPrivacy = "Privado"
        )

        assertTrue(result)
    }

    @Test
    fun `nao pode editar para nome ja existente`() {
        val original = AppCommunityManager.allCommunities.first()

        val result = CommunityValidator.canEdit(
            original = original,
            newName = "Home2",
            newDescription = "ABC",
            newPrivacy = "Público"
        )

        assertFalse(result)
    }
}
