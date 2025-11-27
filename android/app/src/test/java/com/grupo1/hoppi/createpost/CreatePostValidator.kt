package com.grupo1.hoppi.createpost

import com.grupo1.hoppi.ui.screens.mainapp.TagItem

object CreatePostValidator {
    fun validatePostText(text: String): Boolean {
        return text.isNotBlank() && text.length <= 300
    }

    fun validateTag(tag: TagItem?): Boolean {
        return tag != null
    }

    fun canPublish(text: String, tag: TagItem?): Boolean {
        return validatePostText(text) && validateTag(tag)
    }
}