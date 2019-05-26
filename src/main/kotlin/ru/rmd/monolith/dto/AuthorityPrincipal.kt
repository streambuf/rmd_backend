package ru.rmd.monolith.dto

import com.fasterxml.jackson.annotation.JsonIgnore
import java.util.*

data class AuthorityPrincipal(
        val login: String,
        val privileges: EnumSet<Privilege>
) {

    @JsonIgnore
    fun isAdmin() = privileges.contains(Privilege.ADMIN)
}