package ru.rmd.monolith.dto.request

data class VoteRequest(
        val postId: String,
        val rating: Int
)