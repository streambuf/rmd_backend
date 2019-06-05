package ru.rmd.monolith.controller

import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono
import ru.rmd.monolith.service.SitemapService

@RestController
@RequestMapping("/api/v1/sitemap.xml")
class SitemapController(
        private val sitemapService: SitemapService
) {

    @GetMapping(value = [""], produces = [MediaType.APPLICATION_XML_VALUE])
    fun getSitemap(): Mono<String> {
        return sitemapService.createSitemap()
    }

}