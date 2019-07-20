package ru.rmd.monolith.entity

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "post_rating")
data class PostRatingEntity(
        @Id
        var id: String?,
        var postId: String,
        var userId: String,
        var rating: Int
) {
}