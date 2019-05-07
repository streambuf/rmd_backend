package ru.rmd.monolith.repository

import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.stereotype.Repository
import ru.rmd.monolith.entity.PostEntity

@Repository
interface PostRepository : ReactiveMongoRepository<PostEntity, String> {
//    override fun findAll(): Flux<PostEntity>
}