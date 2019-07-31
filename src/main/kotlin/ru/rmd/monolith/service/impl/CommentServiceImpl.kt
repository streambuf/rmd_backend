package ru.rmd.monolith.service.impl

import org.springframework.stereotype.Service
import org.springframework.util.StringUtils
import reactor.core.publisher.Mono
import ru.rmd.monolith.dto.AuthorityPrincipal
import ru.rmd.monolith.dto.request.PersistCommentRequest
import ru.rmd.monolith.entity.CommentEntity
import ru.rmd.monolith.repository.CommentRepository
import ru.rmd.monolith.service.CommentService
import ru.rmd.monolith.service.PostService
import ru.rmd.monolith.service.UserService
import java.util.*

@Service
class CommentServiceImpl(
        private val postService: PostService,
        private val commentRepository: CommentRepository,
        private val userService: UserService
) : CommentService {

    override fun getByPostId(postId: String) = commentRepository.findByPostIdOrderByCreatedAtDesc(postId)

    override fun create(request: PersistCommentRequest, principal: AuthorityPrincipal): Mono<CommentEntity> {
        val userMono = request.fakeAuthor
                ?.takeIf { !StringUtils.isEmpty(it) }
                ?.let { userService.createFakeUser(it) }
                ?.map { it.id }
                ?: Mono.just(principal.login)
        return userMono.flatMap { userLogin ->
            commentRepository.save(convertRequestToCommentEntity(request, userLogin!!))
                    .flatMap { comment -> postService.updateCommentsCount(comment.postId, 1).map { comment } }
        }

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