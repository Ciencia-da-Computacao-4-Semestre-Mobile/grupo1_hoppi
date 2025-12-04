package com.grupo1.hoppi.network.communities

import com.google.gson.annotations.SerializedName
import java.util.*

data class Community(
    val id: String,
    val name: String,
    val avatar: String,
    val description: String,
    @SerializedName("is_private") val isPrivate: Boolean,
    @SerializedName("requires_approval") val requiresApproval: Boolean,
    @SerializedName("created_at") val createdAt: Date,
    @SerializedName("created_by") val createdBy: User?,
    val members: List<CommunityMember>?
)

data class User(
    val id: String,
    val username: String,
    val display_name: String?
)

data class CommunityMember(
    val user_id: String,
    val role: String
)

data class CreateCommunityRequest(
    val name: String,
    val description: String,
    val is_private: Boolean = false,
    val requires_approval: Boolean = false,
    val avatar: String = "avatar_1"
)

data class UpdateCommunityRequest(
    val name: String? = null,
    val description: String? = null,
    val is_private: Boolean? = null,
    val requires_approval: Boolean? = null
)

data class CommunityRequestActionRequest(
    val action: String // "approve" ou "reject"
)

data class TransferOwnerRequest(
    val new_owner_user_id: String
)