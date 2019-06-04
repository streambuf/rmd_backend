package ru.rmd.monolith.controller

import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/api/v1/robots")
class SearchRobotsController {

    val ROBOTS_TXT = """
            User-agent: *
            Disallow: /registration
            Disallow: /login
            Disallow: /posts/add
            Disallow: /posts/edit/*/

            User-agent: Yandex
            Disallow: /registration
            Disallow: /login
            Disallow: /posts/add
            Disallow: /posts/edit/*/
            Host: https://ratemydate.ru

            Sitemap: https://ratemydate.ru/sitemap.xml
        """.trimIndent()

    @GetMapping(value = [""], produces = [MediaType.TEXT_PLAIN_VALUE])
    fun getRobotsTxt(): Mono<String> {
        return Mono.just(ROBOTS_TXT)
    }

}