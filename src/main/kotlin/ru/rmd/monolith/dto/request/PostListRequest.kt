package ru.rmd.monolith.dto.request

import ru.rmd.monolith.dto.RangeDate

data class PostListRequest(
        val size: Int?,
        val page: Int?,
        val city: String?,
        val ageMin: Int?,
        val ageMax: Int?,
        val gender: String?,
        val services: List<String>?,
        val order: String?,
        val rangeDate: RangeDate?
)