package com.grupo1.hoppi.ui.screens.mainapp

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Comment
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.grupo1.hoppi.R
import com.grupo1.hoppi.ui.screens.home.MainAppDestinations

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
    val communityName = if (communityId == "UnaspSP") "Comunidade Unasp SP" else "Comunidade de Estudantes"
    val isPrivate = remember { mutableStateOf(communityId == "UnaspSP") }

    var accessStatus by remember {
        mutableStateOf(
            if (isPrivate.value) CommunityAccessStatus.NOT_MEMBER_PRIVATE
            else CommunityAccessStatus.NOT_MEMBER_PUBLIC
        )
    }

    // Para fins de demonstração, simulamos a solicitação pendente se o ID for UnaspSP/requested
    if (communityId == "UnaspSP/requested") {
        isPrivate.value = true
        accessStatus = CommunityAccessStatus.REQUEST_PENDING
    }

    Scaffold(
        containerColor = Color.White,
        topBar = {
            CommunityDetailTopBar(
                title = communityName,
                onBackClick = { navController.popBackStack() }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            CommunityDetailHeader(
                communityName = communityName,
                isOfficial = true,
                isPrivate = isPrivate.value,
                membersCount = "1.870",
                postsCount = "560",
                accessStatus = accessStatus,
                onActionButtonClick = {
                    accessStatus = handleCommunityAction(accessStatus, isPrivate.value)
                }
            )

            CommunityDetailBody(accessStatus = accessStatus)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommunityDetailTopBar(title: String, onBackClick: () -> Unit) {
    TopAppBar(
        title = { },
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar")
            }
        },
        actions = {
            IconButton(onClick = { }) {
                Icon(Icons.Filled.MoreVert, contentDescription = "Mais opções")
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
    )
    Divider(color = Color.Gray.copy(alpha = 0.2f), thickness = 1.dp)
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
        Text(count, fontWeight = FontWeight.Bold, fontSize = 18.sp)
        Text(label, color = Color.Gray, fontSize = 14.sp)
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
                .size(40.dp)
                .clip(CircleShape)
                .background(post.avatarColor)
        )
        Spacer(Modifier.width(20.dp))
        Column(modifier = Modifier.weight(1f)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(post.username, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                Spacer(Modifier.width(5.dp))
                Text(post.handle, color = Color.Gray, fontSize = 14.sp)
            }
            Text(post.content, fontSize = 14.sp, modifier = Modifier.padding(top = 10.dp, bottom = 10.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Filled.FavoriteBorder, contentDescription = "Likes", modifier = Modifier.size(12.dp), tint = Color.Gray)
                Spacer(Modifier.width(4.dp))
                Text(post.likes, color = Color.Gray, fontSize = 12.sp)
                Spacer(Modifier.width(16.dp))
                Icon(Icons.Filled.Comment, contentDescription = "Comments", modifier = Modifier.size(12.dp), tint = Color.Gray)
                Spacer(Modifier.width(4.dp))
                Text(post.comments, color = Color.Gray, fontSize = 12.sp)
            }
        }
    }
}

@Composable
fun CommunityPostList(posts: List<CommunityPost>) {
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        items(posts) { post ->
            PostCardDetail(post = post)
            Divider(color = Color.Gray.copy(alpha = 0.2f), thickness = 1.dp)
        }
        item { Spacer(modifier = Modifier.height(56.dp)) }
    }
}

@Composable
fun AccessDeniedContent(accessStatus: CommunityAccessStatus) {
    val message = "Essa Comunidade é privada, para ter acesso solicite ao administrador da página"

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
            modifier = Modifier.size(120.dp)
        )
        Spacer(modifier = Modifier.height(32.dp))
        Text(
            text = message,
            textAlign = TextAlign.Center,
            color = Color.Gray,
            fontSize = 16.sp
        )
    }
}

@Composable
fun CommunityDetailBody(accessStatus: CommunityAccessStatus) {
    val hasAccess = accessStatus == CommunityAccessStatus.MEMBER ||
            accessStatus == CommunityAccessStatus.NOT_MEMBER_PUBLIC

    if (hasAccess) {
        CommunityPostList(posts = mockCommunityPosts)
    } else {
        AccessDeniedContent(accessStatus = accessStatus)
    }
}

@Composable
fun CommunityDetailHeader(
    communityName: String,
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
            true // O booleano 'isBordered' é o terceiro valor
        )
    }

    // O ícone (icon) agora é definido separadamente ou dentro do Row do botão
    val icon = when (accessStatus) {
        CommunityAccessStatus.MEMBER -> Icons.Default.Check
        CommunityAccessStatus.REQUEST_PENDING -> Icons.Default.Add // Simulação do ícone de relógio
        else -> Icons.Default.Add
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
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
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = communityName,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = Color.Black
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    if (isOfficial) {
                        Image(
                            painter = painterResource(id = R.drawable.oficial),
                            contentDescription = "Comunidade Oficial",
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
                Text(
                    text = if (isPrivate) "Comunidade Privada" else "Comunidade Oficial",
                    fontSize = 14.sp,
                    color = Color.Gray
                )
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
}