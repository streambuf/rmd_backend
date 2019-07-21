package ru.rmd.monolith.service

import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import ru.rmd.monolith.dto.AuthorityPrincipal
import ru.rmd.monolith.dto.request.PersistCommentRequest
import ru.rmd.monolith.entity.CommentEntity

interface CommentService {
    fun getByPostId(postId: String): Flux<CommentEntity>
    fun create(request: PersistCommentRequest, principal: AuthorityPrincipal): Mono<CommentEntity>
}