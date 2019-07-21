package ru.rmd.monolith.service

import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import ru.rmd.monolith.dto.AuthorityPrincipal
import ru.rmd.monolith.dto.request.PersistPostRequest
import ru.rmd.monolith.dto.request.PostListRequest
import ru.rmd.monolith.entity.PostEntity

interface PostService {
    fun getOneBySlug(slug: String): Mono<PostEntity>
    fun getOneById(id: String): Mono<PostEntity>
    fun getList(request: PostListRequest): Flux<PostEntity>
    fun getAll(): Flux<PostEntity>
    fun create(request: PersistPostRequest, principal: AuthorityPrincipal): Mono<PostEntity>
    fun update(slug: String, request: PersistPostRequest, principal: AuthorityPrincipal): Mono<PostEntity>
    fun updateRating(id: String, rating: Int): Mono<PostEntity>
    fun updateCommentsCount(postId: String, value: Int): Mono<PostEntity>
}