package ru.rmd.monolith.dto

data class UserInfo(
        val login: String,
        val email: String?,
        val privileges: Set<String>,
        val authToken: String?
)