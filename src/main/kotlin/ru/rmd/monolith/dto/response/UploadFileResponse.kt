package ru.rmd.monolith.dto.response

data class UploadFileResponse(
        val success: Int = 1,
        val file: UploadFile
)

data class UploadFile(
    val url: String
)