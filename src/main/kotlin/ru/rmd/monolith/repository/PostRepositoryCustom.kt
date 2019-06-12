package ru.rmd.monolith.repository

import com.mongodb.client.result.UpdateResult
import org.springframework.data.domain.PageRequest
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import ru.rmd.monolith.dto.request.PersistPostRequest
import ru.rmd.monolith.dto.request.PostListSearchRequest
import ru.rmd.monolith.entity.PostEntity

interface PostRepositoryCustom {
    fun update(id: String, request: PersistPostRequest): Mono<UpdateResult>
    fun find(req: PostListSearchRequest): Flux<PostEntity>
    fun findAll(): Flux<PostEntity>
}