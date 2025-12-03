package com.grupo1.hoppi.ui.screens.mainapp

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.grupo1.hoppi.R
import androidx.navigation.NavController
import com.grupo1.hoppi.network.ApiClient
import com.grupo1.hoppi.network.likes.LikeResponse
import com.grupo1.hoppi.network.posts.PostResponse
import com.grupo1.hoppi.ui.screens.home.LikesViewModel
import com.grupo1.hoppi.ui.screens.home.PostsViewModel
import com.grupo1.hoppi.ui.screens.home.ProfileImage
import com.grupo1.hoppi.ui.screens.home.UsersViewModel
import com.grupo1.hoppi.ui.screens.home.formatDate
import com.grupo1.hoppi.ui.screens.home.formatHour
import kotlinx.coroutines.launch

data class DetailedPost(
    val id: String,
    val username: String,
    val userHandle: String,
    val content: String,
    val timestamp: String,
    val date: String,
    val likes: String,
    val commentsCount: String,
    val shares: String,
    val tag: String? = null,
    val isLiked: Boolean = false,
    val avatarColor: Color = Color(0xFFA4485F)
)

data class Comment(
    val id: String,
    val username: String,
    val userHandle: String,
    val replyToHandle: String,
    val content: String,
    val likes: String,
    val commentsCount: String,
    val shares: String,
    val isLiked: Boolean = false,
    val avatarColor: Color = Color(0xFFA4485F)
)

