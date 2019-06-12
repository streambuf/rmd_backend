package ru.rmd.monolith.service.impl

import com.redfin.sitemapgenerator.WebSitemapGenerator
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import ru.rmd.monolith.configuration.AppProperties
import ru.rmd.monolith.service.PostService
import ru.rmd.monolith.service.SitemapService

@Service
class SitemapServiceImpl(
        private val postService: PostService,
        private val properties: AppProperties
) : SitemapService {


    override fun createSitemap(): Mono<String> {
        val sitemap = WebSitemapGenerator(properties.serverHost)
        return postService.getAll()
                .doOnNext { sitemap.addUrl("${properties.serverHost}/posts/${it.slug}") }
                .reduce { t, _ -> t }.map { sitemap.writeAsStrings().joinToString("") }
    }
}