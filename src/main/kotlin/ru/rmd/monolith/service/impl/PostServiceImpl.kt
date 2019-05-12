package ru.rmd.monolith.service.impl

import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import ru.rmd.monolith.dto.PersistPostRequest
import ru.rmd.monolith.entity.PostEntity
import ru.rmd.monolith.exception.BadRequestException
import ru.rmd.monolith.repository.PostRepository
import ru.rmd.monolith.service.PostService
import ru.rmd.monolith.service.UserService

@Service
class PostServiceImpl(
        private val postRepository: PostRepository,
        private val userService: UserService
) : PostService {

    override fun create(request: PersistPostRequest): Mono<PostEntity> {
        return userService.createFakeUser(request.system.author!!)
                .then(postRepository.insert(convertRequestToPostEntity(request = request)))
    }

    override fun getOne(id: String) = postRepository.findById(id)

    override fun getList() = postRepository.findAll()

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
            author = request.system.author ?: author ?: throw BadRequestException("Empty author")
    )

}