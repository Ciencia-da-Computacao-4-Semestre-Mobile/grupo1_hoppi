package com.grupo1.hoppi.ui.screens.mainapp

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.grupo1.hoppi.R
import com.grupo1.hoppi.network.communities.Community
import com.grupo1.hoppi.network.posts.PostResponse
import com.grupo1.hoppi.ui.screens.home.CommunitiesViewModel
import com.grupo1.hoppi.ui.screens.home.LikesViewModel
import com.grupo1.hoppi.ui.screens.home.MainAppDestinations
import com.grupo1.hoppi.ui.screens.home.PostsViewModel
import com.grupo1.hoppi.ui.screens.home.ProfileImage
import com.grupo1.hoppi.ui.screens.home.UsersViewModel
import com.grupo1.hoppi.ui.screens.settings.HoppiOrange
import kotlinx.coroutines.launch

enum class CommunityAccessStatus {
    MEMBER,
    NOT_MEMBER_PUBLIC,
    NOT_MEMBER_PRIVATE,
    REQUEST_PENDING
}

@Composable
fun CommunityDetailScreen(
    navController: NavController,
    communityId: String,
    userViewModel: UsersViewModel,
    postsViewModel: PostsViewModel,
    communitiesViewModel: CommunitiesViewModel,
    likesViewModel: LikesViewModel
) {
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val currentUser by userViewModel.currentUser.collectAsState()
    val avatarIndex by userViewModel.avatarIndexFlow.collectAsState(initial = 5)
    val token by userViewModel.token.collectAsState(initial = null)

    LaunchedEffect(Unit) {
        communitiesViewModel.loadCommunities()
    }

    val communities by communitiesViewModel.communities.collectAsState()

    val currentCommunity = communities.find { it.id == communityId } ?: Community(
        id = communityId,
        name = "Comunidade Não Encontrada",
        description = "Esta comunidade não existe ou não foi encontrada.",
        avatar = "avatar_1",
        isPrivate = false,
        requiresApproval = false,
        createdAt = java.util.Date(),
        createdBy = null,
        members = emptyList()
    )

    val communityMembers by communitiesViewModel.members.collectAsState()
    val isMember = communityMembers[currentCommunity.id]?.any { it.user_id == currentUser } == true
    val followedCommunities by communitiesViewModel.followedCommunities.collectAsState()

    val accessStatus = when {
        followedCommunities.contains(currentCommunity.id) -> CommunityAccessStatus.MEMBER
        currentCommunity.isPrivate -> CommunityAccessStatus.NOT_MEMBER_PRIVATE
        else -> CommunityAccessStatus.NOT_MEMBER_PUBLIC
    }

    val posts by postsViewModel.posts.collectAsState()
    val mainPosts by remember(posts) {
        derivedStateOf {
            posts.distinctBy { it.id }
                .filter {
                    it.is_reply_to == null ||
                            it.is_reply_to == "" ||
                            it.is_reply_to.lowercase() == "null"
                }
        }
    }

    val likesMap by likesViewModel.likes.collectAsState()
    val currentUserId by userViewModel.currentUserId.collectAsState()

    LaunchedEffect(token) {
        if (!token.isNullOrEmpty()) {
            userViewModel.loadProfile(token!!)
            postsViewModel.loadFeed(token!!)
        }
    }

    LaunchedEffect(posts) {
        posts.forEach { post ->
            likesViewModel.loadLikes(post.id)
        }
    }

    fun handleCommunityAction() {
        if (token.isNullOrEmpty()) return

        when (accessStatus) {
            CommunityAccessStatus.MEMBER -> {
                communitiesViewModel.leaveCommunity(currentCommunity.id, token!!)
                communitiesViewModel.unfollowCommunity(currentCommunity.id, token!!)
            }
            CommunityAccessStatus.NOT_MEMBER_PUBLIC -> {
                communitiesViewModel.joinCommunity(currentCommunity.id, token!!)
                communitiesViewModel.followCommunity(currentCommunity.id, token!!)
            }
            CommunityAccessStatus.NOT_MEMBER_PRIVATE -> {
                communitiesViewModel.joinCommunity(currentCommunity.id, token!!)
                communitiesViewModel.followCommunity(currentCommunity.id, token!!)
                scope.launch { snackbarHostState.showSnackbar("Pedido de seguir enviado") }
            }
            CommunityAccessStatus.REQUEST_PENDING -> {
                // cancelar solicitação
            }
        }
    }

    var showReportDialog by remember { mutableStateOf(false) }
    var reportReason by remember { mutableStateOf("") }

    val notifications = remember { mutableStateListOf<String>() }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = Color.White,
        topBar = {
            CommunityDetailTopBar(
                title = currentCommunity.name,
                onBackClick = { navController.popBackStack() },
                onSettingsClick = {
                    navController.navigate("main/community_settings/${currentCommunity.id}")
                },
                isOfficial = false,
                isCreator = currentCommunity.createdBy?.id == currentUser,
                isMember = accessStatus == CommunityAccessStatus.MEMBER,
                accessStatus = accessStatus,
                onReportClick = { /* TODO */ },
                onFollowClick = { handleCommunityAction() },
                onUnfollowClick = { handleCommunityAction() },
                onEditCommunityClick = {
                    navController.navigate("${MainAppDestinations.EDIT_COMMUNITY_ROUTE}/${currentCommunity.id}")
                }
            )
        },
        contentWindowInsets = WindowInsets(0,0,0,0),
        floatingActionButton = {
            if (accessStatus == CommunityAccessStatus.MEMBER || accessStatus == CommunityAccessStatus.NOT_MEMBER_PUBLIC) {
                FloatingActionButton(
                    onClick = {
                        navController.navigate("${MainAppDestinations.CREATE_POST_COMMUNITY_ROUTE}/${currentCommunity.id}")
                    },
                    shape = CircleShape,
                    containerColor = HoppiOrange
                ) {
                    Icon(Icons.Filled.Add, contentDescription = "Criar Post", tint = Color.White, modifier = Modifier.size(30.dp))
                }
            }
        },
        floatingActionButtonPosition = FabPosition.End
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            item {
                CommunityDetailHeader(
                    communityName = currentCommunity.name,
                    creatorInfo = "Criado por ${currentCommunity.createdBy?.display_name ?: "Sistema"}",
                    isOfficial = false,
                    communityDescription = currentCommunity.description,
                    isPrivate = currentCommunity.isPrivate,
                    membersCount = (communityMembers[currentCommunity.id]?.size ?: 0).toString(),
                    postsCount = posts.size.toString(),
                    accessStatus = accessStatus,
                    onActionButtonClick = { handleCommunityAction() },
                    isCreator = currentCommunity.createdBy?.id == currentUser
                )
            }

            items(mainPosts) { post ->
                val likesForPost = likesMap[post.id] ?: emptyList()
                val isLiked = likesForPost.any { it.user_id == currentUserId }
                val likeCount = likesForPost.size

                PostCard(
                    avatarIndex = avatarIndex,
                    post = post,
                    isLiked = isLiked,
                    likeCount = likeCount,
                    onPostClick = { navController.navigate("main/post_open/${post.id}") },
                    onLikeClick = {
                        if (isLiked) {
                            likesViewModel.unlikePost(post.id, token!!)
                        } else {
                            likesViewModel.likePost(post.id, token!!)
                        }
                    }
                )

                Divider(
                    color = Color(0xFF9CBDC6).copy(alpha = 0.5f),
                    thickness = 1.dp
                )
            }

            /* CommunityDetailBodyItems(
                accessStatus = accessStatus.value,
                posts = posts,
                navController = navController,
                postsViewModel = postsViewModel,
                avatarIndex = avatarIndex
            ) */
        }

        if (showReportDialog) {
            AlertDialog(
                onDismissRequest = { showReportDialog = false },
                title = { Text("Denunciar comunidade") },
                text = {
                    Column {
                        Text("Motivo da denúncia:")
                        OutlinedTextField(
                            value = reportReason,
                            onValueChange = { reportReason = it },
                            modifier = Modifier.fillMaxWidth(),
                            placeholder = { Text("Descreva o motivo...") }
                        )
                    }
                },
                confirmButton = {
                    TextButton(onClick = {
                        showReportDialog = false
                        scope.launch {
                            snackbarHostState.showSnackbar("Denúncia enviada com sucesso!")
                        }
                    }) {
                        Text("Enviar")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showReportDialog = false }) {
                        Text("Cancelar")
                    }
                }
            )
        }

    }
}

