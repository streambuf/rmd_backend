package ru.rmd.monolith.dto.request

class PersistCommentRequest(
    var message: Map<String, Any>,
    var postId: String,
    val fakeAuthor: String?
)
