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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.grupo1.hoppi.R
import com.grupo1.hoppi.ui.screens.home.MainAppDestinations
import com.grupo1.hoppi.ui.screens.home.Post
import com.grupo1.hoppi.ui.screens.home.PostsViewModel
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
    communityId: Int,
    postsViewModel: PostsViewModel
) {
    val community = findCommunityById(communityId)

    val currentCommunity = community?.copy(
        isPrivate = if (communityId == 10) true else community.isPrivate
    ) ?: Community(
        id = 0,
        name = "Comunidade Não Encontrada",
        description = "Esta comunidade não existe ou não foi encontrada.",
        creatorUsername = "Sistema",
        isOfficial = false,
        isPrivate = false
    )

    val isPrivate = currentCommunity.isPrivate

    val posts by remember(currentCommunity.id) {
        derivedStateOf {
            postsViewModel.getCommunityPosts(currentCommunity.id)
        }
    }

    val realCreator = currentCommunity.creatorUsername
    val isCreator = realCreator == postsViewModel.currentUser
    val creatorInfo = "Criado por $realCreator"

    val followedSnapshot = AppCommunityManager.followedCommunities.toList()

    var accessStatus by remember(currentCommunity.id, followedSnapshot) {
        mutableStateOf(
            if (AppCommunityManager.isFollowing(currentCommunity.name))
                CommunityAccessStatus.MEMBER
            else if (isPrivate)
                CommunityAccessStatus.NOT_MEMBER_PRIVATE
            else
                CommunityAccessStatus.NOT_MEMBER_PUBLIC
        )
    }

    LaunchedEffect(followedSnapshot, currentCommunity.id) {
        accessStatus = when {
            AppCommunityManager.isFollowing(currentCommunity.name) -> CommunityAccessStatus.MEMBER
            AppCommunityManager.isRequestPending(currentCommunity.name) -> CommunityAccessStatus.REQUEST_PENDING
            isPrivate -> CommunityAccessStatus.NOT_MEMBER_PRIVATE
            else -> CommunityAccessStatus.NOT_MEMBER_PUBLIC
        }
    }

    val hasAccess = accessStatus == CommunityAccessStatus.MEMBER ||
            accessStatus == CommunityAccessStatus.NOT_MEMBER_PUBLIC

    var showReportDialog by remember { mutableStateOf(false) }
    var reportReason by remember { mutableStateOf("") }
    var showDeleteDialog by remember { mutableStateOf(false) }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    fun onDeleteCommunityClick() {
        AppCommunityManager.deleteCommunity(currentCommunity.name)
        navController.popBackStack()
    }

    val notifications = remember { mutableStateListOf<String>() }

    fun handleCommunityFollow(currentStatus: CommunityAccessStatus, isPrivate: Boolean) {
        when (currentStatus) {
            CommunityAccessStatus.MEMBER -> {
                AppCommunityManager.unfollowCommunity(currentCommunity.name)
                accessStatus = if (isPrivate) CommunityAccessStatus.NOT_MEMBER_PRIVATE else CommunityAccessStatus.NOT_MEMBER_PUBLIC
            }
            CommunityAccessStatus.NOT_MEMBER_PUBLIC -> {
                AppCommunityManager.followCommunity(currentCommunity.name)
                accessStatus = CommunityAccessStatus.MEMBER
            }
            CommunityAccessStatus.NOT_MEMBER_PRIVATE -> {
                AppCommunityManager.sendFollowRequest(currentCommunity.name)
                accessStatus = CommunityAccessStatus.REQUEST_PENDING
                notifications.add("${postsViewModel.currentUser} pediu para seguir ${currentCommunity.name}")
                scope.launch { snackbarHostState.showSnackbar("Pedido de seguir enviado ao criador") }
            }
            CommunityAccessStatus.REQUEST_PENDING -> {
                AppCommunityManager.cancelFollowRequest(currentCommunity.name)
                accessStatus = CommunityAccessStatus.NOT_MEMBER_PRIVATE
            }
        }
    }

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
                isOfficial = currentCommunity.isOfficial,
                isCreator = isCreator,
                isMember = accessStatus == CommunityAccessStatus.MEMBER,
                accessStatus = accessStatus,
                onReportClick = { showReportDialog = true },
                onFollowClick = {
                    AppCommunityManager.followCommunity(currentCommunity.name)
                    accessStatus = CommunityAccessStatus.MEMBER
                    scope.launch { snackbarHostState.showSnackbar("Você seguiu a comunidade") }
                },
                onUnfollowClick = {
                    AppCommunityManager.unfollowCommunity(currentCommunity.name)
                    accessStatus = if (isPrivate) CommunityAccessStatus.NOT_MEMBER_PRIVATE else CommunityAccessStatus.NOT_MEMBER_PUBLIC
                    scope.launch { snackbarHostState.showSnackbar("Você deixou a comunidade") }
                },
                onDeleteCommunityClick = { showDeleteDialog = true },
                onEditCommunityClick = {
                    navController.navigate("${MainAppDestinations.EDIT_COMMUNITY_ROUTE}/${currentCommunity.id}")
                }
            )
        },
        contentWindowInsets = WindowInsets(0,0,0,0),
        floatingActionButton = {
            if (hasAccess) {
                FloatingActionButton(
                    onClick = {
                        navController.navigate("${MainAppDestinations.CREATE_POST_COMMUNITY_ROUTE}/${currentCommunity.id}")
                    },
                    shape = CircleShape,
                    containerColor = HoppiOrange
                ) {
                    Icon(Icons.Filled.Add, contentDescription = "Criar Post", tint = Color.White)
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
                    creatorInfo = creatorInfo,
                    isOfficial = currentCommunity.isOfficial,
                    communityDescription = currentCommunity.description,
                    isPrivate = isPrivate,
                    membersCount = "1.870",
                    postsCount = posts.size.toString(),
                    accessStatus = accessStatus,
                    onActionButtonClick = {
                        handleCommunityFollow(accessStatus, isPrivate)
                    },
                    isCreator = isCreator
                )
            }

            CommunityDetailBodyItems(
                accessStatus = accessStatus,
                posts = posts,
                navController = navController,
                postsViewModel = postsViewModel
            )
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

        if (showDeleteDialog) {
            AlertDialog(
                onDismissRequest = { showDeleteDialog = false },
                title = { Text("Excluir Comunidade") },
                text = {
                    Text("Tem certeza de que deseja excluir esta comunidade?")
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            showDeleteDialog = false
                            onDeleteCommunityClick()
                        }
                    ) {
                        Text("Excluir", color = Color.Red)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDeleteDialog = false }) {
                        Text("Cancelar")
                    }
                }
            )
        }
    }
}

