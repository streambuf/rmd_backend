package ru.rmd.monolith.service.impl

import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import reactor.core.publisher.switchIfEmpty
import ru.rmd.monolith.configuration.AppProperties
import ru.rmd.monolith.dto.AuthorityPrincipal
import ru.rmd.monolith.dto.UserInfo
import ru.rmd.monolith.dto.request.LoginUserRequest
import ru.rmd.monolith.dto.request.RegisterUserRequest
import ru.rmd.monolith.entity.UserEntity
import ru.rmd.monolith.exception.UserException
import ru.rmd.monolith.repository.UserRepository
import ru.rmd.monolith.service.UserService
import ru.rmd.monolith.utils.HashGenerator
import ru.rmd.monolith.utils.JwtUtils
import java.util.*

@Service
class UserServiceImpl(
        private val userRepository: UserRepository,
        private val props: AppProperties
) : UserService {

    override fun getCurrentUser(principal: AuthorityPrincipal): Mono<AuthorityPrincipal> {
        return Mono.just(principal)
    }

    override fun registerUser(request: RegisterUserRequest): Mono<UserInfo> {
        val user = convertRegisterUserRequestToUserEntity(request)
        user.authToken = JwtUtils.generateJwt(AuthorityPrincipal(request.login, emptySet()), props.jwtAuthSigningKey)
        return userRepository.insert(user).map { convertUserEntityToUserInfoDTO(it) }
    }

    override fun loginUser(request: LoginUserRequest): Mono<UserInfo> {
       return userRepository.findById(request.login).flatMap {
            if (Objects.equals(HashGenerator.generateHash(request.password, request.login), it.password)) {
                Mono.just(convertUserEntityToUserInfoDTO(it))
            } else {
                Mono.error(UserException("Неправильный логин или пароль"))
            }
        }
    }

    override fun createFakeUser(login: String): Mono<UserEntity> {
        return userRepository.findById(login).flatMap {
            if (it.isFake) {
                Mono.just(it)
            } else {
                Mono.error(UserException("Уже существует реальный пользователь с таким логином: $login"))
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

    private fun convertUserEntityToUserInfoDTO(user: UserEntity) = UserInfo(
            login = user.id!!,
            email = user.email,
            privileges = user.privileges ?: emptySet(),
            authToken = user.authToken
    )

}