object SelectedPostHolder {
    var selectedPost: DetailedPost? = null
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostScreen(
    postId: String?,
    userViewModel: UsersViewModel,
    navController: NavController,
    postsViewModel: PostsViewModel,
    likesViewModel: LikesViewModel = viewModel()
) {
    val postState = remember { mutableStateOf<PostResponse?>(null) }
    val commentsState by postsViewModel.comments.collectAsState()
    var isLoading by remember { mutableStateOf(true) }
    val coroutineScope = rememberCoroutineScope()

    val avatarIndex by userViewModel.avatarIndexFlow.collectAsState(initial = 5)
    var showCommentBox by remember { mutableStateOf(false) }
    var newCommentText by remember { mutableStateOf("") }

    val token = userViewModel.token.collectAsState().value
    val currentUser = userViewModel.currentUser.collectAsState().value
    val currentUserId by userViewModel.currentUserId.collectAsState(initial = "")
    val likesMap by likesViewModel.likes.collectAsState()

    LaunchedEffect(postId) {
        if (postId != null) {
            isLoading = true
            try {
                val post = ApiClient.posts.getPost(postId)
                postState.value = post

                postsViewModel.loadComments(postId)

                likesViewModel.loadLikes(postId)
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                isLoading = false
            }
        }
    }

    Scaffold(
        topBar = { PostTopBar(navController) },
        contentWindowInsets = WindowInsets(0.dp)
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
                    .padding(bottom = if (showCommentBox) 80.dp else 0.dp)
            ) {
                item {
                    val post = postState.value

                    if (post != null) {
                        PostHeader(
                            avatarIndex,
                            post = DetailedPost(
                                id = post.id,
                                username = post.author?.display_name ?: "Anônimo",
                                userHandle = "@${post.author?.username?.lowercase() ?: "anon"}",
                                content = post.content,
                                timestamp = formatHour(post.created_at),
                                date = formatDate(post.created_at),
                                likes = (likesMap[post.id]?.size ?: 0).toString(),
                                commentsCount = post.reply_count.toString(),
                                shares = post.metadata?.get("shares")?.toString() ?: "0",
                                // tag = post.tag,
                                isLiked = post.metadata?.get("is_liked") as? Boolean ?: false
                            ),
                            currentUser = currentUser,
                            likesMap = likesMap,
                            userViewModel = userViewModel,
                            onLikeClick = {
                                if (token != null) {
                                    coroutineScope.launch {
                                        val isLiked = likesMap[post.id]?.any { it.user_id == currentUserId } ?: false
                                        if (isLiked) {
                                            likesViewModel.unlikePost(post.id, token)
                                        } else {
                                            likesViewModel.likePost(post.id, token)
                                        }
                                        likesViewModel.loadLikes(post.id)
                                    }
                                }
                            },
                            onDeletePost = {
                                postsViewModel.deletePost(post.id, token ?: "") {
                                    navController.popBackStack()
                                }
                            },
                            onCommentClick = { showCommentBox = true }
                        )
                    } else {
                        if (isLoading) {
                            CircularProgressIndicator(modifier = Modifier.padding(20.dp))
                        } else {
                            Text("Post não encontrado", modifier = Modifier.padding(20.dp))
                        }
                    }

                    Divider(color = Color(0xFF9CBDC6).copy(alpha = 0.5f), thickness = 1.dp)
                }

                items(commentsState) { comment ->
                    CommentItem(
                        avatarIndex,
                        comment = Comment(
                            id = comment.id,
                            username = comment.author?.display_name ?: "Anônimo",
                            userHandle = "@${comment.author?.username?.lowercase() ?: "anon"}",
                            replyToHandle = "",
                            content = comment.content,
                            likes = (likesMap[comment.id]?.size ?: 0).toString(),
                            commentsCount = comment.reply_count.toString(),
                            shares = comment.metadata?.get("shares")?.toString() ?: "0",
                            isLiked = comment.metadata?.get("is_liked") as? Boolean ?: false
                        ),
                        currentUser = currentUser,
                        likesMap = likesMap,
                        userViewModel = userViewModel,
                        onLikeClick = {
                            if (token != null) {
                                coroutineScope.launch {
                                    val isLiked = likesMap[comment.id]?.any { it.user_id == currentUserId } ?: false
                                    if (isLiked) {
                                        likesViewModel.unlikePost(comment.id, token)
                                    } else {
                                        likesViewModel.likePost(comment.id, token)
                                    }
                                    likesViewModel.loadLikes(comment.id)
                                }
                            }
                        },
                        onDeleteComment = {
                            coroutineScope.launch {
                                try {
                                    ApiClient.posts.deletePost(token = "Bearer $token", id = comment.id)

                                    if (postId == null) return@launch
                                    postsViewModel.loadComments(postId)
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }
                            }
                        }

                    )
                }
            }

            if (showCommentBox) {
                val keyboardController = LocalSoftwareKeyboardController.current
                LaunchedEffect(showCommentBox) {
                    keyboardController?.show()
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter)
                        .background(Color.White)
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextField(
                        value = newCommentText,
                        onValueChange = { newCommentText = it },
                        modifier = Modifier
                            .weight(1f)
                            .testTag("CommentField"),
                        placeholder = { Text("Escreva um comentário...") },
                        singleLine = true,
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White,
                            cursorColor = Color.Black,
                            focusedIndicatorColor = Color.Black,
                            unfocusedIndicatorColor = Color.Transparent,
                            disabledIndicatorColor = Color.Transparent,
                            focusedTextColor = Color(0xFF000000),
                            unfocusedTextColor = Color(0xFF000000)
                        ),
                        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                        keyboardActions = KeyboardActions(
                            onDone = { keyboardController?.hide() }
                        )
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            if (newCommentText.isNotBlank() && token != null && postId != null) {
                                coroutineScope.launch {
                                    try {
                                        postsViewModel.createComment(
                                            postId = postId,
                                            content = newCommentText.trim(),
                                            token = token
                                        )
                                        newCommentText = ""
                                        showCommentBox = false

                                        postsViewModel.loadComments(postId)

                                    } catch (e: Exception) {
                                        e.printStackTrace()
                                    }
                                }
                            }
                        },
                        modifier = Modifier.testTag("SendCommentButton"),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEC8445))
                    ) {
                        Text("Enviar")
                    }
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostTopBar(navController: NavController) {
    TopAppBar(
        windowInsets = WindowInsets(0.dp),
        title = {
            Text(
                text = "Post",
                style = MaterialTheme.typography.headlineLarge,
                color = Color.White
            )
        },
        navigationIcon = {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar", tint = Color.White)
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color(0xFFEC8445),
            titleContentColor = Color.White,
            navigationIconContentColor = Color.White
        )
    )
}

