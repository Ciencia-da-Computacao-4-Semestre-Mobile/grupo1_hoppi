package com.grupo1.hoppi.ui.screens.mainapp

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

data class NotificationItem(
    val id: Int,
    val text: String,
    val timeAgo: String,
    val type: NotificationType,
    val hasAction: Boolean = false
)

enum class NotificationType {
    FOLLOW_REQUEST,
    ACCEPTED,
    LIKE,
    COMMENT,
    NEW_FOLLOWER
}

val mockNotifications = listOf(
    NotificationItem(1, "pediu para seguir sua comunidade", "2 dias", NotificationType.FOLLOW_REQUEST, true),
    NotificationItem(2, "Comunidade de Estudantes aceitou sua solicitação", "1 sem", NotificationType.ACCEPTED),
    NotificationItem(3, "Curtiu seu post.", "1 sem", NotificationType.LIKE),
    NotificationItem(4, "Curtiu seu post.", "2 sem", NotificationType.LIKE),
    NotificationItem(5, "Curtiu seu post.", "2 sem", NotificationType.LIKE),
    NotificationItem(6, "Curtiu seu post.", "3 sem", NotificationType.LIKE),
    NotificationItem(7, "Curtiu seu post.", "3 sem", NotificationType.LIKE),
    NotificationItem(8, "Comentou no seu post.", "3 sem", NotificationType.COMMENT),
    NotificationItem(9, "Começou a seguir você.", "3 sem", NotificationType.NEW_FOLLOWER),
)

val NotificationColors = mapOf(
    NotificationType.FOLLOW_REQUEST to Color(0xFF406B8D),
    NotificationType.ACCEPTED to Color(0xFF9CBDC6),
    NotificationType.LIKE to Color(0xFFA4485F),
    NotificationType.COMMENT to Color(0xFFB56576),
    NotificationType.NEW_FOLLOWER to Color(0xFFB56576),
)

@Composable
fun NotificationScreen(navController: NavController) {
    Scaffold(
        topBar = { NotificationTopBar(navController) }
    ) { paddingValues ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color.White)
        ) {
            items(mockNotifications) { item ->
                NotificationRow(item = item)
                Divider(color = Color.LightGray.copy(alpha = 0.5f), thickness = 1.dp)
            }

            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp)
                        .clickable { /* Lógica de carregar mais */ },
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        "Mostrar mais",
                        color = Color.Gray,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationTopBar(navController: NavController) {
    TopAppBar(
        title = { Text("Notificações") },
        navigationIcon = {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(Icons.Filled.ArrowBack, contentDescription = "Voltar")
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
fun NotificationRow(item: NotificationItem) {
    val avatarColor = NotificationColors[item.type] ?: Color.Gray

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 10.dp)
            .background(Color.White),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(avatarColor)
        )

        Spacer(Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Row {
                Text(
                    text = "Fulano de Tal",
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.bodyMedium,
                )
                Text(
                    text = " ${item.text}",
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
            Text(
                text = item.timeAgo,
                color = Color.Gray,
                style = MaterialTheme.typography.bodySmall
            )
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            when (item.type) {
                NotificationType.FOLLOW_REQUEST -> {
                    IconButton(onClick = { /* Aceitar */ }) {
                        Icon(Icons.Default.Check, contentDescription = "Aceitar", tint = Color.Gray)
                    }
                    Spacer(Modifier.width(4.dp))
                    IconButton(onClick = { /* Recusar */ }) {
                        Icon(Icons.Default.Close, contentDescription = "Recusar", tint = Color.Gray)
                    }
                }
                else -> {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(Color.Red)
                            .padding(start = 4.dp)
                    )
                }
            }
        }
    }
}