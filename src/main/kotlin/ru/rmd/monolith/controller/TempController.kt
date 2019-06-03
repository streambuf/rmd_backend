package ru.rmd.monolith.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.rmd.monolith.repository.PostRepository
import ru.rmd.monolith.utils.TranslitUtils

@RestController
@RequestMapping("/api/v1/temp")
class TempController(
        private val postRepository: PostRepository
) {

    @GetMapping(value = ["/slug"])
    fun updateSlugs() = postRepository.findAll().flatMap {
        var slug = "отзыв с сайта знакомств ${it.datingService} ${it.name} ${it.age} город ${it.city}"
        slug = slug.replace(" ", "-").replace(".", "-")
        slug = TranslitUtils.translit(slug)
        it.slug = slug
        postRepository.save(it)
    }

}