package ru.rmd.monolith.service

import reactor.core.publisher.Mono

interface SitemapService {
    fun createSitemap(): Mono<String>
}