package com.grupo1.hoppi.communities

import com.grupo1.hoppi.ui.screens.mainapp.CommunityAccessStatus
import org.junit.Assert.*
import org.junit.Test

class CommunitiesValidatorTest {

    @Test
    fun testValidateCommunitySearchFilter_inactiveSearch_returnsFullList() {
        val list = listOf("Games", "Música", "Tecnologia")
        val result = CommunitiesValidator.validateCommunitySearchFilter(false, "mu", list)
        assertEquals(list, result)
    }

    @Test
    fun testValidateCommunitySearchFilter_activeSearch_returnsFilteredList() {
        val list = listOf("Games", "Música", "Tecnologia")
        val result = CommunitiesValidator.validateCommunitySearchFilter(true, "mu", list)
        assertEquals(listOf("Música"), result)
    }

    @Test
    fun testValidateCommunitySearchFilter_blankQuery_returnsFullList() {
        val list = listOf("Games", "Música")
        val result = CommunitiesValidator.validateCommunitySearchFilter(true, " ", list)
        assertEquals(list, result)
    }

    @Test
    fun testValidateCommunityAccessStatusChange_publicCommunity_toggleMemberStatus() {
        val initial = CommunityAccessStatus.NOT_MEMBER_PUBLIC
        val result = CommunitiesValidator.validateCommunityAccessStatusChange(initial, false)
        assertEquals(CommunityAccessStatus.MEMBER, result)
    }

    @Test
    fun testValidateCommunityAccessStatusChange_privateCommunity_requestPending() {
        val initial = CommunityAccessStatus.NOT_MEMBER_PRIVATE
        val result = CommunitiesValidator.validateCommunityAccessStatusChange(initial, true)
        assertEquals(CommunityAccessStatus.REQUEST_PENDING, result)
    }

    @Test
    fun testValidateCommunityAccessStatusChange_requestPending_backToNotMemberPrivate() {
        val initial = CommunityAccessStatus.REQUEST_PENDING
        val result = CommunitiesValidator.validateCommunityAccessStatusChange(initial, true)
        assertEquals(CommunityAccessStatus.NOT_MEMBER_PRIVATE, result)
    }

    @Test
    fun testValidateCommunityCreation_validData_returnsTrue() {
        val result = CommunitiesValidator.validateCommunityCreation(
            name = "Tech",
            description = "Comunidade sobre tecnologia",
            privacy = "Público"
        )
        assertTrue(result)
    }

    @Test
    fun testValidateCommunityCreation_blankName_returnsFalse() {
        val result = CommunitiesValidator.validateCommunityCreation(
            name = "",
            description = "Algo",
            privacy = "Público"
        )
        assertFalse(result)
    }

    @Test
    fun testValidateCommunityCreation_invalidPrivacy_returnsFalse() {
        val result = CommunitiesValidator.validateCommunityCreation(
            name = "Nova",
            description = "Teste",
            privacy = "Secreto"
        )
        assertFalse(result)
    }

    @Test
    fun testValidateCommunityCreation_longDescription_returnsFalse() {
        val longDescription = "a".repeat(301)
        val result = CommunitiesValidator.validateCommunityCreation(
            name = "Nova",
            description = longDescription,
            privacy = "Privado"
        )
        assertFalse(result)
    }
}