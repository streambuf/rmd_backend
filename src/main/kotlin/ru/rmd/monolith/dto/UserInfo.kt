package ru.rmd.monolith.dto

import java.util.*

data class UserInfo(
        val login: String,
        val email: String?,
        val privileges: EnumSet<Privilege>,
        val authToken: String?
)