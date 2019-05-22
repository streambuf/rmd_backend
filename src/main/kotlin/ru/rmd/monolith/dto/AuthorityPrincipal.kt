package ru.rmd.monolith.dto

data class AuthorityPrincipal(
        val login: String,
        val privileges: Set<String>
)