fun LazyListScope.CommunityDetailBodyItems(
    accessStatus: CommunityAccessStatus,
    posts: List<Post>,
    navController: NavController,
    postsViewModel: PostsViewModel
) {
    val hasAccess = accessStatus == CommunityAccessStatus.MEMBER ||
            accessStatus == CommunityAccessStatus.NOT_MEMBER_PUBLIC

    if (hasAccess) {
        items(posts) { post ->

            PostCardDetail(
                post = post,
                onPostClick = { postId ->
                    navController.navigate("main/post_open/$postId")
                },
                onLikeClick = {
                    postsViewModel.toggleLike(post.id)
                }
            )

            Divider(color = LightBlue.copy(alpha = 0.2f), thickness = 1.dp)
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
    onDeleteCommunityClick: () -> Unit,
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
    var showDeleteDialog by remember { mutableStateOf(false) }

    TopAppBar(
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
                        DropdownMenuItem(
                            text = { Text("Excluir Comunidade", color = Color.Red) },
                            onClick = {
                                showMenu = false
                                onDeleteCommunityClick()
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
fun PostCardDetail(
    post: Post,
    onPostClick: (Int) -> Unit,
    onLikeClick: () -> Unit
) {
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
                .background(Color(0xFF3F51B5))
        )

        Spacer(Modifier.width(20.dp))

        Column(
            modifier = Modifier
                .weight(1f)
                .clickable { onPostClick(post.id) }
        ) {

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = post.username,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.bodyMedium,
                    fontSize = 14.sp,
                    color = Color(0xFF000000),
                )

                Spacer(Modifier.width(5.dp))

                Text(
                    text = "@${post.username.replace(" ", "").lowercase()}",
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
                    painter = painterResource(id = if (post.isLiked) R.drawable.liked else R.drawable.like_detailed),
                    contentDescription = null,
                    modifier = Modifier
                        .size(20.dp)
                        .clickable { onLikeClick() }
                )
                Spacer(Modifier.width(4.dp))
                Text(post.likes.toString(), style = MaterialTheme.typography.bodySmall, color = Color(0xFF000000))

                Spacer(Modifier.width(16.dp))

                Image(
                    painter = painterResource(id = R.drawable.comments_detailed),
                    contentDescription = "Comments",
                    modifier = Modifier.size(12.dp)
                )
                Spacer(Modifier.width(4.dp))
                Text(post.comments.toString(), style = MaterialTheme.typography.bodySmall, color = Color(0xFF000000))

                Spacer(Modifier.weight(1f))

                post.tag?.let { tagName ->
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
                }
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