package ru.rmd.monolith.service

interface DescriptionService {
    fun createDescription(personName: String, serviceName: String, city: String): String
}