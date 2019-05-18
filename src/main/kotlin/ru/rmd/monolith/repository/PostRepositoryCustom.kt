package ru.rmd.monolith.repository

import com.mongodb.client.result.UpdateResult
import reactor.core.publisher.Mono
import ru.rmd.monolith.dto.PersistPostRequest

interface PostRepositoryCustom {
    fun update(id: String, request: PersistPostRequest): Mono<UpdateResult>
}