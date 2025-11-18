package com.grupo1.hoppi.communities

import com.grupo1.hoppi.ui.screens.mainapp.AppCommunityManager
import com.grupo1.hoppi.ui.screens.mainapp.Community
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import androidx.compose.ui.graphics.Color

class EditCommunityValidatorTest {

    private lateinit var original: Community

    @Before
    fun setup() {
        AppCommunityManager.allCommunities.clear()
        original = Community(
            id = 1,
            name = "Comunidade A",
            description = "Descrição",
            creatorUsername = "User1",
            iconColor = Color.Blue
        )
        AppCommunityManager.allCommunities.add(original)
        AppCommunityManager.allCommunities.add(
            Community(
                id = 2,
                name = "Comunidade B",
                description = "Outra",
                creatorUsername = "User2",
                iconColor = Color.Red
            )
        )
    }

    @Test
    fun testCanEditSuccess() {
        val result = CommunityValidator.canEdit(
            original,
            "Comunidade Nova",
            "Nova descrição",
            "Privado"
        )
        assertTrue(result)
    }

    @Test
    fun testOriginalNull() {
        val result = CommunityValidator.canEdit(
            null,
            "Nome",
            "Desc",
            "Público"
        )
        assertFalse(result)
    }

    @Test
    fun testNameTooShort() {
        val result = CommunityValidator.canEdit(
            original,
            "Oi",
            "Desc",
            "Público"
        )
        assertFalse(result)
    }

    @Test
    fun testNameBlank() {
        val result = CommunityValidator.canEdit(
            original,
            "  ",
            "Desc",
            "Privado"
        )
        assertFalse(result)
    }

    @Test
    fun testDescriptionNullAllowed() {
        val result = CommunityValidator.canEdit(
            original,
            "Nome Válido",
            null,
            "Público"
        )
        assertTrue(result)
    }

    @Test
    fun testDescriptionEmptyAllowed() {
        val result = CommunityValidator.canEdit(
            original,
            "Nome Válido",
            "",
            "Público"
        )
        assertTrue(result)
    }

    @Test
    fun testDescriptionWhitespaceAllowed() {
        val result = CommunityValidator.canEdit(
            original,
            "Nome Válido",
            "   ",
            "Público"
        )
        assertTrue(result)
    }

    @Test
    fun testPrivacyInvalid() {
        val result = CommunityValidator.canEdit(
            original,
            "Nome válido",
            "Desc",
            "Protegido"
        )
        assertFalse(result)
    }

    @Test
    fun testDuplicateNameInOtherCommunity() {
        val result = CommunityValidator.canEdit(
            original,
            "Comunidade B",
            "Desc",
            "Privado"
        )
        assertFalse(result)
    }

    @Test
    fun testSameNameAsOriginalAllowed() {
        val result = CommunityValidator.canEdit(
            original,
            "Comunidade A",
            "Mudou",
            "Privado"
        )
        assertTrue(result)
    }

    @Test
    fun testDuplicateNameCaseInsensitive() {
        AppCommunityManager.allCommunities.add(
            Community(
                id = 3,
                name = "comunidade c",
                description = "x",
                creatorUsername = "u",
                iconColor = Color.Green
            )
        )
        val result = CommunityValidator.canEdit(
            original,
            "Comunidade C",
            "Desc",
            "Público"
        )
        assertFalse(result)
    }

    @Test
    fun testEditNameVeryLong() {
        val longName = "A".repeat(80)
        val result = CommunityValidator.canEdit(
            original,
            longName,
            "Desc",
            "Público"
        )
        assertTrue(result)
    }
}