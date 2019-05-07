package ru.rmd.monolith.entity

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "posts")
class PostEntity() {

    @Id
    var id: String? = null

    var message: String? = null

    var user: String? = null
}