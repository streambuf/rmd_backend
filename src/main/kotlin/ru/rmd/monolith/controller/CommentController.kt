package ru.rmd.monolith.controller

import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import ru.rmd.monolith.dto.AuthorityPrincipal
import ru.rmd.monolith.dto.request.PersistCommentRequest
import ru.rmd.monolith.entity.CommentEntity
import ru.rmd.monolith.service.CommentService

@RestController
@RequestMapping("/api/v1/comments")
class CommentController(
        private val commentService: CommentService
) {

    @PostMapping(value = [""])
    fun create(@RequestBody request: PersistCommentRequest, @AuthenticationPrincipal principal: AuthorityPrincipal): Mono<CommentEntity> {
        return commentService.create(request, principal)
    }

    @GetMapping(value = ["/{postId}"])
    fun getOne(@PathVariable("postId") postId: String): Flux<CommentEntity> {
        return commentService.getByPostId(postId)
    }
}