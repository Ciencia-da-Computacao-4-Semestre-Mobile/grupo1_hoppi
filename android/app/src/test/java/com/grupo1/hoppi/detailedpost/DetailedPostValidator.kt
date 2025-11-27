package com.grupo1.hoppi.detailedpost

import com.grupo1.hoppi.ui.screens.mainapp.Comment
import com.grupo1.hoppi.ui.screens.mainapp.DetailedPost

object DetailedPostValidator {
    fun isValidPost(post: DetailedPost?): Boolean {
        if (post == null) return false
        if (post.username.isBlank()) return false
        if (post.userHandle.isBlank()) return false
        if (post.content.isBlank()) return false
        if (post.likes.isBlank()) return false
        if (post.commentsCount.isBlank()) return false
        if (post.shares.isBlank()) return false
        return true
    }

    fun isValidTag(tag: String?): Boolean {
        return when (tag) {
            "Estudo", "Venda", "Info" -> true
            else -> false
        }
    }

    fun isValidComment(comment: Comment?): Boolean {
        if (comment == null) return false
        if (comment.username.isBlank()) return false
        if (comment.userHandle.isBlank()) return false
        if (comment.content.isBlank()) return false
        return true
    }
}