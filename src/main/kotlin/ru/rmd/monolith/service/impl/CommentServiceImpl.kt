package ru.rmd.monolith.service.impl

import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import ru.rmd.monolith.dto.AuthorityPrincipal
import ru.rmd.monolith.dto.request.PersistCommentRequest
import ru.rmd.monolith.entity.CommentEntity
import ru.rmd.monolith.repository.CommentRepository
import ru.rmd.monolith.service.CommentService
import ru.rmd.monolith.service.PostService
import java.util.*

@Service
class CommentServiceImpl(
        private val postService: PostService,
        private val commentRepository: CommentRepository
) : CommentService {

    override fun getByPostId(postId: String) = commentRepository.findByPostId(postId)

    override fun create(request: PersistCommentRequest, principal: AuthorityPrincipal): Mono<CommentEntity> {
        return commentRepository.save(convertRequestToCommentEntity(request, principal.login))
                .doOnNext { postService.updateCommentsCount(it.postId, 1)  }
    }

    private fun convertRequestToCommentEntity(request: PersistCommentRequest, author: String) = CommentEntity(
            id = null,
            message = request.message,
            postId = request.postId,
            author = author,
            createdAt = Date(),
            updatedAt = null,
            rating = 0
    )

}