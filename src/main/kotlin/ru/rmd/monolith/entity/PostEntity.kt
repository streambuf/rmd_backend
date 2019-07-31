package ru.rmd.monolith.entity

import com.fasterxml.jackson.annotation.JsonFormat
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import ru.rmd.monolith.utils.Constants
import ru.rmd.monolith.utils.Constants.DATE_FORMAT
import java.util.*

@Document(collection = "posts")
data class PostEntity(
        @Id
        var id: String?,
        var message: Map<String, Any>,
        var datingService: String,
        var datingServiceProfileLink: String?,
        var city: String,
        var name: String,
        var age: Int,
        var gender: String,
        var image: String?,
        var author: String,
        var slug: String,
        var description: String?,

        @JsonFormat(pattern= DATE_FORMAT)
        var createdAt: Date,

        @JsonFormat(pattern= DATE_FORMAT)
        var updatedAt: Date?,
        var rating: Int?,
        var comments: Int?
) {
}