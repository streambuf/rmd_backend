package ru.rmd.monolith.controller

import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import ru.rmd.monolith.entity.PostEntity
import ru.rmd.monolith.repository.PostRepository

@RestController
@RequestMapping(value = ["post"])
@CrossOrigin(origins = ["*"])
class PostController(
        private val postRepository: PostRepository
) {

    @PostMapping(value = [""])
    fun create(@RequestBody post: PostEntity): Mono<PostEntity> {
        return postRepository.insert(post);
    }

    @GetMapping(value = [""])
    fun all(): Flux<PostEntity> {
        return postRepository.findAll();
    }

}