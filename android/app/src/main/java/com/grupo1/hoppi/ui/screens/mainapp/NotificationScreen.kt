package com.grupo1.hoppi.ui.screens.mainapp

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.compose.ui.platform.LocalView

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
    NotificationItem(
        1,
        "pediu para seguir sua comunidade",
        "2 dias",
        NotificationType.FOLLOW_REQUEST,
        true
    ),
    NotificationItem(
        2,
        "Comunidade de Estudantes aceitou sua solicitação",
        "1 sem",
        NotificationType.ACCEPTED
    ),
    NotificationItem(3, "Curtiu seu post.", "1 sem", NotificationType.LIKE),
    NotificationItem(4, "Curtiu seu post.", "2 sem", NotificationType.LIKE),
    NotificationItem(5, "Curtiu seu post.", "2 sem", NotificationType.LIKE),
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationScreen(
    navController: NavController,
    viewModel: NotificationsViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val view = LocalView.current

    Scaffold(
        topBar = {
            NotificationTopBar(navController = navController)
        },
        contentWindowInsets = WindowInsets(0,0,0,0),
        containerColor = Color.White
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color.White)
        ) {
            items(viewModel.notifications) { item ->
                NotificationRow(
                    item = item,
                    onAccept = { viewModel.acceptRequest(item.id) },
                    onDeny = { viewModel.denyRequest(item.id) }
                )
                Divider(color = Color(0xFF9CBDC6).copy(alpha = 0.5f), thickness = 1.dp)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationTopBar(navController: NavController) {
    TopAppBar(
        windowInsets = WindowInsets(0.dp),
        title = {
            Text(
                text = "Notificações",
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
fun NotificationRow(
    item: NotificationItem,
    onAccept: () -> Unit = {},
    onDeny: () -> Unit = {}
    ) {
    val avatarColor = NotificationColors[item.type] ?: Color(0xFF9CBDC6)
    val checkColor = Color(0xFF9CBDC6)
    val closeColor = Color(0xFFD9D9D9)

    val isFollowRequest = item.type == NotificationType.FOLLOW_REQUEST
    val isAccepted = item.type == NotificationType.ACCEPTED

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 20.dp)
            .background(Color.White),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Row(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Fulano de Tal",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF000000)
                )

                if (!isFollowRequest && !isAccepted) {
                    Text(
                        text = " ${item.text}",
                        fontSize = 14.sp,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color(0xFF000000)
                    )
                }
            }

            if (isFollowRequest || isAccepted) {
                Text(
                    text = item.text,
                    fontSize = 14.sp,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(top = 2.dp),
                    color = Color(0xFF000000)
                )
            }
            Spacer(Modifier.height(4.dp))
            Text(
                text = item.timeAgo,
                color = Color(0xFFA6A6A6),
                fontSize = 14.sp,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            when (item.type) {
                NotificationType.FOLLOW_REQUEST -> {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(25.dp)
                            .clip(CircleShape)
                            .background(checkColor)
                            .clickable { onAccept() }
                    ) {
                        Icon(
                            Icons.Default.Check,
                            contentDescription = "Aceitar",
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                    Spacer(Modifier.width(10.dp))

                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(25.dp)
                            .clip(CircleShape)
                            .background(closeColor)
                            .clickable { onDeny() }
                    ) {
                        Icon(
                            Icons.Default.Close,
                            contentDescription = "Recusar",
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }

                else -> {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFA4485F))
                            .padding(start = 4.dp)
                    )
                }
            }
        }
    }
}