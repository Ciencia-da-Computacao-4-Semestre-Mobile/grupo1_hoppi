package com.grupo1.hoppi.ui.screens.mainapp

import com.grupo1.hoppi.ui.screens.home.Post
import org.junit.Assert.*
import org.junit.Test

class FeedValidatorTest {

    @Test
    fun testHasPosts() {
        val posts = listOf(
            Post(
                id = 1,
                username = "UserA",
                handle = "@usera",
                content = "Post A"
            ),
            Post(
                id = 2,
                username = "UserB",
                handle = "@userb",
                content = "Post B"
            )
        )
        assertTrue(FeedValidator.hasPosts(posts))
        assertFalse(FeedValidator.hasPosts(emptyList()))
    }

    @Test
    fun testIsPostLiked() {
        val post = Post(
            id = 1,
            username = "UserA",
            handle = "@usera",
            content = "Post A",
            isLiked = true
        )
        assertTrue(FeedValidator.isPostLiked(post))
    }

    @Test
    fun testToggleLike() {
        val post = Post(
            id = 1,
            username = "UserA",
            handle = "@usera",
            content = "Post A",
            likes = 3,
            isLiked = false
        )

        val liked = FeedValidator.toggleLike(post)
        assertTrue(liked.isLiked)
        assertEquals(4, liked.likes)

        val unliked = FeedValidator.toggleLike(liked)
        assertFalse(unliked.isLiked)
        assertEquals(3, unliked.likes)
    }
}
