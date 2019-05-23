package ru.rmd.monolith.service.impl

import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import ru.rmd.monolith.dto.AuthorityPrincipal
import ru.rmd.monolith.dto.Privilege
import ru.rmd.monolith.dto.request.PersistPostRequest
import ru.rmd.monolith.entity.PostEntity
import ru.rmd.monolith.exception.BadRequestException
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

    override fun create(request: PersistPostRequest): Mono<PostEntity> {
        return userService.createFakeUser(request.system!!.author!!)
                .then(postRepository.insert(convertRequestToPostEntity(request = request)))
    }

    override fun update(id: String, request: PersistPostRequest, principal: AuthorityPrincipal) = getOne(id)
            .flatMap {
                if (principal.privileges.contains(Privilege.ADMIN) || it.author == principal.login) {
                    Mono.just(it)
                } else {
                    Mono.error<RuntimeException>(PermissionException("Update post denied"))
                }
            }.then(postRepositoryCustom.update(id, request))
            .then(getOne(id))

    private fun convertRequestToPostEntity(request: PersistPostRequest, author: String? = null) = PostEntity(
            id = null,
            message = request.message,
            datingService = request.datingService,
            datingServiceProfileLink = request.datingServiceProfileLink,
            city = request.city,
            name = request.name,
            age = request.age,
            gender = request.gender,
            image = request.image,
            author = request.system?.author ?: author ?: throw BadRequestException("Empty author"),
            createdAt = Date(),
            updatedAt = null
    )

}