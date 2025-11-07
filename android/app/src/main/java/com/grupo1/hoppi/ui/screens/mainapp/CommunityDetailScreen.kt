package com.grupo1.hoppi.ui.screens.mainapp

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.grupo1.hoppi.R

enum class CommunityAccessStatus {
    MEMBER,
    NOT_MEMBER_PUBLIC,
    NOT_MEMBER_PRIVATE,
    REQUEST_PENDING
}

data class CommunityPost(
    val id: Int,
    val username: String,
    val handle: String,
    val content: String,
    val avatarColor: Color,
    val likes: String = "10 K",
    val comments: String = "1.5 K"
)

val mockCommunityPosts = listOf(
    CommunityPost(1, "Fulano de Tal", "@fulan.tal", "Lorem ipsum is simply dummy text of the printing and typesetting industry", Color(0xFFB56576)),
    CommunityPost(2, "Ciclano", "@cicl.ano", "Lorem ipsum is simply dummy text of the printing and typesetting industry", Color(0xFFFF9800)),
    CommunityPost(3, "Estudante", "@estudante123", "Lorem ipsum is simply dummy text of the printing and typesetting industry", Color(0xFF3F51B5)),
    CommunityPost(4, "Ciclano", "@cicl.ano", "Lorem ipsum is simply dummy text of the printing and typesetting industry", Color(0xFFFF9800)),
)

