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
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.grupo1.hoppi.R
import com.grupo1.hoppi.ui.screens.home.Post
import com.grupo1.hoppi.ui.screens.home.PostsViewModel
import com.grupo1.hoppi.ui.screens.home.ProfileImage
import com.grupo1.hoppi.ui.screens.home.UserViewModel

@Composable
fun FeedScreen(
    modifier: Modifier,
    postsViewModel: PostsViewModel,
    userViewModel: UserViewModel,
    onPostClick: (postId: Int) -> Unit,
    onNotificationsClick: () -> Unit,
    onProfileClick: () -> Unit,
) {
    val posts = postsViewModel.posts
    val avatarIndex by userViewModel.avatarIndexFlow.collectAsState(initial = 5)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        FeedTopBarContent(
            onNotificationsClick = onNotificationsClick,
            onProfileClick = onProfileClick
        )
        Divider(color = Color(0xFF9CBDC6), thickness = 1.dp)

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(1.dp)
        ) {
            items(posts) { post ->
                PostCard(
                    avatarIndex,
                    post = post,
                    onPostClick = onPostClick,
                    onLikeClick = { postsViewModel.toggleLike(post.id) }
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
fun PostCard(
    avatarIndex: Int,
    post: Post,
    onPostClick: (postId: Int) -> Unit,
    onLikeClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .clickable { onPostClick(post.id) }
            .padding(20.dp),
        verticalAlignment = Alignment.Top
    ) {
        Box (
            modifier = Modifier.border(0.5.dp, Black, CircleShape)
        ) {
            ProfileImage(
                option = avatarIndex,
                profileSize = 30.dp,
                backgroundSize = 40.dp - 1.dp,
            )
        }

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
                            post.handle,
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
                    painter = painterResource(id = if (post.isLiked) R.drawable.liked else R.drawable.like_detailed),
                    contentDescription = null,
                    modifier = Modifier
                        .size(20.dp)
                        .clickable { onLikeClick() }
                        .testTag("like_icon")
                )
                Spacer(Modifier.width(4.dp))
                Text(post.likes.toString(), style = MaterialTheme.typography.bodySmall, color = Color(0xFF000000))

                Spacer(Modifier.width(16.dp))

                Image(
                    painter = painterResource(id = R.drawable.comments_detailed),
                    contentDescription = "Comentários",
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
