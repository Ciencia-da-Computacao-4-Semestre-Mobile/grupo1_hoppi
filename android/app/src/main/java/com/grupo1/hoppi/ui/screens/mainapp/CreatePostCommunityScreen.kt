package com.grupo1.hoppi.ui.screens.mainapp

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import kotlinx.coroutines.delay
import com.grupo1.hoppi.R
import com.grupo1.hoppi.ui.screens.home.PostsViewModel
import com.grupo1.hoppi.ui.screens.home.ProfileImage
import com.grupo1.hoppi.ui.screens.home.UserViewModel

@Composable
fun CreatePostCommunityScreen(
    navController: NavController,
    userViewModel: UserViewModel,
    postsViewModel: PostsViewModel,
    communityId: Int
) {
    val avatarIndex by userViewModel.avatarIndexFlow.collectAsState(initial = 5)
    var postText by remember { mutableStateOf("") }
    var selectedTag by remember { mutableStateOf<TagItem?>(null) }
    var showMenuTags by remember { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }

    val availableTags = listOf(
        TagItem("Estudo", VerdeEstudo, R.drawable.estudo_icon),
        TagItem("Venda", AzulVenda, R.drawable.venda_icon),
        TagItem("Info", LaranjaInfo, R.drawable.info_icon)
    )

    LaunchedEffect(Unit) {
        delay(300)
        focusRequester.requestFocus()
    }

    Scaffold(
        topBar = {
            CreatePostTopBar(
                postText = postText,
                selectedTag = selectedTag,
                onCloseClick = { navController.popBackStack() },
                onPublish = {
                    if (postText.isNotBlank()) {
                        postsViewModel.addCommunityPost(
                            content = postText.trim(),
                            username = "Fulano de Tal",
                            isSale = selectedTag?.name == "Venda",
                            tag = selectedTag?.name,
                            communityId = communityId
                        )
                        navController.popBackStack()
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(top = paddingValues.calculateTopPadding())
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .imePadding()
                    .padding(horizontal = 20.dp)
                    .padding(bottom = paddingValues.calculateBottomPadding() + 60.dp)
            ) {
                Row(
                    modifier = Modifier
                        .padding(top = 20.dp)
                        .fillMaxWidth()
                        .weight(1f),
                    verticalAlignment = Alignment.Top
                ) {
                    Box (
                        modifier = Modifier.border(1.dp, Black, CircleShape)
                    ) {
                        ProfileImage(
                            option = avatarIndex,
                            profileSize = 30.dp,
                            backgroundSize = 50.dp - 2.dp,
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))

                    BasicTextField(
                        value = postText,
                        onValueChange = { if (it.length <= 300) postText = it },
                        modifier = Modifier
                            .fillMaxSize()
                            .focusRequester(focusRequester),
                        textStyle = TextStyle(fontSize = 16.sp, color = Color.Black),
                        decorationBox = { innerTextField ->
                            Column(
                                modifier = Modifier.fillMaxSize()
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .weight(1f)
                                        .padding(vertical = 12.dp)
                                ) {
                                    if (postText.isEmpty()) {
                                        Text(
                                            text = "O que você está pensando?",
                                            color = CinzaClaro,
                                            fontSize = 16.sp,
                                        )
                                    }
                                    innerTextField()
                                }
                            }
                        }
                    )
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .background(Color.White)
                    .imePadding()
                    .heightIn(min = 60.dp)
            ) {
                Divider(
                    color = Color(0xFF000000),
                    thickness = 1.dp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.TopCenter)
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter)
                        .padding(horizontal = 20.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        selectedTag?.let { tag ->
                            TagDisplay(tag = tag, onDismiss = { selectedTag = null })
                            Spacer(modifier = Modifier.width(8.dp))
                        }

                        Box(contentAlignment = Alignment.BottomStart) {
                            AdicionarTagButton(
                                onClick = { showMenuTags = !showMenuTags }
                            )
                        }
                    }
                    Text(
                        text = "${postText.length}/300 caracteres",
                        color = CinzaClaro,
                        fontSize = 12.sp
                    )
                }
            }

            AnimatedVisibility(
                visible = showMenuTags,
                modifier = Modifier
                    .imePadding()
                    .align(Alignment.BottomStart)
                    .padding(start = 20.dp, bottom = 50.dp)
                    .offset(y = (-10).dp)
                    .zIndex(5f),
                enter = expandVertically(expandFrom = Alignment.Bottom),
                exit = shrinkVertically(shrinkTowards = Alignment.Bottom)
            ) {
                Column(
                    modifier = Modifier
                        .shadow(8.dp, RoundedCornerShape(10.dp))
                        .background(Color.White, RoundedCornerShape(10.dp))
                        .padding(12.dp, 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    availableTags.forEach { tag ->
                        TagMenuItem(
                            tag = tag,
                            onClick = {
                                selectedTag = it
                                showMenuTags = false
                            }
                        )
                        if (tag != availableTags.last()) {
                            Spacer(modifier = Modifier.height(10.dp))
                        }
                    }
                }
            }
        }
    }
}