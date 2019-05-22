package ru.rmd.monolith.dto.request

data class LoginUserRequest(
        val login: String,
        val password: String
)