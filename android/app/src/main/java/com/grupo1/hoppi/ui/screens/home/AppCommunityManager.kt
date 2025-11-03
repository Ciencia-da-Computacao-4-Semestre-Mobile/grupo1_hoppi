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
    val isOfficial: Boolean = false,
    val iconColor: Color = LightBlue
)

val initialHomeCommunities = listOf(
    Community("Comunidade de Estudantes", "Comunidade Oficial\nUm espaço para compartilhar ideias, tirar dúvidas e trocar experiências", isOfficial = true),
    Community("Comunidade UNASP-SP", "Criado por UNASP-SP\nAqui você encontra novidades, comunicados e eventos relacionados ao UNASP-SP."),
    Community("Comunidade da Graduação", "Criado por Graduação UNASP\nDiscussões, dicas acadêmicas e informações sobre o dia a dia da graduação."),
    Community("Comunidade Hoppi", "Comunidade Oficial\nCanal de comunicação da Hoppi! Fique por dentro das atualizações e novidades.", isOfficial = true),
    Community("Classificados - venda de produtos", "Criado por João\nEspaço destinado à compra e venda de produtos entre os alunos."),
)

val initialExploreCommunities = listOf(
    Community("Compra e Venda de Materiais", "Comunidade Oficial\nLorem ipsum is simply dummy text of the printing and typesetting industry", isOfficial = true),
    Community("Comunidade São Paulo", "Comunidade Oficial\nLorem ipsum is simply dummy text of the printing and typesetting industry", isOfficial = true),
    Community("Comunidade Alimentação", "Comunidade Oficial\nLorem ipsum is simply dummy text of the printing and typesetting industry", isOfficial = true),
    Community("Comunidade 1", "Criado por Fulano de Tal\nLorem ipsum is simply dummy text of the printing and typesetting industry"),
    Community("Comunidade 2", "Criado por Fulano de Tal\nLorem ipsum is simply dummy text of the printing and typesetting industry"),
    Community("Comunidade 3", "Criado por Fulano de Tal\nLorem ipsum is simply dummy text of the printing and typesetting industry"),
)

object AppCommunityManager {
    val userCreatedCommunities: SnapshotStateList<Community> = mutableStateListOf()

    fun createCommunity(name: String, description: String, privacy: String) {
        val newCommunity = Community(
            name = name,
            description = "Criado por Você\n$description",
            isOfficial = false,
            iconColor = LightBlue
        )
        userCreatedCommunities.add(newCommunity)
    }
}