package com.grupo1.hoppi.ui.screens.mainapp

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.grupo1.hoppi.ui.screens.home.MainAppDestinations
import com.grupo1.hoppi.R
import com.grupo1.hoppi.ui.components.mainapp.BottomNavBar

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

val homeCommunities = listOf(
    Community("Comunidade de Estudantes", "Comunidade Oficial\nUm espaço para compartilhar ideias, tirar dúvidas e trocar experiências", isOfficial = true),
    Community("Comunidade UNASP-SP", "Criado por UNASP-SP\nAqui você encontra novidades, comunicados e eventos relacionados ao UNASP-SP."),
    Community("Comunidade da Graduação", "Criado por Graduação UNASP\nDiscussões, dicas acadêmicas e informações sobre o dia a dia da graduação."),
    Community("Comunidade Hoppi", "Comunidade Oficial\nCanal de comunicação da Hoppi! Fique por dentro das atualizações e novidades.", isOfficial = true),
    Community("Classificados - venda de produtos", "Criado por João\nEspaço destinado à compra e venda de produtos entre os alunos."),
)

val exploreCommunities = listOf(
    Community("Compra e Venda de Materiais", "Comunidade Oficial\nLorem ipsum is simply dummy text of the printing and typesetting industry", isOfficial = true),
    Community("Comunidade São Paulo", "Comunidade Oficial\nLorem ipsum is simply dummy text of the printing and typesetting industry", isOfficial = true),
    Community("Comunidade Alimentação", "Comunidade Oficial\nLorem ipsum is simply dummy text of the printing and typesetting industry", isOfficial = true),
    Community("Comunidade 1", "Criado por Fulano de Tal\nLorem ipsum is simply dummy text of the printing and typesetting industry"),
    Community("Comunidade 2", "Criado por Fulano de Tal\nLorem ipsum is simply dummy text of the printing and typesetting industry"),
    Community("Comunidade 3", "Criado por Fulano de Tal\nLorem ipsum is simply dummy text of the printing and typesetting industry"),
)

@Composable
fun CommunitiesScreen(navController: NavController) {
    val tabs = listOf("Home", "Explorar")
    var selectedTabIndex by remember { mutableStateOf(0) }

    var isSearchActive by remember { mutableStateOf(false) }
    var searchText by remember { mutableStateOf("") }

    val currentList = if (selectedTabIndex == 0) homeCommunities else exploreCommunities

    val filteredCommunities = remember(searchText, currentList) {
        if (!isSearchActive || searchText.isBlank()) {
            currentList
        } else {
            currentList.filter { community ->
                val query = searchText.trim().lowercase()
                community.name.lowercase().contains(query) ||
                        community.description.lowercase().contains(query)
            }
        }
    }

    Scaffold(
        containerColor = Color.White,
        topBar = {
            Column(modifier = Modifier.background(Color.White)) {
                CommunitiesTopBar(
                    onProfileClick = {
                        navController.navigate(MainAppDestinations.PROFILE_ROUTE)
                    },
                    onSearchClick = {
                        isSearchActive = !isSearchActive
                        if (!isSearchActive) searchText = ""
                    },
                    isSearchActive = isSearchActive,
                    searchText = searchText,
                    onSearchTextChange = { searchText = it }
                )

                TabRow(
                    selectedTabIndex = selectedTabIndex,
                    containerColor = Color.White,
                    contentColor = HoppiOrange,
                    indicator = { tabPositions ->
                        TabRowDefaults.Indicator(
                            modifier = Modifier
                                .tabIndicatorOffset(tabPositions[selectedTabIndex])
                                .padding(horizontal = 40.dp)
                                .clip(RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp)),
                            color = HoppiOrange
                        )
                    }
                ) {
                    tabs.forEachIndexed { index, title ->
                        Tab(
                            selected = selectedTabIndex == index,
                            onClick = { selectedTabIndex = index },
                            text = {
                                Text(
                                    text = title,
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontSize = 16.sp,
                                    color = if (selectedTabIndex == index) HoppiOrange else Color.Gray
                                )
                            }
                        )
                    }
                }
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { /* Ação de criar post/comunidade (página diferente) */ },
                containerColor = HoppiOrange,
                shape = FloatingActionButtonDefaults.extendedFabShape,
                modifier = Modifier.clip(CircleShape)
            ) {
                Icon(
                    Icons.Filled.Add,
                    contentDescription = "Criar Post",
                    tint = Color.White,
                    modifier = Modifier.size(30.dp)
                )
            }
        },
        floatingActionButtonPosition = FabPosition.End
    ) { paddingValues ->
        val adjustedPaddingValues = PaddingValues(
            top = paddingValues.calculateTopPadding(),
            start = paddingValues.calculateStartPadding(LocalLayoutDirection.current),
            end = paddingValues.calculateEndPadding(LocalLayoutDirection.current),
            bottom = 0.dp
        )
        Box(modifier = Modifier.padding(adjustedPaddingValues)) {
            CommunityTabContent(
                communities = filteredCommunities,
                isExploreTab = selectedTabIndex == 1
            )
        }
    }
}

