package com.grupo1.hoppi.profile

import com.grupo1.hoppi.ui.screens.home.Post
import org.junit.Assert.*
import org.junit.Test

class ProfileValidatorTest {

    @Test
    fun testHasUserPosts() {
        val posts = listOf(
            Post(
                id = 1,
                username = "UserOne",
                handle = "@userone",
                content = "Hello World"
            )
        )
        assertTrue(ProfileValidator.hasUserPosts(posts))
        assertFalse(ProfileValidator.hasUserPosts(emptyList()))
    }

    @Test
    fun testIsPostLiked() {
        val post = Post(
            id = 2,
            username = "UserOne",
            handle = "@userone",
            content = "Test Content",
            isLiked = true
        )
        assertTrue(ProfileValidator.isPostLiked(post))
    }

    @Test
    fun testToggleLike() {
        val post = Post(
            id = 3,
            username = "UserTwo",
            handle = "@usertwo",
            content = "Sample",
            likes = 2,
            isLiked = false
        )

        val liked = ProfileValidator.toggleLike(post)
        assertTrue(liked.isLiked)
        assertEquals(3, liked.likes)

        val unliked = ProfileValidator.toggleLike(liked)
        assertFalse(unliked.isLiked)
        assertEquals(2, unliked.likes)
    }

    @Test
    fun testFilterByUser() {
        val posts = listOf(
            Post(1, "UserOne", "@userone", "Post 1"),
            Post(2, "UserTwo", "@usertwo", "Post 2"),
            Post(3, "UserOne", "@userone", "Post 3")
        )

        val filtered = ProfileValidator.filterByUser(posts, "UserOne")

        assertEquals(2, filtered.size)
        assertTrue(filtered.all { it.username == "UserOne" })
    }
}