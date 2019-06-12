package ru.rmd.monolith.repository.impl

import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.Update
import org.springframework.stereotype.Repository
import org.springframework.util.CollectionUtils
import org.springframework.util.StringUtils
import reactor.core.publisher.Flux
import ru.rmd.monolith.dto.request.PersistPostRequest
import ru.rmd.monolith.dto.request.PostListSearchRequest
import ru.rmd.monolith.entity.PostEntity
import ru.rmd.monolith.repository.PostRepositoryCustom
import java.util.*

@Repository
class PostRepositoryCustomImpl(
        private val template: ReactiveMongoTemplate
) : PostRepositoryCustom {

    override fun update(id: String, request: PersistPostRequest) = template.updateFirst(Query(Criteria.where("slug").`is`(id)),
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


    override fun find(req: PostListSearchRequest): Flux<PostEntity> {
        val query = Query()

        if (!StringUtils.isEmpty(req.city)) {
            query.addCriteria(Criteria.where("city").`is`(req.city))
        }

        if (!StringUtils.isEmpty(req.gender)) {
            query.addCriteria(Criteria.where("gender").`is`(req.gender))
        }

        if (req.ageMin != null && req.ageMax != null) {
            query.addCriteria(Criteria.where("age").gte(req.ageMin).lte(req.ageMax))
        } else if (req.ageMin != null) {
            query.addCriteria(Criteria.where("age").gte(req.ageMin))
        } else if (req.ageMax != null) {
            query.addCriteria(Criteria.where("age").lte(req.ageMax))
        }

        if (req.minDate != null) {
            query.addCriteria(Criteria.where("createdAt").gte(req.minDate))
        }

        if (!CollectionUtils.isEmpty(req.services)) {
            query.addCriteria(Criteria.where("datingService").`in`(*req.services!!.toTypedArray()))
        }

        query.with(req.pageRequest)
        return template.find(query, PostEntity::class.java, "posts")
    }

    override fun findAll(): Flux<PostEntity> {
        val query = Query()
        return template.find(query, PostEntity::class.java, "posts")
    }

}