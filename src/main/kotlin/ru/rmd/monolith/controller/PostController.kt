package ru.rmd.monolith.controller

import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import ru.rmd.monolith.dto.AuthorityPrincipal
import ru.rmd.monolith.dto.request.PersistPostRequest
import ru.rmd.monolith.entity.PostEntity
import ru.rmd.monolith.service.PostService

@RestController
@RequestMapping("/api/v1/posts")
class PostController(
        private val postService: PostService
) {

    @PostMapping(value = [""])
    fun create(@RequestBody request: PersistPostRequest): Mono<PostEntity> {
        return postService.create(request)
    }

    @GetMapping(value = ["/{id}"])
    fun getOne(@PathVariable("id") id: String): Mono<PostEntity> {
        return postService.getOne(id)
    }

    @PutMapping(value = ["/{id}"])
    fun update(
            @PathVariable("id") id: String,
            @RequestBody request: PersistPostRequest,
            @AuthenticationPrincipal principal: AuthorityPrincipal
    ): Mono<PostEntity> {
        return postService.update(id, request, principal)
    }

    @GetMapping(value = [""])
    fun getList(): Flux<PostEntity> {
        return postService.getList()
    }
}