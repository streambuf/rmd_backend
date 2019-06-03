package ru.rmd.monolith.service.impl

import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import org.springframework.util.StringUtils
import reactor.core.publisher.Flux
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
import ru.rmd.monolith.utils.TranslitUtils
import java.util.*

@Service
class PostServiceImpl(
        private val postRepository: PostRepository,
        private val postRepositoryCustom: PostRepositoryCustom,
        private val userService: UserService
) : PostService {

    override fun getOne(slug: String) = postRepository.findBySlug(slug)
            .switchIfEmpty(Mono.error(NotFoundException("Not found post with slug: $slug")))

    override fun getList(size: Int?, page: Int?): Flux<PostEntity> {
        val pageRequest = PageRequest.of(page ?: 0, size ?: 30, Sort.Direction.DESC, "createdAt")
        return postRepositoryCustom.find(pageRequest)
    }

    override fun create(request: PersistPostRequest, principal: AuthorityPrincipal): Mono<PostEntity> {
        val userMono = request.system?.author
                ?.takeIf { !StringUtils.isEmpty(it) }
                ?.let { userService.createFakeUser(it) }
                ?.map { it.id }
                ?: Mono.just(principal.login)

        var slug = createSlug(request.datingService, request.name, request.age, request.city)

        val slugMono = postRepository.countBySlug(slug).map { count ->
            if (count > 0) {
                slug = "${slug}_$count"
            }
            slug
        }

        return Mono.zip(userMono, slugMono).flatMap { postRepository.insert(convertRequestToPostEntity(request, it.t1!!, it.t2!!)) }
    }

    override fun update(slug: String, request: PersistPostRequest, principal: AuthorityPrincipal) = getOne(slug)
            .flatMap {
                if (principal.isAdmin() || it.author == principal.login) {
                    Mono.just(it)
                } else {
                    Mono.error<RuntimeException>(PermissionException("Update post denied"))
                }
            }.then(postRepositoryCustom.update(slug, request))
            .then(getOne(slug))

    private fun convertRequestToPostEntity(request: PersistPostRequest, author: String, slug: String) = PostEntity(
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
            updatedAt = null,
            slug = slug
    )

    private fun createSlug(datingService: String, name: String, age: Int, city: String) =
            "отзыв с сайта знакомств $datingService $name $age город $city"
                    .replace(" ", "-")
                    .replace(".", "-")
                    .let { TranslitUtils.translit(it) }

}