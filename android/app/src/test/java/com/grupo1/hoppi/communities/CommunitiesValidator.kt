package com.grupo1.hoppi.communities

import com.grupo1.hoppi.ui.screens.mainapp.CommunityAccessStatus
import com.grupo1.hoppi.ui.screens.mainapp.handleCommunityAction
import java.text.Normalizer

object CommunitiesValidator {

    private fun normalizeText(text: String): String {
        return Normalizer.normalize(text, Normalizer.Form.NFD)
            .replace(Regex("\\p{Mn}+"), "")
            .lowercase()
    }

    fun validateCommunitySearchFilter(
        isSearchActive: Boolean,
        searchText: String,
        currentList: List<String>
    ): List<String> {
        if (!isSearchActive || searchText.isBlank()) return currentList
        val query = normalizeText(searchText.trim())
        return currentList.filter {
            normalizeText(it).contains(query)
        }
    }

    fun validateCommunityAccessStatusChange(
        currentStatus: CommunityAccessStatus,
        isPrivate: Boolean
    ): CommunityAccessStatus {
        return handleCommunityAction(currentStatus, isPrivate)
    }

    fun validateCommunityCreation(
        name: String,
        description: String,
        privacy: String
    ): Boolean {
        if (name.isBlank()) return false
        if (privacy !in listOf("Público", "Privado")) return false
        return description.length <= 300
    }
}