package com.grupo1.hoppi.ui.screens.mainapp

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Public
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.grupo1.hoppi.ui.screens.home.PostsViewModel

@Composable
fun CreateCommunityScreen(navController: NavController) {
    var communityName by remember { mutableStateOf("") }
    var communityDescription by remember { mutableStateOf("") }
    var isPrivacyExpanded by remember { mutableStateOf(false) }
    var selectedPrivacyOption by remember { mutableStateOf("Público") }
    val systemUiController = rememberSystemUiController()
    val useDarkIcons = false
    val isCreateEnabled = communityName.isNotBlank()
    val postsViewModel: PostsViewModel = viewModel()

    DisposableEffect(systemUiController) {
        systemUiController.setStatusBarColor(
            color = HoppiOrange,
            darkIcons = useDarkIcons
        )
        onDispose {}
    }

    Scaffold(
        topBar = {
            CreateCommunityTopBar(onCloseClick = { navController.popBackStack() })
        },
        containerColor = Color.White
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 20.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .background(LightBlue)
                    .clickable { /* Handle add photo click */ },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Add Foto",
                    color = Color.White,
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.height(30.dp))

            Text(
                text = "Nome *",
                color = HoppiOrange,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier
                    .padding(bottom = 5.dp)
                    .align(Alignment.Start)
            )

            OutlinedTextField(
                value = communityName,
                onValueChange = { communityName = it },
                label = { Text("Nome", color = GrayColor) },
                singleLine = true,
                shape = RoundedCornerShape(30.dp),
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = HoppiOrange,
                    unfocusedBorderColor = Color.Black.copy(alpha = 0.5f),
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    cursorColor = HoppiOrange
                )
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Descrição",
                color = HoppiOrange,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier
                    .padding(bottom = 5.dp)
                    .align(Alignment.Start)
            )

            OutlinedTextField(
                value = communityDescription,
                onValueChange = { communityDescription = it },
                label = { Text("Descrição", color = GrayColor) },
                minLines = 3,
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = HoppiOrange,
                    unfocusedBorderColor = Color.Black.copy(alpha = 0.5f),
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    cursorColor = HoppiOrange
                )
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Privacidade",
                color = HoppiOrange,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier
                    .padding(bottom = 5.dp)
                    .align(Alignment.Start)
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentSize(Alignment.TopStart)
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .clickable { isPrivacyExpanded = true },
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Spacer(modifier = Modifier.width(0.dp))

                            when (selectedPrivacyOption) {
                                "Público" -> Icon(Icons.Default.Public, contentDescription = "Public Icon", tint = Color.Black)
                                "Privado" -> Icon(Icons.Default.Lock, contentDescription = "Private Icon", tint = Color.Black)
                            }

                            Spacer(modifier = Modifier.width(10.dp))

                            Text(
                                text = selectedPrivacyOption,
                                color = Color.Black,
                                fontSize = 16.sp
                            )
                        }

                        Box(
                            modifier = Modifier.fillMaxHeight(),
                            contentAlignment = Alignment.CenterEnd
                        ) {
                            Icon(
                                Icons.Default.ArrowDropDown,
                                contentDescription = "Dropdown Arrow",
                                tint = Color.Black
                            )
                            Spacer(modifier = Modifier.width(0.dp))
                        }
                    }

                    Divider(
                        color = Color.Black.copy(alpha = 0.5f),
                        thickness = 1.dp
                    )
                }

                DropdownMenu(
                    expanded = isPrivacyExpanded,
                    onDismissRequest = { isPrivacyExpanded = false },
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .background(Color.White)
                        .border(1.dp, Color.Gray.copy(alpha = 0.5f))
                ) {
                    DropdownMenuItem(
                        text = {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Public, contentDescription = "Public Icon")
                                Spacer(Modifier.width(8.dp))
                                Text("Público")
                            }
                        },
                        onClick = {
                            selectedPrivacyOption = "Público"
                            isPrivacyExpanded = false
                        }
                    )
                    DropdownMenuItem(
                        text = {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Lock, contentDescription = "Private Icon")
                                Spacer(Modifier.width(8.dp))
                                Text("Privado")
                            }
                        },
                        onClick = {
                            selectedPrivacyOption = "Privado"
                            isPrivacyExpanded = false
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(30.dp))

            Button(
                onClick = {
                    AppCommunityManager.createCommunity(
                        name = communityName,
                        description = communityDescription,
                        privacy = selectedPrivacyOption,
                        creatorUsername = postsViewModel.currentUser
                    )
                    navController.popBackStack()
                },
                enabled = isCreateEnabled,
                modifier = Modifier
                    .width(110.dp)
                    .height(48.dp)
                    .clip(RoundedCornerShape(25.dp)),
                colors = ButtonDefaults.buttonColors(containerColor = HoppiOrange),
                contentPadding = PaddingValues(horizontal = 20.dp, vertical = 12.dp)
            ) {
                Text("Criar", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.SemiBold, style = MaterialTheme.typography.headlineLarge)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateCommunityTopBar(onCloseClick: () -> Unit) {
    TopAppBar(
        windowInsets = WindowInsets(0.dp),
        title = { Text("", color = Branco, fontSize = 20.sp) },
        navigationIcon = {
            IconButton(onClick = onCloseClick) {
                Icon(Icons.Default.Close, contentDescription = "Fechar", tint = Branco)
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = Laranja)
    )
}

