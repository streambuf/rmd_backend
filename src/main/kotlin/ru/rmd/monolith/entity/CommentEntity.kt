package ru.rmd.monolith.entity

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.util.*

@Document(collection = "comments")
data class CommentEntity(
        @Id
        var id: String?,
        var message: Map<String, Any>,
        var postId: String,
        var author: String,
        var createdAt: Date,
        var updatedAt: Date?,
        var rating: Int
) {
}