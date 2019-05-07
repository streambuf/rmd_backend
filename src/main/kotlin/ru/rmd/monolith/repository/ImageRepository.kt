package ru.rmd.monolith.repository

import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.stereotype.Repository
import ru.rmd.monolith.entity.ImageEntity

@Repository
interface ImageRepository : ReactiveMongoRepository<ImageEntity, String> {
//    override fun findAll(): Flux<PostEntity>
}