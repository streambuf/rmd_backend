package ru.rmd.monolith.service

import reactor.core.publisher.Mono
import ru.rmd.monolith.dto.AuthorityPrincipal
import ru.rmd.monolith.dto.UserInfo
import ru.rmd.monolith.dto.request.LoginUserRequest
import ru.rmd.monolith.dto.request.RegisterUserRequest
import ru.rmd.monolith.entity.UserEntity

interface UserService {
    fun getCurrentUser(principal: AuthorityPrincipal): Mono<AuthorityPrincipal>
    fun registerUser(request: RegisterUserRequest): Mono<UserInfo>
    fun loginUser(request: LoginUserRequest): Mono<UserInfo>
    fun createFakeUser(login: String): Mono<UserEntity>
}