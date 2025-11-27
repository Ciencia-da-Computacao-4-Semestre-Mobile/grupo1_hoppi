package com.grupo1.hoppi.communities

import com.grupo1.hoppi.ui.screens.mainapp.*
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class CommunityDetailedValidatorTest {

    private lateinit var communityPublic: Community
    private lateinit var communityPrivate: Community
    private lateinit var creatorCommunity: Community

    @Before
    fun setup() {
        AppCommunityManager.allCommunities.clear()
        AppCommunityManager.followedCommunities.clear()
        AppCommunityManager.userCreatedCommunities.clear()
        AppCommunityManager.pendingRequests.clear()

        communityPublic = Community(
            id = 1,
            name = "ComuPublica",
            description = "Desc",
            creatorUsername = "Alice",
            isPrivate = false
        )

        communityPrivate = Community(
            id = 2,
            name = "ComuPrivada",
            description = "Desc",
            creatorUsername = "Bob",
            isPrivate = true
        )

        creatorCommunity = Community(
            id = 3,
            name = "MinhaComu",
            description = "Sou o criador",
            creatorUsername = "Caio",
            isPrivate = false
        )

        AppCommunityManager.allCommunities.addAll(
            listOf(communityPublic, communityPrivate, creatorCommunity)
        )
    }

    @Test
    fun `communityExists retorna true para comunidade existente`() {
        assertTrue(CommunityValidator.communityExists(1))
        assertTrue(CommunityValidator.communityExists(3))
    }

    @Test
    fun `communityExists retorna false para ID inexistente`() {
        assertFalse(CommunityValidator.communityExists(999))
    }

    @Test
    fun `isCreator retorna true quando usuario eh criador`() {
        assertTrue(CommunityValidator.isCreator("Caio", creatorCommunity))
    }

    @Test
    fun `isCreator retorna false quando usuario nao eh criador`() {
        assertFalse(CommunityValidator.isCreator("Alice", creatorCommunity))
    }

    @Test
    fun `isCreator retorna false com usuario nulo`() {
        assertFalse(CommunityValidator.isCreator(null, creatorCommunity))
    }

    @Test
    fun `isCreator retorna false com comunidade nula`() {
        assertFalse(CommunityValidator.isCreator("Caio", null))
    }

    @Test
    fun `seguir comunidade modifica lista corretamente`() {
        assertFalse(AppCommunityManager.isFollowing("ComuPublica"))
        AppCommunityManager.followCommunity("ComuPublica")
        assertTrue(AppCommunityManager.isFollowing("ComuPublica"))
    }

    @Test
    fun `deixar de seguir remove corretamente da lista`() {
        AppCommunityManager.followCommunity("ComuPublica")
        assertTrue(AppCommunityManager.isFollowing("ComuPublica"))

        AppCommunityManager.unfollowCommunity("ComuPublica")
        assertFalse(AppCommunityManager.isFollowing("ComuPublica"))
    }

    @Test
    fun `enviar pedido de follow adiciona aos pendentes`() {
        assertFalse(AppCommunityManager.isRequestPending("ComuPrivada"))

        AppCommunityManager.sendFollowRequest("ComuPrivada")

        assertTrue(AppCommunityManager.isRequestPending("ComuPrivada"))
    }

    @Test
    fun `cancelar pedido de follow remove dos pendentes`() {
        AppCommunityManager.sendFollowRequest("ComuPrivada")
        assertTrue(AppCommunityManager.isRequestPending("ComuPrivada"))

        AppCommunityManager.cancelFollowRequest("ComuPrivada")

        assertFalse(AppCommunityManager.isRequestPending("ComuPrivada"))
    }

    @Test
    fun `publica nao seguida retorna NOT_MEMBER_PUBLIC`() {
        val status = when {
            AppCommunityManager.isFollowing(communityPublic.name) ->
                CommunityAccessStatus.MEMBER
            communityPublic.isPrivate ->
                CommunityAccessStatus.NOT_MEMBER_PRIVATE
            else -> CommunityAccessStatus.NOT_MEMBER_PUBLIC
        }
        assertEquals(CommunityAccessStatus.NOT_MEMBER_PUBLIC, status)
    }

    @Test
    fun `publica seguida retorna MEMBER`() {
        AppCommunityManager.followCommunity("ComuPublica")

        val status = when {
            AppCommunityManager.isFollowing("ComuPublica") ->
                CommunityAccessStatus.MEMBER
            else -> CommunityAccessStatus.NOT_MEMBER_PUBLIC
        }

        assertEquals(CommunityAccessStatus.MEMBER, status)
    }

    @Test
    fun `privada nao seguida retorna NOT_MEMBER_PRIVATE`() {
        val status = when {
            AppCommunityManager.isFollowing("ComuPrivada") ->
                CommunityAccessStatus.MEMBER
            communityPrivate.isPrivate ->
                CommunityAccessStatus.NOT_MEMBER_PRIVATE
            else -> CommunityAccessStatus.NOT_MEMBER_PUBLIC
        }
        assertEquals(CommunityAccessStatus.NOT_MEMBER_PRIVATE, status)
    }

    @Test
    fun `privada com pedido pendente retorna REQUEST_PENDING`() {
        AppCommunityManager.sendFollowRequest("ComuPrivada")

        val status = when {
            AppCommunityManager.isFollowing("ComuPrivada") -> CommunityAccessStatus.MEMBER
            AppCommunityManager.isRequestPending("ComuPrivada") -> CommunityAccessStatus.REQUEST_PENDING
            communityPrivate.isPrivate -> CommunityAccessStatus.NOT_MEMBER_PRIVATE
            else -> CommunityAccessStatus.NOT_MEMBER_PUBLIC
        }

        assertEquals(CommunityAccessStatus.REQUEST_PENDING, status)
    }

    @Test
    fun `handleCommunityAction - MEMBER vira NOT_MEMBER_PUBLIC`() {
        assertEquals(
            CommunityAccessStatus.NOT_MEMBER_PUBLIC,
            handleCommunityAction(
                currentStatus = CommunityAccessStatus.MEMBER,
                isPrivate = false
            )
        )
    }

    @Test
    fun `handleCommunityAction - NOT_MEMBER_PUBLIC vira MEMBER`() {
        assertEquals(
            CommunityAccessStatus.MEMBER,
            handleCommunityAction(
                currentStatus = CommunityAccessStatus.NOT_MEMBER_PUBLIC,
                isPrivate = false
            )
        )
    }

    @Test
    fun `handleCommunityAction - NOT_MEMBER_PRIVATE privada vira REQUEST_PENDING`() {
        assertEquals(
            CommunityAccessStatus.REQUEST_PENDING,
            handleCommunityAction(
                currentStatus = CommunityAccessStatus.NOT_MEMBER_PRIVATE,
                isPrivate = true
            )
        )
    }

    @Test
    fun `handleCommunityAction - NOT_MEMBER_PRIVATE publica vira MEMBER`() {
        assertEquals(
            CommunityAccessStatus.MEMBER,
            handleCommunityAction(
                currentStatus = CommunityAccessStatus.NOT_MEMBER_PRIVATE,
                isPrivate = false
            )
        )
    }

    @Test
    fun `handleCommunityAction - REQUEST_PENDING volta para NOT_MEMBER_PRIVATE`() {
        assertEquals(
            CommunityAccessStatus.NOT_MEMBER_PRIVATE,
            handleCommunityAction(
                currentStatus = CommunityAccessStatus.REQUEST_PENDING,
                isPrivate = true
            )
        )
    }

    @Test
    fun `usuario tem acesso se MEMBER`() {
        val accessStatus = CommunityAccessStatus.MEMBER
        val hasAccess = accessStatus == CommunityAccessStatus.MEMBER ||
                accessStatus == CommunityAccessStatus.NOT_MEMBER_PUBLIC

        assertTrue(hasAccess)
    }

    @Test
    fun `usuario tem acesso se NOT_MEMBER_PUBLIC`() {
        val accessStatus = CommunityAccessStatus.NOT_MEMBER_PUBLIC
        val hasAccess = accessStatus == CommunityAccessStatus.MEMBER ||
                accessStatus == CommunityAccessStatus.NOT_MEMBER_PUBLIC

        assertTrue(hasAccess)
    }

    @Test
    fun `usuario nao tem acesso se NOT_MEMBER_PRIVATE`() {
        val accessStatus = CommunityAccessStatus.NOT_MEMBER_PRIVATE
        val hasAccess = accessStatus == CommunityAccessStatus.MEMBER ||
                accessStatus == CommunityAccessStatus.NOT_MEMBER_PUBLIC

        assertFalse(hasAccess)
    }

    @Test
    fun `usuario nao tem acesso se REQUEST_PENDING`() {
        val accessStatus = CommunityAccessStatus.REQUEST_PENDING
        val hasAccess = accessStatus == CommunityAccessStatus.MEMBER ||
                accessStatus == CommunityAccessStatus.NOT_MEMBER_PUBLIC

        assertFalse(hasAccess)
    }
}