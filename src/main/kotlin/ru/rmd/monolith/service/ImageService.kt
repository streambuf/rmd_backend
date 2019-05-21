package ru.rmd.monolith.service

import org.springframework.core.io.buffer.DataBuffer
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import ru.rmd.monolith.dto.response.UploadFileResponse
import ru.rmd.monolith.dto.request.UploadImageByUrlRequest
import ru.rmd.monolith.entity.ImageEntity

interface ImageService {
    fun getImageById(id: String): Mono<ImageEntity>
    fun createImageByFile(data: Flux<DataBuffer>): Mono<UploadFileResponse>
    fun createImageByUrl(request: UploadImageByUrlRequest): Mono<UploadFileResponse>
}