@Composable
fun PostHeader(
    avatarIndex: Int,
    post: DetailedPost,
    currentUser: String?,
    likesMap: Map<String, List<LikeResponse>>,
    userViewModel: UsersViewModel,
    onLikeClick: () -> Unit,
    onDeletePost: () -> Unit,
    onCommentClick: () -> Unit
) {
    var menuExpanded by remember { mutableStateOf(false) }
    var showReportDialog by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    val current = currentUser?.removePrefix("@")?.trim()?.lowercase()
    val author = post.userHandle.removePrefix("@").trim().lowercase()
    val currentUserId by userViewModel.currentUserId.collectAsState()
    val isLiked by remember(likesMap, currentUserId) {
        derivedStateOf {
            likesMap[post.id]?.any { it.user_id == currentUserId } == true
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .testTag("PostPrincipal")
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier.border(1.dp, Black, CircleShape)
                ) {
                    ProfileImage(
                        option = avatarIndex,
                        profileSize = 35.dp,
                        backgroundSize = 50.dp - 2.dp,
                    )
                }
                Spacer(Modifier.width(15.dp))
                Column {
                    Text(
                        post.username,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.bodyMedium,
                        fontSize = 16.sp,
                        color = Color(0xFF000000)
                    )
                    Text(
                        post.userHandle,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color(0xFFA6A6A6),
                        fontSize = 14.sp
                    )
                }
                Spacer(Modifier.weight(1f))

                Box {
                    Icon(
                        Icons.Default.MoreVert,
                        contentDescription = "Mais opções",
                        modifier = Modifier
                            .size(20.dp)
                            .clickable { menuExpanded = true },
                        tint = Color(0xFFA6A6A6)
                    )

                    DropdownMenu(
                        expanded = menuExpanded,
                        onDismissRequest = { menuExpanded = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Denunciar Post") },
                            onClick = {
                                menuExpanded = false
                                showReportDialog = true
                            }
                        )

                        if (author == current) {
                            DropdownMenuItem(
                                text = { Text("Excluir Post", color = Color.Red) },
                                onClick = {
                                    menuExpanded = false
                                    showDeleteDialog = true
                                }
                            )
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
                }
            }

            Text(
                post.content,
                style = MaterialTheme.typography.bodyMedium,
                fontSize = 16.sp,
                modifier = Modifier
                    .padding(top = 10.dp, bottom = 10.dp)
                    .testTag("PostContent_${post.id}"),
                color = Color(0xFF000000)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row {
                    Text(
                        post.timestamp,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFFA6A6A6),
                        fontSize = 12.sp
                    )
                    Text(
                        " • ${post.date}",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFFA6A6A6),
                        fontSize = 12.sp
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                if (post.tag != null) {
                    TagLabel(post.tag)
                }
            }

            Spacer(Modifier.height(16.dp))
        }
        Divider(color = Color.Black.copy(alpha = 0.5f), thickness = 1.dp)

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp, horizontal = 20.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            InteractionIcon(
                iconResId = if (isLiked) R.drawable.liked else R.drawable.like_detailed,
                count = (likesMap[post.id]?.size ?: 0).toString(),
                iconSize = 25.dp,
                textSize = 14.sp,
                onClick = {
                    Log.d("PostHeader", "Like clicked for post=${post.id}, currentUserId=$currentUserId, isLiked=$isLiked")
                    onLikeClick()
                },
            )
            InteractionIcon(
                R.drawable.comments_detailed,
                post.commentsCount,
                20.dp,
                12.sp,
                onClick = onCommentClick
            )
        }

        Divider(color = Color.Black.copy(alpha = 0.5f), thickness = 1.dp)
        if (showDeleteDialog) {
            AlertDialog(
                onDismissRequest = { showDeleteDialog = false },
                title = { Text("Excluir Post") },
                text = { Text("Tem certeza que deseja excluir este post?") },
                confirmButton = {
                    TextButton(onClick = {
                        showDeleteDialog = false
                        onDeletePost()
                    }) {
                        Text("Sim")
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


@Composable
fun CommentItem(
    avatarIndex: Int,
    comment: Comment,
    currentUser: String?,
    likesMap: Map<String, List<LikeResponse>>,
    userViewModel: UsersViewModel,
    onLikeClick: () -> Unit,
    onDeleteComment: () -> Unit
) {
    var menuExpanded by remember { mutableStateOf(false) }
    var showReportDialog by remember { mutableStateOf(false) }
    val current = currentUser?.removePrefix("@")?.trim()?.lowercase()
    val author = comment.userHandle.removePrefix("@").trim().lowercase()
    val currentUserId by userViewModel.currentUserId.collectAsState()
    val isLiked by remember(likesMap, currentUserId) {
        derivedStateOf {
            likesMap[comment.id]?.any { it.user_id == currentUserId } == true
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(horizontal = 20.dp, vertical = 16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box (
                modifier = Modifier.border(0.5.dp, Black, CircleShape)
            ) {
                ProfileImage(
                    option = avatarIndex,
                    profileSize = 30.dp,
                    backgroundSize = 40.dp - 1.dp,
                )
            }
            Spacer(Modifier.width(10.dp))
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = comment.username,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color(0xFF000000)
                    )
                    Spacer(Modifier.width(4.dp))
                    Text(
                        text = comment.userHandle,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        color = Color(0xFFA6A6A6),
                        fontSize = 12.sp,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                Text(
                    text = buildAnnotatedString {
                        withStyle(style = SpanStyle(color = Color(0xFFA6A6A6), fontSize = 12.sp)) {
                            append("Respondendo a ")
                        }
                        withStyle(style = SpanStyle(color = Color(0xFF406B8D), fontSize = 12.sp)) {
                            append(comment.replyToHandle)
                        }
                    },
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(top = 1.dp)
                )
            }
            Spacer(Modifier.width(8.dp))
            Box {
                Icon(
                    Icons.Default.MoreVert,
                    contentDescription = "Mais opções",
                    modifier = Modifier
                        .size(20.dp)
                        .clickable { menuExpanded = true },
                    tint = Color(0xFFA6A6A6)
                )

                DropdownMenu(
                    expanded = menuExpanded,
                    onDismissRequest = { menuExpanded = false }
                ) {

                    if (author == current) {
                        DropdownMenuItem(
                            text = { Text("Excluir Comentário", color = Color.Red) },
                            onClick = {
                                menuExpanded = false
                                onDeleteComment()
                            }
                        )
                    } else {
                        DropdownMenuItem(
                            text = { Text("Denunciar Comentário") },
                            onClick = {
                                menuExpanded = false
                                showReportDialog = true
                            }
                        )
                    }
                }
            }

            if (showReportDialog) {
                ReportDialog(
                    title = "Denunciar Comentário",
                    onDismiss = { showReportDialog = false },
                    onConfirm = { reason ->
                        showReportDialog = true
                    }
                )
            }
        }

        Text(
            comment.content,
            style = MaterialTheme.typography.bodyMedium,
            fontSize = 14.sp,
            modifier = Modifier.padding(start = 47.dp, top = 8.dp, bottom = 8.dp),
            color = Color(0xFF000000)
        )

        Row(
            modifier = Modifier.padding(start = 47.dp),
            horizontalArrangement = Arrangement.spacedBy(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            InteractionIcon(
                iconResId = if (isLiked) R.drawable.liked else R.drawable.like_detailed,
                count = (likesMap[comment.id]?.size ?: 0).toString(),
                iconSize = 20.dp,
                textSize = 14.sp,
                onClick = {
                    Log.d("CommentItem", "Like clicked for comment=${comment.id}, currentUserId=$currentUserId, isLiked=$isLiked")
                    onLikeClick()
                          },
                )
                }
        }
    }


@Composable
fun InteractionIcon(
    iconResId: Int,
    count: String,
    iconSize: Dp,
    textSize: TextUnit,
    onClick: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clickable { onClick() }
    ) {
        Image(
            painter = painterResource(id = iconResId),
            contentDescription = null,
            modifier = Modifier.size(iconSize)
        )
        Spacer(Modifier.width(4.dp))
        Text(
            count,
            style = MaterialTheme.typography.bodySmall,
            color = Color(0xFFA6A6A6),
            fontSize = textSize
        )
    }
}


@Composable
fun TagLabel(tagName: String) {

    val (bgColor, textColor, iconRes) = when (tagName) {
        "Estudo" -> Triple(Color(0xFF85D39F), Color(0xFF00822B), R.drawable.estudo_icon)
        "Venda" -> Triple(Color(0xFF9CBDC6), Color(0xFF406B8D), R.drawable.venda_icon)
        "Info" -> Triple(Color(0xFFEDC8B1), Color(0xFFEC8445), R.drawable.info_icon)
        else -> Triple(Color.LightGray, Color.DarkGray, null)
    }

    Row(
        modifier = Modifier
            .background(bgColor, RoundedCornerShape(4.dp))
            .padding(horizontal = 8.dp, vertical = 3.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        if (iconRes != null) {
            Image(
                painter = painterResource(id = iconRes),
                contentDescription = null,
                modifier = Modifier.size(14.dp)
            )
            Spacer(Modifier.width(6.dp))
        }

        Text(
            text = tagName,
            color = textColor,
            fontSize = 13.sp
        )
    }
}

@Composable
fun ReportDialog(
    title: String,
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
) {
    var reason by remember { mutableStateOf("") }
    var sent by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(title) },
        text = {
            if (!sent) {
                Column {
                    Text("Por que você quer denunciar?")
                    Spacer(Modifier.height(12.dp))
                    TextField(
                        value = reason,
                        onValueChange = { reason = it },
                        placeholder = { Text("Descreva aqui...") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = false
                    )
                }
            } else {
                Text("Denúncia enviada com sucesso!")
            }
        },
        confirmButton = {
            if (!sent) {
                TextButton(
                    onClick = {
                        onConfirm(reason)
                        sent = true
                    }
                ) { Text("Enviar") }
            } else {
                TextButton(onClick = onDismiss) { Text("Fechar") }
            }
        },
        dismissButton = {
            if (!sent) {
                TextButton(onClick = onDismiss) { Text("Cancelar") }
            }
        }
    )
}