package ru.rmd.monolith.service

import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import ru.rmd.monolith.dto.request.PersistPostRequest
import ru.rmd.monolith.entity.PostEntity

interface PostService {
    fun create(request: PersistPostRequest): Mono<PostEntity>
    fun getOne(id: String): Mono<PostEntity>
    fun getList(): Flux<PostEntity>
    fun update(id: String, request: PersistPostRequest): Mono<PostEntity>
}