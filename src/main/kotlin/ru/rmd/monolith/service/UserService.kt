package ru.rmd.monolith.service

import reactor.core.publisher.Mono
import ru.rmd.monolith.entity.UserEntity

interface UserService {
    fun createFakeUser(login: String): Mono<UserEntity>
}