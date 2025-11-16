package com.grupo1.hoppi.ui.screens.mainapp

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.graphics.Color

val GrayColor = Color(0xFFDBD8D6)
val HoppiOrange = Color(0xFFEC8445)
val LightBlue = Color(0xFF9CBDC6)
val ItemCardBackground = Color(0xFFDBDBDB)

data class Community(
    val name: String,
    val description: String,
    val id: Int,
    val isOfficial: Boolean = false,
    val isPrivate: Boolean = false,
    val iconColor: Color = LightBlue,
    val creatorUsername: String = "Sistema"
)

val initialHomeCommunities = listOf(
    Community(
        name = "Comunidade de Estudantes",
        description = "Um espaço para compartilhar ideias, tirar dúvidas e trocar experiências",
        id = 1,
        isOfficial = true,
        creatorUsername = "Hoppi"
    ),
    Community(
        name = "Comunidade UNASP-SP",
        description = "Aqui você encontra novidades, comunicados e eventos relacionados ao UNASP-SP.",
        id = 2,
        isPrivate = true,
        creatorUsername = "UNASP-SP"
    ),
    Community(
        name = "Comunidade da Graduação",
        description = "Discussões, dicas acadêmicas e informações sobre o dia a dia da graduação.",
        id = 3,
        creatorUsername = "Graduação UNASP"
    ),
    Community(
        name = "Comunidade Hoppi",
        description = "Canal de comunicação da Hoppi! Fique por dentro das atualizações e novidades.",
        id = 4,
        isOfficial = true,
        creatorUsername = "Hoppi"
    ),
    Community(
        name = "Classificados - venda de produtos",
        description = "Espaço destinado à compra e venda de produtos entre os alunos.",
        id = 5,
        creatorUsername = "João"
    )
)

val initialExploreCommunities = listOf(
    Community(
        name = "Compra e Venda de Materiais",
        description = "Lorem ipsum is simply dummy text of the printing and typesetting industry",
        id = 6,
        isOfficial = true,
        creatorUsername = "Hoppi"
    ),
    Community(
        name = "Comunidade São Paulo",
        description = "Lorem ipsum is simply dummy text of the printing and typesetting industry",
        id = 7,
        isOfficial = true,
        creatorUsername = "Equipe SP"
    ),
    Community(
        name = "Comunidade Alimentação",
        description = "Lorem ipsum is simply dummy text of the printing and typesetting industry",
        id = 8,
        isOfficial = true,
        creatorUsername = "Hoppi"
    ),
    Community(
        name = "Comunidade 1",
        description = "Lorem ipsum is simply dummy text of the printing and typesetting industry",
        id = 9,
        creatorUsername = "Fulano de Tal"
    ),
    Community(
        name = "Comunidade 2",
        description = "Lorem ipsum is simply dummy text of the printing and typesetting industry",
        id = 10,
        creatorUsername = "User 2"
    ),
    Community(
        name = "Comunidade 3",
        description = "Lorem ipsum is simply dummy text of the printing and typesetting industry",
        id = 11,
        creatorUsername = "User 3"
    )
)

fun findCommunityById(id: Int): Community? {
    return AppCommunityManager.allCommunities.find { it.id == id }
}

object AppCommunityManager {
    val allCommunities: SnapshotStateList<Community> = mutableStateListOf<Community>().apply {
        addAll(initialHomeCommunities)
        addAll(initialExploreCommunities)
    }

    val followedCommunities: SnapshotStateList<String> = mutableStateListOf<String>().apply {
        initialHomeCommunities.forEach { add(it.name) }
    }

    val userCreatedCommunities: SnapshotStateList<Community> = mutableStateListOf()

    val pendingRequests = mutableSetOf<String>()

    fun createCommunity(
        name: String,
        description: String,
        privacy: String,
        creatorUsername: String
    ) {
        val newId = (allCommunities.maxOfOrNull { it.id } ?: 0) + 1

        val newCommunity = Community(
            name = name,
            description = description,
            id = newId,
            isPrivate = privacy.lowercase() == "privado" || privacy.lowercase() == "private",
            creatorUsername = creatorUsername,
            iconColor = LightBlue,
            isOfficial = false
        )

        allCommunities.add(newCommunity)
        userCreatedCommunities.add(newCommunity)

        if (!followedCommunities.contains(name)) {
            followedCommunities.add(name)
        }
    }

    fun sendFollowRequest(name: String) {
        pendingRequests.add(name)
    }

    fun cancelFollowRequest(name: String) {
        pendingRequests.remove(name)
    }

    fun isRequestPending(name: String): Boolean {
        return pendingRequests.contains(name)
    }

    fun followCommunity(name: String) {
        if (!followedCommunities.contains(name)) {
            followedCommunities.add(name)
        }
    }

    fun unfollowCommunity(name: String) {
        followedCommunities.remove(name)
    }

    fun isFollowing(name: String): Boolean {
        return followedCommunities.contains(name)
    }

    fun deleteCommunity(name: String) {
        allCommunities.removeAll { it.name == name && !it.isOfficial }
        userCreatedCommunities.removeAll { it.name == name }
        followedCommunities.remove(name)
    }
}
