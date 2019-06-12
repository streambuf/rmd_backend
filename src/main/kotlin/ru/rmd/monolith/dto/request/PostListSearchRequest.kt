package ru.rmd.monolith.dto.request

import org.springframework.data.domain.PageRequest
import java.util.*

data class PostListSearchRequest(
        val pageRequest: PageRequest,
        val city: String?,
        val ageMin: Int?,
        val ageMax: Int?,
        val gender: String?,
        val services: List<String>?,
        val minDate: Date?
)