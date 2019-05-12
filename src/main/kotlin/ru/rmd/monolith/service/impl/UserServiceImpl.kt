package ru.rmd.monolith.service.impl

import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import reactor.core.publisher.switchIfEmpty
import ru.rmd.monolith.entity.UserEntity
import ru.rmd.monolith.exception.UserException
import ru.rmd.monolith.repository.UserRepository
import ru.rmd.monolith.service.UserService

@Service
class UserServiceImpl(
   private val userRepository: UserRepository
) : UserService {


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
}