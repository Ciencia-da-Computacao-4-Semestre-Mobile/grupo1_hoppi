package com.grupo1.hoppi.ui.screens.mainapp

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Public
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun EditCommunityScreen(
    navController: NavController,
    communityId: Int
) {
    val community = remember { mutableStateOf(findCommunityById(communityId)) }
    if (community.value == null) {
        navController.popBackStack()
        return
    }

    var newName by remember { mutableStateOf(community.value!!.name) }
    var newDescription by remember { mutableStateOf(community.value!!.description) }
    var isPrivacyExpanded by remember { mutableStateOf(false) }
    var selectedPrivacyOption by remember { mutableStateOf(if (community.value!!.isPrivate) "Privado" else "Público") }

    Scaffold(
        topBar = {
            EditCommunityTopBar(navController = navController)
        },
        containerColor = Color.White
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 20.dp, vertical = 20.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(10.dp))

            Text(
                text = "Nome *",
                color = HoppiOrange,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.align(Alignment.Start)
            )
            OutlinedTextField(
                value = newName,
                onValueChange = { newName = it },
                label = { Text("Nome da Comunidade", color = GrayColor) },
                singleLine = true,
                shape = RoundedCornerShape(30.dp),
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = HoppiOrange,
                    unfocusedBorderColor = Color.Black.copy(alpha = 0.5f),
                    cursorColor = HoppiOrange,
                    focusedPlaceholderColor = Color(0xFF4C4B4B),
                    unfocusedPlaceholderColor = Color(0xFF4C4B4B),
                    focusedLabelColor = Color(0xFF4C4B4B),
                    unfocusedLabelColor = Color(0xFF4C4B4B),
                    focusedTextColor = Color(0xFF000000),
                    unfocusedTextColor = Color(0xFF000000)
                )
            )

            Spacer(Modifier.height(20.dp))

            Text(
                text = "Descrição *",
                color = HoppiOrange,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.align(Alignment.Start)
            )
            OutlinedTextField(
                value = newDescription,
                onValueChange = { newDescription = it },
                label = { Text("Descrição", color = GrayColor) },
                minLines = 3,
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = HoppiOrange,
                    unfocusedBorderColor = Color.Black.copy(alpha = 0.5f),
                    cursorColor = HoppiOrange,
                    focusedPlaceholderColor = Color(0xFF4C4B4B),
                    unfocusedPlaceholderColor = Color(0xFF4C4B4B),
                    focusedLabelColor = Color(0xFF4C4B4B),
                    unfocusedLabelColor = Color(0xFF4C4B4B),
                    focusedTextColor = Color(0xFF000000),
                    unfocusedTextColor = Color(0xFF000000)
                )
            )

            Spacer(Modifier.height(20.dp))

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
                                "Público" -> Icon(Icons.Default.Public, contentDescription = "Public Icon", tint = Color(0xFF000000))
                                "Privado" -> Icon(Icons.Default.Lock, contentDescription = "Private Icon", tint = Color(0xFF000000))
                            }

                            Spacer(modifier = Modifier.width(10.dp))

                            Text(
                                text = selectedPrivacyOption,
                                color = Color(0xFF000000),
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
                                tint = Color(0xFF000000)
                            )
                            Spacer(modifier = Modifier.width(0.dp))
                        }
                    }

                    Divider(
                        color = Color(0xFF000000).copy(alpha = 0.5f),
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
                                Icon(Icons.Default.Public, contentDescription = "Public Icon", tint = Color(0xFF000000))
                                Spacer(Modifier.width(8.dp))
                                Text("Público", color = Color(0xFF000000))
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
                                Icon(Icons.Default.Lock, contentDescription = "Private Icon", tint = Color(0xFF000000))
                                Spacer(Modifier.width(8.dp))
                                Text("Privado", color = Color(0xFF000000))
                            }
                        },
                        onClick = {
                            selectedPrivacyOption = "Privado"
                            isPrivacyExpanded = false
                        }
                    )
                }
            }

            Spacer(Modifier.height(30.dp))

            Button(
                onClick = {
                    val updatedCommunity = community.value!!.copy(
                        name = newName,
                        description = newDescription,
                        isPrivate = selectedPrivacyOption == "Privado"
                    )
                    AppCommunityManager.allCommunities.removeAll { it.id == community.value!!.id }
                    AppCommunityManager.allCommunities.add(updatedCommunity)
                    AppCommunityManager.userCreatedCommunities.removeAll { it.id == community.value!!.id }
                    AppCommunityManager.userCreatedCommunities.add(updatedCommunity)

                    navController.popBackStack()
                },
                enabled = newName.isNotBlank(),
                modifier = Modifier
                    .width(140.dp)
                    .padding(bottom = 20.dp),
                colors = ButtonDefaults.buttonColors(containerColor = HoppiOrange),
                shape = CircleShape
            ) {
                Text("Salvar", color = Color.White, fontSize = 18.sp, textAlign = TextAlign.Center)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditCommunityTopBar(navController: NavController) {
    TopAppBar(
        windowInsets = WindowInsets(0.dp),
        title = {
            Text(
                text = "Editar Comunidade",
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