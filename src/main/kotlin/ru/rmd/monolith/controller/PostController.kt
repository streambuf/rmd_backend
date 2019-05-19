package ru.rmd.monolith.controller

import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import ru.rmd.monolith.dto.PersistPostRequest
import ru.rmd.monolith.entity.PostEntity
import ru.rmd.monolith.service.PostService

@RestController
@RequestMapping("/api/v1/posts")
@CrossOrigin(origins = ["*"])
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
    fun update(@PathVariable("id") id: String, @RequestBody request: PersistPostRequest): Mono<PostEntity> {
        return postService.update(id, request)
    }

    @GetMapping(value = [""])
    fun getList(): Flux<PostEntity> {
        return postService.getList()
    }
}