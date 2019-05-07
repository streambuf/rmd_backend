package ru.rmd.monolith.configuration

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Primary
import org.springframework.stereotype.Component
import org.springframework.validation.annotation.Validated
import javax.validation.constraints.NotEmpty

@Primary
@Component
@Validated
@ConfigurationProperties("spring.app")
class AppProperties {

    @NotEmpty
    lateinit var serverHost: String

}