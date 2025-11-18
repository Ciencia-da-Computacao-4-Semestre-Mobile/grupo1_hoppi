package com.grupo1.hoppi.communities

import com.grupo1.hoppi.ui.screens.mainapp.AppCommunityManager
import com.grupo1.hoppi.ui.screens.mainapp.Community

object CommunityValidator {

    fun isValidName(name: String?): Boolean {
        return !name.isNullOrBlank() && name.trim().length >= 3
    }

    fun isValidDescription(description: String?): Boolean {
        if (description == null) return true
        return description.trim().length >= 0
    }

    fun isValidPrivacy(privacy: String?): Boolean {
        return privacy == "Público" || privacy == "Privado"
    }

    fun isValidCommunity(community: Community?): Boolean {
        if (community == null) return false
        if (!isValidName(community.name)) return false
        if (!isValidDescription(community.description)) return false
        return true
    }

    fun canCreate(
        name: String?,
        description: String?,
        privacy: String?,
        creator: String?
    ): Boolean {
        return isValidName(name)
                && isValidDescription(description)
                && isValidPrivacy(privacy)
                && !creator.isNullOrBlank()
                && !AppCommunityManager.allCommunities.any { it.name.equals(name, true) }
    }

    fun canEdit(
        original: Community?,
        newName: String?,
        newDescription: String?,
        newPrivacy: String?
    ): Boolean {
        if (original == null) return false
        if (!isValidName(newName)) return false
        if (!isValidDescription(newDescription)) return false
        if (!isValidPrivacy(newPrivacy)) return false

        val exists = AppCommunityManager.allCommunities.any {
            it.id != original.id && it.name.equals(newName, true)
        }
        if (exists) return false

        return true
    }

    fun canFollow(currentStatus: Boolean): Boolean {
        return !currentStatus
    }

    fun canUnfollow(currentStatus: Boolean): Boolean {
        return currentStatus
    }

    fun communityExists(id: Int): Boolean {
        return AppCommunityManager.allCommunities.any { it.id == id }
    }

    fun isCreator(user: String?, community: Community?): Boolean {
        if (community == null || user == null) return false
        return community.creatorUsername == user
    }
}