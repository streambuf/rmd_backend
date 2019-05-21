package ru.rmd.monolith.service

import org.springframework.security.core.Authentication
import reactor.core.publisher.Mono
import ru.rmd.monolith.dto.UserInfoDTO
import ru.rmd.monolith.dto.request.RegisterUserRequest
import ru.rmd.monolith.entity.UserEntity

interface UserService {
    fun getCurrentUser(): Mono<Authentication>
    fun registerUser(request: RegisterUserRequest): Mono<UserInfoDTO>
    fun createFakeUser(login: String): Mono<UserEntity>
}