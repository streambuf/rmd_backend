package ru.rmd.monolith.service.impl

import org.springframework.stereotype.Service
import org.springframework.util.StringUtils
import reactor.core.publisher.Mono
import ru.rmd.monolith.dto.AuthorityPrincipal
import ru.rmd.monolith.dto.request.PersistPostRequest
import ru.rmd.monolith.entity.PostEntity
import ru.rmd.monolith.exception.NotFoundException
import ru.rmd.monolith.exception.PermissionException
import ru.rmd.monolith.repository.PostRepository
import ru.rmd.monolith.repository.PostRepositoryCustom
import ru.rmd.monolith.service.PostService
import ru.rmd.monolith.service.UserService
import java.util.*

@Service
class PostServiceImpl(
        private val postRepository: PostRepository,
        private val postRepositoryCustom: PostRepositoryCustom,
        private val userService: UserService
) : PostService {

    override fun getOne(id: String) = postRepository.findById(id)
            .switchIfEmpty(Mono.error(NotFoundException("Not found post with id: $id")))

    override fun getList() = postRepository.findAll()

    override fun create(request: PersistPostRequest, principal: AuthorityPrincipal): Mono<PostEntity> {
        val userMono = request.system?.author
                ?.takeIf { !StringUtils.isEmpty(it) }
                ?.let { userService.createFakeUser(it) }
                ?.map { it.id }
                ?: Mono.just(principal.login)

        return userMono.flatMap { author -> postRepository.insert(convertRequestToPostEntity(request, author!!)) }
    }

    override fun update(id: String, request: PersistPostRequest, principal: AuthorityPrincipal) = getOne(id)
            .flatMap {
                if (principal.isAdmin() || it.author == principal.login) {
                    Mono.just(it)
                } else {
                    Mono.error<RuntimeException>(PermissionException("Update post denied"))
                }
            }.then(postRepositoryCustom.update(id, request))
            .then(getOne(id))

    private fun convertRequestToPostEntity(request: PersistPostRequest, author: String) = PostEntity(
            id = null,
            message = request.message,
            datingService = request.datingService,
            datingServiceProfileLink = request.datingServiceProfileLink,
            city = request.city,
            name = request.name,
            age = request.age,
            gender = request.gender,
            image = request.image,
            author = author,
            createdAt = Date(),
            updatedAt = null
    )

}