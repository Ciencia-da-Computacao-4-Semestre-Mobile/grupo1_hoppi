package com.grupo1.hoppi.ui.screens.mainapp

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.grupo1.hoppi.R
import androidx.navigation.NavController
import com.grupo1.hoppi.ui.screens.home.PostsViewModel

data class DetailedPost(
    val id: Int,
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
    val id: Int,
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

val mockDetailedPost = DetailedPost(
    id = 1,
    username = "Fulano de Tal",
    userHandle = "@fulan.tal",
    content = "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book.",
    timestamp = "21:30",
    date = "19 set 2025",
    likes = "10 K",
    commentsCount = "1,5 K",
    shares = "2 K"
)

val mockComments = listOf(
    Comment(1, "Pessoa 2", "@pessoa_2", "@fulan.tal", "Lorem Ipsum is simply dummy text of the printing and typesetting industry.", "15", "1", "0"),
    Comment(2, "Pessoa 3", "@pessoa_3", "@fulan.tal", "Lorem Ipsum is simply dummy text of the printing and typesetting industry.", "20", "2", "1"),
    Comment(3, "Pessoa 4", "@pessoa_4", "@fulan.tal", "Lorem Ipsum is simply dummy text of the printing and typesetting industry.", "20", "2", "1"),
    Comment(4, "Pessoa 5", "@pessoa_5", "@fulan.tal", "Lorem Ipsum is simply dummy text of the printing and typesetting industry.", "25", "5", "2"),
    Comment(5, "Pessoa 6", "@pessoa_6", "@fulan.tal", "Lorem Ipsum is simply dummy text of the printing and typesetting industry.", "25", "0", "2"),
    Comment(6, "Pessoa 7", "@pessoa_7", "@fulan.tal", "Lorem Ipsum is simply dummy text of the printing and typesetting industry.", "25", "1", "1"),
)

object SelectedPostHolder {
    var selectedPost: DetailedPost? = null
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostScreen(
    postId: Int?,
    navController: NavController,
    postsViewModel: PostsViewModel
) {
    val commentList = remember { mutableStateListOf<Comment>().apply { addAll(mockComments) } }
    var showCommentBox by remember { mutableStateOf(false) }
    var newCommentText by remember { mutableStateOf("") }

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
                    val post = postsViewModel.posts.firstOrNull { it.id == postId }

                    if (post != null) {
                        PostHeader(
                            post = DetailedPost(
                                id = post.id,
                                username = post.username,
                                userHandle = "@${post.username.lowercase()}",
                                content = post.content,
                                timestamp = "agora",
                                date = "hoje",
                                likes = post.likes.toString(),
                                commentsCount = post.comments.toString(),
                                shares = post.shares.toString(),
                                tag = post.tag,
                                isLiked = post.isLiked
                            ),
                            onLikeClick = { postsViewModel.toggleLike(post.id) },
                            onDeletePost = {
                                postsViewModel.deletePost(post.id)
                                navController.popBackStack()
                            },
                            onCommentClick = { showCommentBox = true }
                        )
                    } else {
                        Text("Post não encontrado", modifier = Modifier.padding(20.dp))
                    }

                    Divider(color = Color(0xFF9CBDC6).copy(alpha = 0.5f), thickness = 1.dp)
                }

                items(commentList) { comment ->
                    CommentItem(
                        comment = comment,
                        onLikeClick = { /* atualizar likes */ },
                        onDeleteComment = {
                            commentList.remove(comment)
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
                        modifier = Modifier.weight(1f),
                        placeholder = { Text("Escreva um comentário...") },
                        singleLine = true,
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color(0xFFEDC8B1),
                            unfocusedContainerColor = Color(0xFFEDC8B1),
                            cursorColor = Color.Black,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            disabledIndicatorColor = Color.Transparent
                        ),
                        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                        keyboardActions = KeyboardActions(
                            onDone = { keyboardController?.hide() }
                        )
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            if (newCommentText.isNotBlank()) {
                                commentList.add(
                                    0,
                                    Comment(
                                        id = (commentList.maxOfOrNull { it.id } ?: 0) + 1,
                                        username = "Você",
                                        userHandle = "@voce",
                                        replyToHandle = "",
                                        content = newCommentText,
                                        likes = "0",
                                        commentsCount = "0",
                                        shares = "0"
                                    )
                                )

                                postsViewModel.addComment(postId!!)

                                newCommentText = ""
                                showCommentBox = false
                            }
                        },
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
    post: DetailedPost,
    onLikeClick: () -> Unit,
    onDeletePost: () -> Unit,
    onCommentClick: () -> Unit
) {
    var menuExpanded by remember { mutableStateOf(false) }
    var showReportDialog by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .testTag("PostPrincipal")
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(50.dp)
                        .clip(CircleShape)
                        .background(post.avatarColor)
                )
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

                        DropdownMenuItem(
                            text = { Text("Excluir Post", color = Color.Red) },
                            onClick = {
                                menuExpanded = false
                                showDeleteDialog = true
                            }
                        )
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
                iconResId = if (post.isLiked) R.drawable.liked else R.drawable.like_detailed,
                count = post.likes,
                iconSize = 25.dp,
                textSize = 14.sp,
                onClick = onLikeClick,
            )
            InteractionIcon(
                R.drawable.comments_detailed,
                post.commentsCount,
                25.dp,
                12.sp,
                onClick = onCommentClick
            )
            InteractionIcon(
                R.drawable.share_detailed,
                post.shares,
                25.dp,
                14.sp,
                onClick = { /* */ })
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
    comment: Comment,
    onLikeClick: () -> Unit,
    onDeleteComment: () -> Unit
) {
    var menuExpanded by remember { mutableStateOf(false) }
    var showReportDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(horizontal = 20.dp, vertical = 16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(35.dp)
                    .clip(CircleShape)
                    .background(comment.avatarColor)
            )
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

                    if (comment.userHandle == "@voce") {
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
                iconResId = if (comment.isLiked) R.drawable.liked else R.drawable.like_detailed,
                count = comment.likes,
                iconSize = 25.dp,
                textSize = 14.sp,
                onClick = onLikeClick
            )
            InteractionIcon(R.drawable.comments_detailed, comment.commentsCount, 15.dp, 10.sp, onClick = { /* */ })
            InteractionIcon(R.drawable.share_detailed, comment.shares, 15.dp, 10.sp, onClick = { /* */ })
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