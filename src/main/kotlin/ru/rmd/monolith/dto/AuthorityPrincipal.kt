package ru.rmd.monolith.dto

import java.util.*

data class AuthorityPrincipal(
        val login: String,
        val privileges: EnumSet<Privilege>
)