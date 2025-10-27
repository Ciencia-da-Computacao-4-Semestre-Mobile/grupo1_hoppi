package com.grupo1.hoppi.ui.screens.mainapp

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.grupo1.hoppi.R
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.grupo1.hoppi.ui.components.mainapp.BottomNavBar

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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostScreen(
    navController: NavController,
    postId: Int?
) {

    Scaffold(
        topBar = { PostTopBar(navController) },
        bottomBar = { BottomNavBar(navController) }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(paddingValues)
        ) {
            item {
                PostHeader(post = mockDetailedPost)
                Divider(color = Color(0xFF9CBDC6).copy(alpha = 0.5f), thickness = 1.dp)
            }

            items(mockComments) { comment ->
                CommentItem(comment = comment)
                Divider(color = Color(0xFF9CBDC6).copy(alpha = 0.5f), thickness = 1.dp)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostTopBar(navController: NavController) {
    TopAppBar(
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
fun PostHeader(post: DetailedPost) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
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
                    Text(post.username, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyMedium, fontSize = 16.sp)
                    Text(post.userHandle, style = MaterialTheme.typography.bodyMedium, color = Color(0xFFA6A6A6), fontSize = 14.sp)
                }
                Spacer(Modifier.weight(1f))
                Icon(Icons.Default.MoreVert, contentDescription = "Mais opções", modifier = Modifier.size(20.dp), tint = Color(0xFFA6A6A6))
            }

            Text(post.content, style = MaterialTheme.typography.bodyMedium, fontSize = 16.sp, modifier = Modifier.padding(top = 10.dp, bottom = 10.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(post.timestamp, style = MaterialTheme.typography.bodySmall, color = Color(0xFFA6A6A6), fontSize = 12.sp)
                Text(" • ${post.date}", style = MaterialTheme.typography.bodySmall, color = Color(0xFFA6A6A6), fontSize = 12.sp)
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
            InteractionIcon(R.drawable.like_detailed, post.likes, 25.dp, 14.sp)
            InteractionIcon(R.drawable.comments_detailed, post.commentsCount, 25.dp, 12.sp)
            InteractionIcon(R.drawable.share_detailed, post.shares, 25.dp, 14.sp)
        }

        Divider(color = Color.Black.copy(alpha = 0.5f), thickness = 1.dp)
    }
}

@Composable
fun CommentItem(comment: Comment) {
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
            Icon(Icons.Default.MoreVert, contentDescription = "Mais opções", modifier = Modifier.size(20.dp), tint = Color(0xFFA6A6A6))
        }

        Text(comment.content, style = MaterialTheme.typography.bodyMedium, fontSize = 14.sp, modifier = Modifier.padding(start = 47.dp, top = 8.dp, bottom = 8.dp))

        Row(
            modifier = Modifier.padding(start = 47.dp),
            horizontalArrangement = Arrangement.spacedBy(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            InteractionIcon(R.drawable.like_detailed, comment.likes, 15.dp, 10.sp)
            InteractionIcon(R.drawable.comments_detailed, comment.commentsCount, 15.dp, 10.sp)
            InteractionIcon(R.drawable.share_detailed, comment.shares, 15.dp, 10.sp)
        }
    }
}

@Composable
fun InteractionIcon(
    iconResId: Int,
    count: String,
    iconSize: Dp,
    textSize: TextUnit
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Image(
            painter = painterResource(id = iconResId),
            contentDescription = null,
            modifier = Modifier.size(iconSize),
            alignment = Alignment.Center
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


@Preview(showBackground = true, widthDp = 360)
@Composable
fun PostScreenPreview() {
    PostScreen(navController = rememberNavController(), postId = 1)
}