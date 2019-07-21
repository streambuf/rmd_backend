package ru.rmd.monolith.repository

import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import ru.rmd.monolith.entity.CommentEntity

@Repository
interface CommentRepository : ReactiveMongoRepository<CommentEntity, String> {
    fun findByPostId(postId: String) : Flux<CommentEntity>
}