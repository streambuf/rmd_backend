package ru.rmd.monolith.configuration

import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration


@Configuration
@ComponentScan("ru.rmd.*")
class AppConfiguration