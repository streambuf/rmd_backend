package ru.rmd.monolith.entity

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
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
        var createdAt: Date,
        var updatedAt: Date?,
        var rating: Int?,
        var comments: Int?
) {
}