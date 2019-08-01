package ru.rmd.monolith.service.impl

import mu.KotlinLogging
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import reactor.core.publisher.SynchronousSink
import reactor.core.publisher.switchIfEmpty
import ru.rmd.monolith.dto.AuthorityPrincipal
import ru.rmd.monolith.dto.request.VoteRequest
import ru.rmd.monolith.entity.PostEntity
import ru.rmd.monolith.entity.PostRatingEntity
import ru.rmd.monolith.exception.BadRequestException
import ru.rmd.monolith.repository.PostRatingRepository
import ru.rmd.monolith.service.PostRatingService
import ru.rmd.monolith.service.PostService
import kotlin.math.abs

@Service
class PostRatingServiceImpl(
        private val postService: PostService,
        private val repository: PostRatingRepository
) : PostRatingService {

    private val logger = KotlinLogging.logger {}

    override fun updatePostRating(request: VoteRequest, principal: AuthorityPrincipal): Mono<PostEntity> {
        validateVoteRequest(request)
        return repository.findByPostIdAndUserId(request.postId, principal.login)
                .switchIfEmpty {
                    val newPostRating = PostRatingEntity(
                            id = null,
                            postId = request.postId,
                            rating = request.rating,
                            userId = principal.login
                    )
                    Mono.just(newPostRating)
                }
                .handle { postRating, sink: SynchronousSink<PostRatingEntity> ->
                    when {
                        postRating.id == null || principal.isAdmin() -> sink.next(postRating)
                        postRating.rating != request.rating -> {
                            postRating.rating = postRating.rating + request.rating;
                            sink.next(postRating)
                        }
                        else -> sink.error(BadRequestException("User ${principal.login} already voted"))
                    }
                }
                .flatMap { repository.save(it) }
                .flatMap { postService.updateRating(it.postId, request.rating) }
    }

    private fun validateVoteRequest(request: VoteRequest) {
        if (abs(request.rating) != 1) {
            throw BadRequestException("Invalid rating value")
        }
    }

}