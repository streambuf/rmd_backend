package ru.rmd.monolith.repository.impl

import org.springframework.data.domain.PageRequest
import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.Update
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import ru.rmd.monolith.dto.request.PersistPostRequest
import ru.rmd.monolith.entity.PostEntity
import ru.rmd.monolith.repository.PostRepositoryCustom
import java.util.*

@Repository
class PostRepositoryCustomImpl(
        private val template: ReactiveMongoTemplate
) : PostRepositoryCustom {

    override fun update(id: String, request: PersistPostRequest) = template.updateFirst(Query(Criteria.where("id").`is`(id)),
                Update().set("message", request.message)
                        .set("datingService", request.datingService)
                        .set("datingServiceProfileLink", request.datingServiceProfileLink ?: "")
                        .set("city", request.city)
                        .set("name", request.name)
                        .set("age", request.age)
                        .set("gender", request.gender)
                        .set("image", request.image ?: "")
                        .set("updatedAt", Date()),
            PostEntity::class.java)


    override fun find(pageRequest: PageRequest): Flux<PostEntity> {
        val query = Query()
        query.with(pageRequest)
        return template.find(query, PostEntity::class.java, "posts")
    }

}