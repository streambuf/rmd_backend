package ru.rmd.monolith.service

import reactor.core.publisher.Mono
import ru.rmd.monolith.dto.AuthorityPrincipal
import ru.rmd.monolith.dto.request.VoteRequest
import ru.rmd.monolith.entity.PostEntity

interface PostRatingService {
    fun updatePostRating(request: VoteRequest, principal: AuthorityPrincipal): Mono<PostEntity>
}