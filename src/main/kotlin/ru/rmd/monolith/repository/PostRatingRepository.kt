package ru.rmd.monolith.repository

import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import reactor.core.publisher.Mono
import ru.rmd.monolith.entity.PostRatingEntity

interface PostRatingRepository : ReactiveMongoRepository<PostRatingEntity, String> {
    fun findByPostIdAndUserId(postId: String, userId: String): Mono<PostRatingEntity>
}