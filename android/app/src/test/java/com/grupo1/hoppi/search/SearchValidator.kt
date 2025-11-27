package com.grupo1.hoppi.search

object SearchValidator {
    fun isValidSearch(query: String): Boolean {
        return query.isNotBlank() && query.length >= 2
    }

    fun filterItems(items: List<String>, query: String): List<String> {
        return if (isValidSearch(query)) {
            items.filter { it.contains(query, ignoreCase = true) }
        } else {
            emptyList()
        }
    }
}