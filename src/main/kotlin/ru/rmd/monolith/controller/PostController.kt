package ru.rmd.monolith.controller

import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import ru.rmd.monolith.dto.AuthorityPrincipal
import ru.rmd.monolith.dto.RangeDate
import ru.rmd.monolith.dto.request.PersistPostRequest
import ru.rmd.monolith.dto.request.PostListRequest
import ru.rmd.monolith.dto.request.VoteRequest
import ru.rmd.monolith.entity.PostEntity
import ru.rmd.monolith.service.PostRatingService
import ru.rmd.monolith.service.PostService

@RestController
@RequestMapping("/api/v1/posts")
class PostController(
        private val postService: PostService,
        private val postRatingService: PostRatingService
) {

    @PostMapping(value = [""])
    fun create(@RequestBody request: PersistPostRequest, @AuthenticationPrincipal principal: AuthorityPrincipal): Mono<PostEntity> {
        return postService.create(request, principal)
    }

    @GetMapping(value = ["/{slug}"])
    fun getOne(@PathVariable("slug") slug: String): Mono<PostEntity> {
        return postService.getOneBySlug(slug)
    }

    @PutMapping(value = ["/{slug}"])
    fun update(
            @PathVariable("slug") slug: String,
            @RequestBody request: PersistPostRequest,
            @AuthenticationPrincipal principal: AuthorityPrincipal
    ): Mono<PostEntity> {
        return postService.update(slug, request, principal)
    }

    @PostMapping(value = ["/vote"])
    fun vote(@RequestBody request: VoteRequest, @AuthenticationPrincipal principal: AuthorityPrincipal): Mono<PostEntity> {
        return postRatingService.updatePostRating(request, principal)
    }

    @GetMapping(value = [""])
    fun getList(@RequestParam("size") size: Int?,
                @RequestParam("page") page: Int?,
                @RequestParam("city") city: String?,
                @RequestParam("ageMin") ageMin: Int?,
                @RequestParam("ageMax") ageMax: Int?,
                @RequestParam("gender") gender: String?,
                @RequestParam("services") services: List<String>?,
                @RequestParam("order") order: String?,
                @RequestParam("rangeDate") rangeDate: RangeDate?
    ): Flux<PostEntity> {
        val request = PostListRequest(
                size = size,
                page = page,
                city = city,
                ageMin = ageMin,
                ageMax = ageMax,
                gender = gender,
                services = services,
                order = order,
                rangeDate = rangeDate
        )
        return postService.getList(request)
    }
}