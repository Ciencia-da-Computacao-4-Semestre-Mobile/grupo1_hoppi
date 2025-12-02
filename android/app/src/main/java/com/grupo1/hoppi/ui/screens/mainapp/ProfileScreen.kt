package com.grupo1.hoppi.ui.screens.mainapp

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.grupo1.hoppi.R
import com.grupo1.hoppi.network.posts.PostResponse
import com.grupo1.hoppi.ui.screens.home.PostsViewModel
import com.grupo1.hoppi.ui.screens.home.ProfileImage
import com.grupo1.hoppi.ui.screens.home.UsersViewModel

val Pink = Color(0xFFA4485F)
val LightBlueDivider = Color(0xFF9CBDC6)
val LightGreyText = Color(0xFFA6A6A6)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    navController: NavController,
    postsViewModel: PostsViewModel,
    userViewModel: UsersViewModel,
    onPostClick: (postId: String) -> Unit,
    onSettingsClick: () -> Unit,
    token: String
) {
    LaunchedEffect(token) {
        if (token.isNotEmpty()) {
            userViewModel.loadProfile(token)
        }
    }

    val profile by userViewModel.profile.collectAsState()

    if (profile == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(
                color = HoppiOrange
            )
        }
        return
    }

    val user = profile!!

    LaunchedEffect(user.id, token) {
        if (token.isNotEmpty()) {
            postsViewModel.loadUserPosts(user.id, token)
        }
    }

    val posts by postsViewModel.userPosts.collectAsState()
    val avatarIndex by userViewModel.avatarIndexFlow.collectAsState(initial = 5)

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        verticalArrangement = Arrangement.spacedBy(1.dp)
    ) {
        item {
            ProfileHeaderContent(
                avatarIndex = avatarIndex,
                name = user.displayName,
                username = "@${user.username}",
                bio = user.institution ?: "",
                navController = navController,
                onSettingsClick = onSettingsClick
            )
            Divider(color = LightBlueDivider.copy(alpha = 0.5f), thickness = 1.dp)
        }

        items(posts) { post ->
            ProfilePostCard(
                avatarIndex = avatarIndex,
                post = post,
                onPostClick = onPostClick,
                onLikeClick = { /* Placeholder, like ainda não implementado */ }
            )
            Divider(color = LightBlueDivider.copy(alpha = 0.5f), thickness = 1.dp)
        }
    }
}

@Composable
fun ProfileHeaderContent(
    avatarIndex: Int,
    name: String,
    username: String,
    bio: String,
    navController: NavController,
    onSettingsClick: () -> Unit
) {
    val headerHeight = 230.dp
    val profileSize = 140.dp

    var expanded by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White),
        contentAlignment = Alignment.TopCenter
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(headerHeight)
        ) {
            val curveHeight = size.height * 0.5f
            val path = Path().apply {
                moveTo(0f, curveHeight)
                quadraticBezierTo(
                    size.width / 2, size.height * 1.5f,
                    size.width, curveHeight
                )
                lineTo(size.width, 0f)
                lineTo(0f, 0f)
                close()
            }
            drawPath(path, color = Pink)
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 15.dp, start = 8.dp, end = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Voltar",
                    tint = Color.White,
                    modifier = Modifier.size(30.dp)
                )
            }

            Box {
                IconButton(onClick = { expanded = true }) {
                    Icon(
                        Icons.Default.MoreVert,
                        contentDescription = "Mais opções",
                        tint = Color.White,
                        modifier = Modifier.size(30.dp)
                    )
                }

                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    DropdownMenuItem(
                        text = { Text("Configurações") },
                        onClick = {
                            onSettingsClick()
                            expanded = false
                        },
                        leadingIcon = {
                            Icon(Icons.Filled.Settings, contentDescription = "Configurações")
                        }
                    )
                }
            }
        }

          Box(
            modifier = Modifier
                .size(230.dp)
                .align(Alignment.BottomCenter)
                .offset(y = profileSize / 2f)
                .background(Color.White, CircleShape)
                .padding(25.dp)
                .border(3.dp, Black, CircleShape),
            contentAlignment = Alignment.Center
        ) {
              ProfileImage(
                  option = avatarIndex,
                  profileSize = profileSize - 12.dp,
                  backgroundSize = 230.dp - 6.dp,
              )
        }
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(top = profileSize / 2 - 10.dp)
    ) {
        Text(username, color = LightGreyText, style = MaterialTheme.typography.bodyMedium, fontSize = 14.sp)
        Spacer(Modifier.height(10.dp))
        Text(name, style = MaterialTheme.typography.headlineLarge, fontSize = 20.sp, color = Color(0xFF000000))
        Spacer(Modifier.height(20.dp))

        Row(horizontalArrangement = Arrangement.SpaceEvenly, modifier = Modifier.fillMaxWidth().padding(horizontal = 85.dp)) {
            ProfileStat("150", "Seguidores")
            ProfileStat("435", "Seguindo")
            ProfileStat("20", "Posts")
        }

        Spacer(Modifier.height(20.dp))
        Text(
            text = "Instituição: ${bio.ifBlank { "Sem bio definida." }}",
            style = MaterialTheme.typography.bodyMedium,
            fontSize = 14.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 40.dp),
            color = Color(0xFF000000)
        )
        Spacer(Modifier.height(20.dp))
    }
}

@Composable
fun ProfileStat(count: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = count,
            fontWeight = Bold,
            fontSize = 20.sp,
            color = Color(0xFF000000),
            style = MaterialTheme.typography.bodyMedium
        )
        Text(
            text = label,
            fontSize = 14.sp,
            color = LightGreyText,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
fun ProfilePostCard(
    avatarIndex: Int,
    post: PostResponse,
    onPostClick: (postId: String) -> Unit,
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
                profileSize = 35.dp,
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
                            color = LightGreyText,
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
                color = Color(0xFF000000),
            )

            val tags = post.metadata
                ?.get("tags")
                ?.let { raw ->
                    when (raw) {
                        is List<*> -> raw.filterIsInstance<String>()
                        is String -> listOf(raw)
                        else -> emptyList()
                    }
                } ?: emptyList()

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

                tags.forEach { tagName ->
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