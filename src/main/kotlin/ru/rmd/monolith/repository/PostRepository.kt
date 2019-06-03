package ru.rmd.monolith.repository

import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono
import ru.rmd.monolith.entity.PostEntity

@Repository
interface PostRepository : ReactiveMongoRepository<PostEntity, String> {
    fun countBySlug(slug: String): Mono<Long>
    fun findBySlug(slug: String): Mono<PostEntity>
}