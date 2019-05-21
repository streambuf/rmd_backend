package ru.rmd.monolith.dto.request

data class RegisterUserRequest(
        val login: String,
        val email: String,
        val password: String
)