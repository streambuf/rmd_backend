package ru.rmd.monolith.dto

data class UploadFileResponse(
        val success: Int = 1,
        val file: UploadFile
)

data class UploadFile(
    val url: String
)