@Composable
fun CommunityDetailScreen(
    navController: NavController,
    communityId: String
) {
    val community = remember { findCommunityByName(communityId) }

    val currentCommunity = community ?: Community(
        name = "Comunidade Não Encontrada",
        description = "",
        isOfficial = false,
        isPrivate = false
    )

    val isPrivate = currentCommunity.isPrivate

    val creatorInfo = currentCommunity.description.split('\n').firstOrNull { it.startsWith("Criado por") } ?: "Comunidade Oficial"


    var accessStatus by remember {
        mutableStateOf(
            if (isPrivate) CommunityAccessStatus.NOT_MEMBER_PRIVATE
            else CommunityAccessStatus.NOT_MEMBER_PUBLIC
        )
    }

    Scaffold(
        containerColor = Color.White,
        topBar = {
            CommunityDetailTopBar(
                title = currentCommunity.name,
                onBackClick = { navController.popBackStack() },
                onSettingsClick = { navController.navigate("main/community_settings/${currentCommunity.name}") }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            item {
                CommunityDetailHeader(
                    communityName = currentCommunity.name,
                    creatorInfo = creatorInfo,
                    isOfficial = currentCommunity.isOfficial,
                    isPrivate = isPrivate,
                    membersCount = "1.870",
                    postsCount = "560",
                    accessStatus = accessStatus,
                    onActionButtonClick = {
                        accessStatus = handleCommunityAction(accessStatus, isPrivate)
                    }
                )
            }

            CommunityDetailBodyItems(accessStatus = accessStatus)
        }
    }
}

fun LazyListScope.CommunityDetailBodyItems(accessStatus: CommunityAccessStatus) {
    val hasAccess = accessStatus == CommunityAccessStatus.MEMBER ||
            accessStatus == CommunityAccessStatus.NOT_MEMBER_PUBLIC

    if (hasAccess) {
        items(mockCommunityPosts) { post ->
            PostCardDetail(post = post)
            Divider(color = LightBlue.copy(alpha = 0.2f), thickness = 1.dp)
        }
        item { Spacer(modifier = Modifier.height(56.dp)) }
    } else {
        item {
            AccessDeniedContent(accessStatus = accessStatus)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommunityDetailTopBar(
    title: String,
    onBackClick: () -> Unit,
    onSettingsClick: () -> Unit
) {
    var showMenu by remember { mutableStateOf(false) }

    TopAppBar(
        title = { },
        navigationIcon = {
            Box(modifier = Modifier.padding(top = 0.dp, start = 20.dp, end = 0.dp)) {
                IconButton(onClick = onBackClick, modifier = Modifier.offset(y = (-15).dp)) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar", Modifier.size(30.dp))
                }
            }
        },
        actions = {
            Box(modifier = Modifier.padding(top = 0.dp, end = 20.dp, start = 0.dp)) {
                IconButton(onClick = { showMenu = true }, modifier = Modifier.offset(y = (-15).dp)) {
                    Icon(Icons.Filled.MoreVert, contentDescription = "Mais opções", Modifier.size(30.dp))
                }
            }

            DropdownMenu(
                expanded = showMenu,
                onDismissRequest = { showMenu = false }
            ) {
                DropdownMenuItem(
                    text = { Text("Configurações") },
                    onClick = {
                        onSettingsClick()
                        showMenu = false
                    },
                    leadingIcon = {
                        Icon(Icons.Filled.Settings, contentDescription = "Configurações")
                    }
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
    )
    Divider(color = Color.Gray.copy(alpha = 0.2f), thickness = 1.dp)
}

@Composable
fun CommunityDetailHeader(
    communityName: String,
    creatorInfo: String,
    isOfficial: Boolean,
    isPrivate: Boolean,
    membersCount: String,
    postsCount: String,
    accessStatus: CommunityAccessStatus,
    onActionButtonClick: () -> Unit
) {
    val (buttonText, buttonColors, isBordered) = when (accessStatus) {
        CommunityAccessStatus.MEMBER -> Triple(
            "Membro",
            ButtonDefaults.buttonColors(containerColor = LightBlue),
            false
        )
        CommunityAccessStatus.NOT_MEMBER_PUBLIC -> Triple(
            "Seguir",
            ButtonDefaults.buttonColors(containerColor = HoppiOrange),
            false
        )
        CommunityAccessStatus.NOT_MEMBER_PRIVATE -> Triple(
            "Seguir",
            ButtonDefaults.buttonColors(containerColor = HoppiOrange),
            false
        )
        CommunityAccessStatus.REQUEST_PENDING -> Triple(
            "Solicitado",
            ButtonDefaults.buttonColors(containerColor = Color.White),
            true
        )
    }

    val icon = when (accessStatus) {
        CommunityAccessStatus.MEMBER -> Icons.Default.Check
        CommunityAccessStatus.REQUEST_PENDING -> Icons.Default.Add
        else -> Icons.Default.Add
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .offset(y = (-15).dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(LightBlue)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = communityName,
                    fontWeight = FontWeight.Medium,
                    fontSize = 18.sp,
                    color = Color(0xFF000000),
                    style = MaterialTheme.typography.headlineLarge
                )
                Spacer(modifier = Modifier.width(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = creatorInfo,
                        fontSize = 14.sp,
                        color = Color.Gray,
                        style = MaterialTheme.typography.bodyMedium
                    )
                    if (isOfficial) {
                        Image(
                            painter = painterResource(id = R.drawable.oficial),
                            contentDescription = "Comunidade Oficial",
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            CommunityStat(count = membersCount, label = "Membros")
            Spacer(modifier = Modifier.width(32.dp))
            CommunityStat(count = postsCount, label = "Posts")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = onActionButtonClick,
            modifier = Modifier
                .width(160.dp)
                .height(40.dp)
                .clip(RoundedCornerShape(30.dp))
                .run {
                    if (isBordered) {
                        border(1.dp, Color.Gray, RoundedCornerShape(30.dp))
                            .background(Color.White)
                    } else this
                },
            colors = buttonColors,
            contentPadding = PaddingValues(horizontal = 16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (accessStatus == CommunityAccessStatus.MEMBER) {
                    Icon(icon, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(buttonText, color = Color.White, fontWeight = FontWeight.SemiBold)
                } else if (accessStatus == CommunityAccessStatus.REQUEST_PENDING) {
                    Text(buttonText, color = Color.Gray, fontWeight = FontWeight.SemiBold)
                } else {
                    Icon(icon, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(buttonText, color = Color.White, fontWeight = FontWeight.SemiBold)
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
    Divider(color = LightBlue.copy(alpha = 0.2f), thickness = 2.dp)
}

fun handleCommunityAction(currentStatus: CommunityAccessStatus, isPrivate: Boolean): CommunityAccessStatus {
    return when (currentStatus) {
        CommunityAccessStatus.MEMBER -> CommunityAccessStatus.NOT_MEMBER_PUBLIC
        CommunityAccessStatus.NOT_MEMBER_PUBLIC -> CommunityAccessStatus.MEMBER
        CommunityAccessStatus.NOT_MEMBER_PRIVATE -> {
            if (isPrivate) CommunityAccessStatus.REQUEST_PENDING else CommunityAccessStatus.MEMBER
        }
        CommunityAccessStatus.REQUEST_PENDING -> CommunityAccessStatus.NOT_MEMBER_PRIVATE
    }
}

@Composable
fun CommunityStat(count: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(count, fontWeight = FontWeight.Medium, fontSize = 18.sp, style = MaterialTheme.typography.bodyMedium, color = Color(0xFF000000))
        Text(label, color = Color.Gray, fontSize = 14.sp, style = MaterialTheme.typography.bodyMedium)
    }
}

@Composable
fun PostCardDetail(post: CommunityPost) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(20.dp),
        verticalAlignment = Alignment.Top
    ) {
        Box(
            modifier = Modifier
                .size(35.dp)
                .clip(CircleShape)
                .background(post.avatarColor)
        )
        Spacer(Modifier.width(20.dp))
        Column(modifier = Modifier.weight(1f)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(post.username, color = Color(0xFF000000), fontWeight = FontWeight.Medium, fontSize = 14.sp, style = MaterialTheme.typography.bodyMedium)
                Spacer(Modifier.width(5.dp))
                Text(post.handle, color = Color.Gray, fontSize = 14.sp, style = MaterialTheme.typography.bodyMedium)
            }
            Text(post.content, color = Color(0xFF000000), fontSize = 14.sp, modifier = Modifier.padding(top = 10.dp, bottom = 10.dp), style = MaterialTheme.typography.bodyMedium)
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(painter = painterResource(id = R.drawable.like_detailed), contentDescription = "Curtidas", modifier = Modifier.size(12.dp))
                Spacer(Modifier.width(4.dp))
                Text(post.likes, color = Color(0xFF000000), fontSize = 12.sp, style = MaterialTheme.typography.bodyMedium)
                Spacer(Modifier.width(15.dp))
                Image(painter = painterResource(id = R.drawable.comments_detailed), contentDescription = "Curtidas", modifier = Modifier.size(12.dp))
                Spacer(Modifier.width(4.dp))
                Text(post.comments, color = Color(0xFF000000), fontSize = 12.sp, style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}

@Composable
fun AccessDeniedContent(accessStatus: CommunityAccessStatus) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 40.dp, vertical = 60.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.private_community),
            contentDescription = "Acesso Negado",
            modifier = Modifier.size(320.dp)
        )
        Spacer(modifier = Modifier.height(32.dp))
    }
}