package ru.rmd.monolith.entity

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "images")
class ImageEntity {

    @Id
    var id: String? = null

    var data: ByteArray? = null

    constructor(data: ByteArray?) {
        this.data = data
    }
}