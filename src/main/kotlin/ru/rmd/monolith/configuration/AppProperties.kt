package ru.rmd.monolith.configuration

import io.jsonwebtoken.security.Keys
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Primary
import org.springframework.stereotype.Component
import org.springframework.validation.annotation.Validated
import javax.annotation.PostConstruct
import javax.crypto.SecretKey
import javax.validation.constraints.NotEmpty

@Primary
@Component
@Validated
@ConfigurationProperties("spring.app")
class AppProperties {

    @NotEmpty
    lateinit var serverHost: String

    @NotEmpty
    lateinit var jwtAuthSecret: String

    lateinit var jwtAuthSigningKey: SecretKey

    @PostConstruct
    fun init() {
        jwtAuthSigningKey = Keys.hmacShaKeyFor(jwtAuthSecret.toByteArray())
    }

}