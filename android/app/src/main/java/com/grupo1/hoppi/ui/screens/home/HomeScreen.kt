package com.grupo1.hoppi.ui.screens.home

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.grupo1.hoppi.dataStore
import com.grupo1.hoppi.ui.screens.mainapp.FeedScreen

object MainAppDestinations {
    const val FEED_ROUTE = "main/feed"
    const val COMMUNITY_ROUTE = "main/communities"
    const val CREATE_COMMUNITY_ROUTE = "main/create_community"
    const val COMMUNITY_DETAIL_ROUTE = "main/community_detail/{communityId}"
    const val SEARCH_ROUTE = "main/search"
    const val CREATE_POST_ROUTE = "main/create_post"
    const val CREATE_POST_COMMUNITY_ROUTE = "main/create_post_community"
    const val EDIT_COMMUNITY_ROUTE = "main/edit_community"
    const val PROFILE_ROUTE = "main/profile"
    const val NOTIFICATIONS_ROUTE = "main/notifications"
    const val POST_OPEN_ROUTE = "main/post_open/{postId}"
}

@Composable
fun HomeScreen(
    bottomNavController: NavHostController,
    postsViewModel: PostsViewModel,
    userViewModel: UsersViewModel
) {
    val context = LocalContext.current
    val tokenState = userViewModel.token.collectAsState(initial = null)
    val token = tokenState.value

    LaunchedEffect(token) {
        if (!token.isNullOrEmpty()) {
            postsViewModel.loadFeed(token)
        }
    }

    Scaffold() { paddingValues ->
        FeedScreen(
            postsViewModel = postsViewModel,
            modifier = Modifier.padding(paddingValues),
            userViewModel = userViewModel,
            onPostClick = { postId -> bottomNavController.navigate("main/post_open/$postId") },
            onNotificationsClick = { bottomNavController.navigate(MainAppDestinations.NOTIFICATIONS_ROUTE) },
            onProfileClick = { userId ->
                bottomNavController.navigate("main/profile/$userId")
            },
            token = token ?: ""
        )
    }
}