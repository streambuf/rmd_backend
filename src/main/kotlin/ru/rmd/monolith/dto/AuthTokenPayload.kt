package ru.rmd.monolith.dto

data class AuthTokenPayload(
        val login: String,
        val privileges: Set<String>
)