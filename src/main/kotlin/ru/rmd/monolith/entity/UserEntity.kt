package ru.rmd.monolith.entity

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "users")
class UserEntity() {

    @Id
    var id: String? = null

    var isFake: Boolean = false

}
