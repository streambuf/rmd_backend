package ru.rmd.monolith.entity

import com.fasterxml.jackson.annotation.JsonFormat
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import ru.rmd.monolith.utils.Constants.DATE_FORMAT
import java.util.*

@Document(collection = "comments")
data class CommentEntity(
        @Id
        var id: String?,
        var message: Map<String, Any>,
        var postId: String,
        var author: String,

        @JsonFormat(pattern= DATE_FORMAT)
        var createdAt: Date,

        @JsonFormat(pattern= DATE_FORMAT)
        var updatedAt: Date?,
        var rating: Int
) {
}