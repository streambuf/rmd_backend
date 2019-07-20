package ru.rmd.monolith.service.impl

import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import org.springframework.util.StringUtils
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import ru.rmd.monolith.dto.AuthorityPrincipal
import ru.rmd.monolith.dto.RangeDate
import ru.rmd.monolith.dto.request.PersistPostRequest
import ru.rmd.monolith.dto.request.PostListRequest
import ru.rmd.monolith.dto.request.PostListSearchRequest
import ru.rmd.monolith.entity.PostEntity
import ru.rmd.monolith.exception.NotFoundException
import ru.rmd.monolith.exception.PermissionException
import ru.rmd.monolith.repository.PostRepository
import ru.rmd.monolith.repository.PostRepositoryCustom
import ru.rmd.monolith.service.PostService
import ru.rmd.monolith.service.UserService
import ru.rmd.monolith.utils.TranslitUtils
import java.time.Duration
import java.time.Instant
import java.util.*

@Service
class PostServiceImpl(
        private val postRepository: PostRepository,
        private val postRepositoryCustom: PostRepositoryCustom,
        private val userService: UserService,
        private val descriptionServiceImpl: DescriptionServiceImpl
) : PostService {

    override fun getOneBySlug(slug: String) = postRepository.findBySlug(slug)
            .switchIfEmpty(Mono.error(NotFoundException("Not found post with slug: $slug")))

    override fun getOneById(id: String) = postRepository.findById(id)
            .switchIfEmpty(Mono.error(NotFoundException("Not found post with id: $id")))

    override fun getList(request: PostListRequest): Flux<PostEntity> {
        val order = request.order ?: "createdAt"
        val pageRequest = PageRequest.of(request.page ?: 0, request.size ?: 30, Sort.Direction.DESC, order)
        val minDate = convertRangeDateToMinDate(request.rangeDate)

        val searchRequest = PostListSearchRequest(
                pageRequest = pageRequest,
                city = request.city,
                ageMin = request.ageMin,
                ageMax = request.ageMax,
                gender = request.gender,
                services = request.services,
                minDate = minDate
        )

        return postRepositoryCustom.find(searchRequest)
    }

    override fun getAll(): Flux<PostEntity> {
        return postRepositoryCustom.findAll()
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

        val description = descriptionServiceImpl.createDescription(request.name, request.datingService, request.city)

        return Mono.zip(userMono, slugMono).flatMap { postRepository.insert(convertRequestToPostEntity(request, it.t1!!, it.t2!!, description)) }
    }

    override fun update(slug: String, request: PersistPostRequest, principal: AuthorityPrincipal) = getOneBySlug(slug)
            .flatMap {
                if (principal.isAdmin() || it.author == principal.login) {
                    Mono.just(it)
                } else {
                    Mono.error<RuntimeException>(PermissionException("Update post denied"))
                }
            }.then(postRepositoryCustom.update(slug, request))
            .then(getOneBySlug(slug))


    override fun updateRating(id: String, rating: Int) = getOneById(id)
            .flatMap {
                val resultRating = rating + (it.rating ?: 0)
                postRepositoryCustom.updateRating(it.id!!, resultRating)
            }.then(getOneById(id))

    private fun convertRangeDateToMinDate(rangeDate: RangeDate?): Date? {
        if (rangeDate == null) {
            return null
        }

        val dayCount = when (rangeDate) {
            RangeDate.DAY -> 1L
            RangeDate.WEEK -> 7L
            RangeDate.MONTH -> 30L
        }

        val now = Instant.now()
        val before = now.minus(Duration.ofDays(dayCount));
        return Date.from(before)
    }

    private fun convertRequestToPostEntity(request: PersistPostRequest, author: String, slug: String, description: String) = PostEntity(
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
            slug = slug,
            description = description,
            rating = 0
    )

    private fun createSlug(datingService: String, name: String, age: Int, city: String) =
            "отзыв с сайта знакомств $datingService $name $age город $city"
                    .replace(" ", "-")
                    .replace(".", "-")
                    .let { TranslitUtils.translit(it) }

}