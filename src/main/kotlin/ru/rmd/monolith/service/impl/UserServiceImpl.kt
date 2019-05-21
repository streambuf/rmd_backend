package ru.rmd.monolith.service.impl

import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import reactor.core.publisher.switchIfEmpty
import ru.rmd.monolith.configuration.AppProperties
import ru.rmd.monolith.dto.AuthTokenPayload
import ru.rmd.monolith.dto.UserInfoDTO
import ru.rmd.monolith.dto.request.RegisterUserRequest
import ru.rmd.monolith.entity.UserEntity
import ru.rmd.monolith.exception.UserException
import ru.rmd.monolith.repository.UserRepository
import ru.rmd.monolith.service.UserService
import ru.rmd.monolith.utils.HashGenerator
import ru.rmd.monolith.utils.JwtUtils

@Service
class UserServiceImpl(
   private val userRepository: UserRepository,
   private val props: AppProperties
) : UserService {

    override fun getCurrentUser(): Mono<Authentication> {
        return Mono.justOrEmpty(SecurityContextHolder.getContext().getAuthentication())
    }

    override fun registerUser(request: RegisterUserRequest): Mono<UserInfoDTO> {
        val user = convertRegisterUserRequestToUserEntity(request)
        user.authToken = JwtUtils.generateJwt(AuthTokenPayload(request.login, emptySet()), props.jwtAuthSigningKey)
        return userRepository.insert(user).map { convertUserEntityToUserInfoDTO(it) }
    }

    override fun createFakeUser(login: String): Mono<UserEntity> {
        return userRepository.findById(login).flatMap {
            if (it.isFake) {
                Mono.just(it)
            } else {
                Mono.error(UserException("Already exists not fake user with login: $login"))
            }
        }.switchIfEmpty {
            val user = UserEntity().apply {
                id = login
                isFake = true
            }
            userRepository.insert(user)
        }
    }

    private fun convertRegisterUserRequestToUserEntity(request: RegisterUserRequest) = UserEntity()
            .apply {
                id = request.login
                email = request.email
                password = HashGenerator.generateHash(request.password, request.login)
            }

    private fun convertUserEntityToUserInfoDTO(user: UserEntity) = UserInfoDTO(
            login = user.id!!,
            email = user.email,
            privileges = user.privileges ?: emptySet(),
            authToken = user.authToken
    )

}