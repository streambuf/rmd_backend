package ru.rmd.monolith.configuration

import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.data.web.config.EnableSpringDataWebSupport


@Configuration
@ComponentScan("ru.rmd.*")
class AppConfiguration