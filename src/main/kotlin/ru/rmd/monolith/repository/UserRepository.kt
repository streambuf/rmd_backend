package ru.rmd.monolith.repository

import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.stereotype.Repository
import ru.rmd.monolith.entity.UserEntity

@Repository
interface UserRepository : ReactiveMongoRepository<UserEntity, String> {
}