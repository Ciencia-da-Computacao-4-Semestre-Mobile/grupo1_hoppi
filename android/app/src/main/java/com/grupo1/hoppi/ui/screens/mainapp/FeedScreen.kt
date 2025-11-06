package com.grupo1.hoppi.ui.screens.mainapp

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.grupo1.hoppi.R

data class Post(val id: Int, val username: String, val content: String, val isSale: Boolean = false)

val mockPosts = List(10) { i ->
    Post(
        id = i,
        username = "Fulano de Tal",
        content = "Lorem Ipsum is simply dummy text of the printing and typesetting industry",
        isSale = i % 3 == 0
    )
}

@Composable
fun FeedScreen(
    modifier: Modifier = Modifier,
    onPostClick: (postId: Int) -> Unit,
    onNotificationsClick: () -> Unit,
    onProfileClick: () -> Unit,
) {
    val listBackgroundColor = Color.White
    val systemUiController = rememberSystemUiController()

    SideEffect {
        systemUiController.setStatusBarColor(color = Color.White, darkIcons = true)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {

    FeedTopBarContent(
        onNotificationsClick = onNotificationsClick,
        onProfileClick = onProfileClick
    )
    Divider(color = Color(0xFF9CBDC6).copy(alpha = 1.0f), thickness = 1.dp)

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(listBackgroundColor),
        verticalArrangement = Arrangement.spacedBy(1.dp),
        contentPadding = PaddingValues(bottom = 0.dp)
    ) {

        items(mockPosts) { post ->
            PostCard(
                post = post,
                onPostClick = onPostClick
            )
            Divider(color = Color(0xFF9CBDC6).copy(alpha = 0.5f), thickness = 1.dp)
        }
    }
    }
}

@Composable
fun FeedTopBarContent(
    onNotificationsClick: () -> Unit,
    onProfileClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(horizontal = 20.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        IconButton(onClick = onProfileClick) {
            Image(
                painter = painterResource(id = R.drawable.user),
                contentDescription = "Perfil",
                modifier = Modifier.size(35.dp)
            )
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(id = R.drawable.hoppi_orange_logo),
                contentDescription = "Logo Hoppi",
                modifier = Modifier
                    .size(90.dp, 35.dp)
            )
        }

        IconButton(onClick = onNotificationsClick) {
            Image(
                painter = painterResource(id = R.drawable.notificacoes),
                contentDescription = "Notificações",
                modifier = Modifier.size(30.dp)
            )
        }
    }
}

@Composable
fun PostCard(post: Post, onPostClick: (postId: Int) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .clickable { onPostClick(post.id) }
            .padding(20.dp),
        verticalAlignment = Alignment.Top
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(Color(0xFFB56576))
        )

        Spacer(Modifier.width(20.dp))

        Column(modifier = Modifier.weight(1f)) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            post.username,
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.bodyMedium,
                            fontSize = 14.sp,
                            color = Color(0xFF000000)
                        )
                        Spacer(Modifier.width(5.dp))
                        Text(
                            "@fulan.tal",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color(0xFFA6A6A6),
                            fontSize = 14.sp
                        )
                    }
                }
            }

            Text(
                post.content,
                style = MaterialTheme.typography.bodyMedium,
                fontSize = 14.sp,
                modifier = Modifier.padding(top = 10.dp, bottom = 10.dp),
                color = Color(0xFF000000)
            )

            Row(verticalAlignment = Alignment.CenterVertically) {

                Image(
                    painter = painterResource(id = R.drawable.like_detailed),
                    contentDescription = "Curtidas",
                    modifier = Modifier.size(12.dp)
                )
                Spacer(Modifier.width(4.dp))
                Text("10 K", style = MaterialTheme.typography.bodySmall, color = Color(0xFF000000))

                Spacer(Modifier.width(16.dp))

                Image(
                    painter = painterResource(id = R.drawable.comments_detailed),
                    contentDescription = "Comentários",
                    modifier = Modifier.size(12.dp)
                )
                Spacer(Modifier.width(4.dp))
                Text("1,5 K", style = MaterialTheme.typography.bodySmall, color = Color(0xFF000000))

                Spacer(Modifier.weight(1f))

                if (post.isSale) {
                    AssistChip(
                        onClick = { /* Sem Ação */ },
                        label = {
                            Image(
                                painter = painterResource(id = R.drawable.venda_icon),
                                contentDescription = "Venda",
                                modifier = Modifier.size(12.dp)
                            )
                            Spacer(Modifier.width(4.dp))
                            Text(
                                "Venda",
                                fontWeight = FontWeight.Light,
                                style = MaterialTheme.typography.bodyMedium,
                                fontSize = 10.sp,
                                color = Color(0xFF406B8D)
                            )
                        },
                        colors = AssistChipDefaults.assistChipColors(
                            containerColor = Color(0xFF9CBDC6)
                        ),
                        border = null,
                        shape = RoundedCornerShape(5.dp),
                        modifier = Modifier.height(20.dp)
                    )
                }
            }
        }
    }
}