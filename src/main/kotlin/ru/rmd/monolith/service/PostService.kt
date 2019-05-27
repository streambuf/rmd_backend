package ru.rmd.monolith.service

import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import ru.rmd.monolith.dto.AuthorityPrincipal
import ru.rmd.monolith.dto.request.PersistPostRequest
import ru.rmd.monolith.entity.PostEntity

interface PostService {
    fun getOne(id: String): Mono<PostEntity>
    fun getList(size: Int?, page: Int?): Flux<PostEntity>
    fun create(request: PersistPostRequest, principal: AuthorityPrincipal): Mono<PostEntity>
    fun update(id: String, request: PersistPostRequest, principal: AuthorityPrincipal): Mono<PostEntity>
}