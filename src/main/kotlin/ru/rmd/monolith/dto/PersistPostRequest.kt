package ru.rmd.monolith.dto

class PersistPostRequest(
    var message: Map<String, Any>,
    var datingService: String,
    var datingServiceProfileLink: String?,
    var city: String,
    var name: String,
    var age: Int,
    var gender: String,
    var image: String?,
    var system: PersistPostRequestMeta?
)

class PersistPostRequestMeta {
    var author: String? = null
}