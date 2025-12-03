package com.grupo1.hoppi.ui.screens.mainapp

import android.R.attr.onClick
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.platform.testTag
import com.grupo1.hoppi.ui.screens.home.UsersViewModel


@Composable
fun SearchScreen(
    navController: NavController,
    usersViewModel: UsersViewModel
) {
    var searchText by remember { mutableStateOf("") }
    val searchResults by usersViewModel.searchResults.collectAsState()
    val token by usersViewModel.token.collectAsState()
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(token) {
        if (!token.isNullOrEmpty()) {
            usersViewModel.loadAllUsers()
            focusRequester.requestFocus()
        }
    }

    Scaffold(
        topBar = { SearchTopBar(navController) },
        modifier = Modifier
            .fillMaxSize()
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = paddingValues.calculateTopPadding())
                .background(Color.White)
        ) {
            OutlinedTextField(
                value = searchText,
                onValueChange = { text ->
                    searchText = text
                    usersViewModel.searchUsers(text)
                                },
                placeholder = { Text("O que você está buscando?") },
                leadingIcon = { Icon(Icons.Filled.Search, contentDescription = "Busca", tint = Color(0xFFA5A5A5)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 20.dp)
                    .focusRequester(focusRequester)
                    .testTag("SearchTextField"),
                shape = RoundedCornerShape(50.dp),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedContainerColor = Color(0xFFDBDBDB),
                    focusedContainerColor = Color(0xFFDBDBDB),
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent,
                    cursorColor = MaterialTheme.colorScheme.primary,
                    focusedPlaceholderColor = Color(0xFFA5A5A5),
                    unfocusedPlaceholderColor = Color(0xFFA5A5A5),
                    focusedLabelColor = Color(0xFFA5A5A5),
                    unfocusedLabelColor = Color(0xFFA5A5A5),
                    focusedLeadingIconColor = Color(0xFFA5A5A5),
                    unfocusedLeadingIconColor = Color(0xFFA5A5A5),
                    focusedTextColor = Color(0xFF000000),
                    unfocusedTextColor = Color(0xFF000000)
                )
            )

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(0.dp)
            ) {
                items(searchResults) { user ->
                    SearchItemRow(
                        userId = user.id,
                        username = user.username,
                        displayName = user.displayName,
                        onClick = { selectedUserId ->
                            navController.navigate("profile/$selectedUserId")
                        }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchTopBar(navController: NavController) {
    TopAppBar(
        windowInsets = WindowInsets(0.dp),
        title = { Text(text = "Pesquisa", style = MaterialTheme.typography.headlineLarge, color = Color.White) },
        navigationIcon = {
            IconButton(onClick = {
                navController.popBackStack()
            }) {
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
fun SearchItemRow(
    userId: String,
    username: String,
    displayName: String,
    onClick: (String) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick(userId) }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 30.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "$displayName (@$username)",
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFF000000)
            )
        }
        Divider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp)
                .drawBehind {
                    drawLine(
                        color = Color(0xFFBDBDBD),
                        start = Offset(0f, 0f),
                        end = Offset(size.width, 0f),
                        strokeWidth = 1.dp.toPx(),
                        pathEffect = PathEffect.dashPathEffect(floatArrayOf(5f, 10f), 0f)
                    )
                },
            color = Color.Transparent,
            thickness = 1.dp
        )
    }
}