@Composable
fun CommunitiesTopBar(
    onProfileClick: () -> Unit,
    onSearchClick: () -> Unit,
    isSearchActive: Boolean,
    searchText: String,
    onSearchTextChange: (String) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(horizontal = 20.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onProfileClick) {
                    Image(
                        painter = painterResource(id = R.drawable.user),
                        contentDescription = "Perfil",
                        modifier = Modifier.size(35.dp)
                    )
                }

                Spacer(modifier = Modifier.width(13.dp))

                Text(
                    text = "Comunidades",
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold,
                    color = HoppiOrange
                )
            }

            IconButton(onClick = onSearchClick) {
                Icon(
                    Icons.Default.Search,
                    contentDescription = "Search",
                    modifier = Modifier.size(30.dp),
                    tint = if (isSearchActive) Color.Black else HoppiOrange
                )
            }
        }

        if (isSearchActive) {
            OutlinedTextField(
                value = searchText,
                onValueChange = onSearchTextChange,
                label = { Text("Buscar Comunidades...") },
                singleLine = true,
                shape = RoundedCornerShape(10.dp),
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .padding(bottom = 8.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = HoppiOrange,
                    unfocusedBorderColor = Color.Gray.copy(alpha = 0.5f),
                )
            )
        }
    }
}

@Composable
fun CommunityItem(community: Community, onCommunityClick: () -> Unit, isExploreTab: Boolean) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 6.dp)
            .clickable(onClick = onCommunityClick),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = ItemCardBackground),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)

    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.Top
        ) {
            Box(
                modifier = Modifier
                    .size(35.dp)
                    .clip(CircleShape)
                    .background(community.iconColor)
            )

            Spacer(modifier = Modifier.width(10.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = community.name,
                    style = MaterialTheme.typography.bodyMedium,
                    fontSize = 16.sp,
                    color = Color.Black
                )

                val descriptionLines = community.description.split('\n')

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = descriptionLines.getOrNull(0) ?: "",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    if (community.isOfficial) {
                        Image(
                            painter = painterResource(id = R.drawable.oficial),
                            contentDescription = "Comunidade Oficial",
                            modifier = Modifier.size(16.dp),
                        )
                    }
                }

                descriptionLines.getOrNull(1)?.let {
                    Spacer(modifier = Modifier.width(10.dp))

                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodyMedium,
                        fontSize = 14.sp,
                        color = Color.Black
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            if (isExploreTab) {
                Icon(
                    Icons.Default.Add,
                    contentDescription = "Join Community",
                    tint = HoppiOrange,
                    modifier = Modifier.size(28.dp)
                )
            }
        }
    }
}

@Composable
fun CommunityTabContent(communities: List<Community>, isExploreTab: Boolean) {

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(1.dp)
    ) {
        items(communities) { community ->
            CommunityItem(
                community = community,
                onCommunityClick = { /* Click action */ },
                isExploreTab = isExploreTab
            )
        }
    }
}