fun LazyListScope.CommunityDetailBodyItems(
    accessStatus: CommunityAccessStatus,
    posts: List<PostResponse>,
    navController: NavController,
    postsViewModel: PostsViewModel,
    avatarIndex: Int,
) {
    val hasAccess = accessStatus == CommunityAccessStatus.MEMBER ||
            accessStatus == CommunityAccessStatus.NOT_MEMBER_PUBLIC

    if (hasAccess) {
        items(items = posts) { post ->

            PostCardDetail(
                avatarIndex,
                post = post,
                onPostClick = { postId ->
                    navController.navigate("main/post_open/$postId")
                },
                onLikeClick = { /* */ }
            )

            Divider(color = LightBlueDivider.copy(alpha = 0.2f), thickness = 1.dp)
        }

        item { Spacer(modifier = Modifier.height(56.dp)) }

    } else {
        item {
            AccessDeniedContent(accessStatus)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommunityDetailTopBar(
    title: String,
    onBackClick: () -> Unit,
    onSettingsClick: () -> Unit,
    isOfficial: Boolean,
    isCreator: Boolean,
    isMember: Boolean,
    accessStatus: CommunityAccessStatus,
    onReportClick: () -> Unit,
    onFollowClick: () -> Unit,
    onUnfollowClick: () -> Unit,
    onEditCommunityClick: () -> Unit
) {
    var showMenu by remember { mutableStateOf(false) }
    val menuFollowText = when (accessStatus) {
        CommunityAccessStatus.MEMBER -> "Sair"
        CommunityAccessStatus.REQUEST_PENDING -> "Cancelar solicitação"
        else -> "Seguir"
    }
    var menuExpanded by remember { mutableStateOf(false) }
    var showReportDialog by remember { mutableStateOf(false) }

    TopAppBar(
        modifier = Modifier.testTag("community_topbar"),
        title = { },
        navigationIcon = {
            Box(modifier = Modifier.padding(top = 0.dp, start = 20.dp, end = 0.dp)) {
                IconButton(
                    onClick = onBackClick,
                    modifier = Modifier.offset(y = (-15).dp)
                ) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Voltar",
                        tint = Color(0xFF000000),
                        modifier = Modifier.size(30.dp)
                    )
                }
            }
        },
        actions = {
            Box(modifier = Modifier.padding(top = 0.dp, end = 20.dp, start = 0.dp)) {
                IconButton(
                    onClick = { showMenu = true },
                    modifier = Modifier.offset(y = (-15).dp)
                ) {
                    Icon(
                        Icons.Filled.MoreVert,
                        contentDescription = "Voltar",
                        tint = Color(0xFF000000),
                        modifier = Modifier.size(30.dp)
                    )
                }
            }

            DropdownMenu(
                expanded = showMenu,
                onDismissRequest = { showMenu = false }
            ) {
                when {
                    isOfficial -> {
                        DropdownMenuItem(
                            text = { Text(if (isMember) "Sair" else "Seguir") },
                            onClick = {
                                showMenu = false
                                if (isMember) onUnfollowClick() else onFollowClick()
                            }
                        )
                    }

                    isCreator -> {
                        DropdownMenuItem(
                            text = { Text("Editar Comunidade") },
                            onClick = {
                                showMenu = false
                                onEditCommunityClick()
                            }
                        )
                    }

                    else -> {
                        DropdownMenuItem(
                            text = { Text("Denunciar Comunidade") },
                            onClick = {
                                menuExpanded = false
                                showReportDialog = true
                            }
                        )

                        DropdownMenuItem(
                            text = { Text(menuFollowText) },
                            onClick = {
                                showMenu = false
                                when (accessStatus) {
                                    CommunityAccessStatus.MEMBER -> onUnfollowClick()
                                    CommunityAccessStatus.REQUEST_PENDING -> {
                                        onUnfollowClick()
                                    }
                                    else -> onFollowClick()
                                }
                            }
                        )
                    }
                }
            }

            if (showReportDialog) {
                ReportDialog(
                    title = "Denunciar Post",
                    onDismiss = { showReportDialog = false },
                    onConfirm = { reason ->
                        showReportDialog = true
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
    communityDescription: String,
    isPrivate: Boolean,
    membersCount: String,
    postsCount: String,
    accessStatus: CommunityAccessStatus,
    onActionButtonClick: () -> Unit,
    isCreator: Boolean
) {
    val (buttonText, buttonColors, isBordered) = when (accessStatus) {
        CommunityAccessStatus.MEMBER -> Triple(
            "Membro",
            ButtonDefaults.buttonColors(containerColor = LightBlueDivider),
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
                    .clip(CircleShape)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.icon_community),
                    contentDescription = "Ícone comunidade",
                    modifier = Modifier.size(80.dp)
                )
            }
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
            Text(
                text = communityDescription,
                style = MaterialTheme.typography.bodyMedium,
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 40.dp),
                color = Color(0xFF000000)
            )
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
        if (!isCreator) {
            Button(
                onClick = { onActionButtonClick() },
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
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
    Divider(color = LightBlueDivider.copy(alpha = 0.2f), thickness = 2.dp)
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
fun PostCardDetail(
    avatarIndex: Int,
    post: PostResponse,
    onPostClick: (String) -> Unit,
    onLikeClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(20.dp),
        verticalAlignment = Alignment.Top
    ) {

        Box (
            modifier = Modifier.border(0.5.dp, Black, CircleShape)
        ) {
            ProfileImage(
                option = avatarIndex,
                profileSize = 35.dp,
                backgroundSize = 40.dp - 1.dp,
            )
        }

        Spacer(Modifier.width(20.dp))

        Column(
            modifier = Modifier
                .weight(1f)
                .clickable { onPostClick(post.id) }
        ) {

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = post.author?.display_name ?: "Usuário",
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.bodyMedium,
                    fontSize = 14.sp,
                    color = Color(0xFF000000),
                )

                Spacer(Modifier.width(5.dp))

                Text(
                    text = "@${post.author?.username ?: "usuario"}",
                    style = MaterialTheme.typography.bodyMedium,
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }

            Text(
                post.content,
                style = MaterialTheme.typography.bodyMedium,
                fontSize = 14.sp,
                modifier = Modifier.padding(top = 10.dp, bottom = 10.dp),
                color = Color(0xFF000000),
            )

            Row(verticalAlignment = Alignment.CenterVertically) {

                Image(
                    painter = painterResource(id = R.drawable.like_detailed),
                    contentDescription = null,
                    modifier = Modifier
                        .size(20.dp)
                        .clickable { onLikeClick() }
                )
                Spacer(Modifier.width(4.dp))
                Text("0", style = MaterialTheme.typography.bodySmall, color = Color(0xFF000000))

                Spacer(Modifier.width(16.dp))

                Image(
                    painter = painterResource(id = R.drawable.comments_detailed),
                    contentDescription = "Comments",
                    modifier = Modifier.size(12.dp)
                )
                Spacer(Modifier.width(4.dp))
                Text(post.reply_count.toString(), style = MaterialTheme.typography.bodySmall, color = Color(0xFF000000))

                Spacer(Modifier.weight(1f))

                /* post.tag?.let { tagName ->
                    val (bgColor, textColor, iconRes) = when (tagName) {
                        "Estudo" -> Triple(VerdeEstudo, TextColorEstudo, R.drawable.estudo_icon)
                        "Venda" -> Triple(AzulVenda, TextColorVenda, R.drawable.venda_icon)
                        "Info" -> Triple(LaranjaInfo, TextColorInfo, R.drawable.info_icon)
                        else -> Triple(Color.LightGray, Color.Black, R.drawable.info_icon)
                    }

                    AssistChip(
                        onClick = { },
                        label = {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Image(
                                    painter = painterResource(id = iconRes),
                                    contentDescription = tagName,
                                    modifier = Modifier.size(12.dp)
                                )
                                Spacer(Modifier.width(4.dp))
                                Text(tagName, fontSize = 10.sp, color = textColor)
                            }
                        },
                        colors = AssistChipDefaults.assistChipColors(containerColor = bgColor),
                        border = null,
                        shape = RoundedCornerShape(5.dp),
                        modifier = Modifier.height(20.dp)
                    )
                } */
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