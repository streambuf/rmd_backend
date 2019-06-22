package ru.rmd.monolith.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.rmd.monolith.repository.PostRepository
import ru.rmd.monolith.service.DescriptionService

@RestController
@RequestMapping("/api/v1/temp")
class TempController(
        private val postRepository: PostRepository,
        private val descriptionService: DescriptionService
) {


    @GetMapping(value = ["/description"])
    fun getCurrentUser() = postRepository.findAll().map {
        val d = descriptionService.createDescription(it.name, it.datingService, it.city)
        it.description = d
        postRepository.save(it)
        it
    }


}