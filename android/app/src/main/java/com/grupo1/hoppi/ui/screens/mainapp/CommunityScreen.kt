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
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.grupo1.hoppi.ui.screens.home.MainAppDestinations
import com.grupo1.hoppi.R
import kotlinx.coroutines.launch

@Composable
fun CommunitiesScreen(navController: NavController) {
    val tabs = listOf("Home", "Explorar")
    var selectedTabIndex by remember { mutableStateOf(0) }
    var isSearchActive by remember { mutableStateOf(false) }
    var searchText by remember { mutableStateOf("") }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val homeCommunities = AppCommunityManager.allCommunities.filter { AppCommunityManager.isFollowing(it.name) }
    val exploreCommunities = AppCommunityManager.allCommunities.filter { !AppCommunityManager.isFollowing(it.name) }

    val currentList = if (selectedTabIndex == 0) homeCommunities else exploreCommunities

    val filteredCommunities = if (!isSearchActive || searchText.isBlank()) {
        currentList
    } else {
        val q = searchText.trim().lowercase()
        currentList.filter { community ->
            community.name.lowercase().contains(q) ||
                    community.description.lowercase().contains(q)
        }
    }

    Scaffold(
        containerColor = Color.White,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            Column(modifier = Modifier.background(Color.White)) {
                CommunitiesTopBar(
                    onProfileClick = { navController.navigate(MainAppDestinations.PROFILE_ROUTE) },
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
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate(MainAppDestinations.CREATE_COMMUNITY_ROUTE) },
                containerColor = HoppiOrange,
                shape = FloatingActionButtonDefaults.extendedFabShape,
                modifier = Modifier.clip(CircleShape)
            ) {
                Icon(
                    Icons.Filled.Add,
                    contentDescription = "Criar Comunidade",
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
                isExploreTab = selectedTabIndex == 1,
                onCommunityClick = { communityId ->
                    navController.navigate("main/community_detail/$communityId")
                },
                onFollowToggle = { communityName ->
                    if (AppCommunityManager.isFollowing(communityName)) {
                        AppCommunityManager.unfollowCommunity(communityName)
                        scope.launch { snackbarHostState.showSnackbar("Você deixou a comunidade") }
                    } else {
                        AppCommunityManager.followCommunity(communityName)
                        scope.launch { snackbarHostState.showSnackbar("Você seguiu a comunidade") }
                    }
                }
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
                    focusedPlaceholderColor = Color(0xFF000000),
                    unfocusedPlaceholderColor = Color(0xFF000000),
                    focusedLabelColor = Color(0xFF000000),
                    unfocusedLabelColor = Color(0xFF000000),
                    focusedLeadingIconColor = Color(0xFF000000),
                    unfocusedLeadingIconColor = Color(0xFF000000),
                    focusedTextColor = Color(0xFF000000),
                    unfocusedTextColor = Color(0xFF000000)
                )
            )
        }
    }
}

@Composable
fun CommunityTabContent(
    communities: List<Community>,
    isExploreTab: Boolean,
    onCommunityClick: (Int) -> Unit,
    onFollowToggle: (String) -> Unit
) {
    Spacer(modifier = Modifier.width(20.dp))
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(1.dp)
    ) {
        items(communities) { community ->
            CommunityItem(
                community = community,
                onCommunityClick = { onCommunityClick(community.id) },
                isExploreTab = isExploreTab,
                onFollowToggle = onFollowToggle
            )
        }
    }
}

@Composable
fun CommunityItem(
    community: Community,
    onCommunityClick: () -> Unit,
    isExploreTab: Boolean,
    onFollowToggle: (String) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 5.dp)
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

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = community.creatorUsername,
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

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = community.description,
                    style = MaterialTheme.typography.bodyMedium,
                    fontSize = 14.sp,
                    color = Color.Black,
                    modifier = Modifier.fillMaxWidth()
                )

            }

            Spacer(modifier = Modifier.width(16.dp))

            val isFollowing = AppCommunityManager.isFollowing(community.name)
            IconButton(onClick = {
                onFollowToggle(community.name)
            }) {
                if (isFollowing) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Seguindo",
                        tint = HoppiOrange,
                        modifier = Modifier.size(28.dp)
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Seguir",
                        tint = HoppiOrange,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }
        }
    }
}
