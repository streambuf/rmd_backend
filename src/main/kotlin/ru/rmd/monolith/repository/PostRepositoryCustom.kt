package ru.rmd.monolith.repository

import com.mongodb.client.result.UpdateResult
import reactor.core.publisher.Mono
import ru.rmd.monolith.dto.request.PersistPostRequest

interface PostRepositoryCustom {
    fun update(id: String, request: PersistPostRequest): Mono<UpdateResult>
}