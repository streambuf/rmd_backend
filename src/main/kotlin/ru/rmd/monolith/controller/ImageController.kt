package ru.rmd.monolith.controller

import org.springframework.core.io.buffer.DataBuffer
import org.springframework.http.MediaType
import org.springframework.http.codec.multipart.FilePart
import org.springframework.http.server.reactive.ServerHttpResponse
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import ru.rmd.monolith.dto.response.UploadFileResponse
import ru.rmd.monolith.dto.request.UploadImageByUrlRequest
import ru.rmd.monolith.repository.ImageRepository
import ru.rmd.monolith.service.ImageService

@RestController
@RequestMapping("/api/v1/images")
@CrossOrigin(origins = ["*"])
class ImageController(
        private val imageRepository: ImageRepository,
        private val imageService: ImageService
) {

    @GetMapping(value = ["/{id}"], produces = ["image/*"])
    fun load(@PathVariable("id") id: String, response: ServerHttpResponse): Mono<DataBuffer> {
        return imageService.getImageById(id).map {
            response.bufferFactory().wrap(it.data!!)
        }
    }

    @PostMapping(value = ["/uploadByFile"], consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun uploadByFile(@RequestPart("image") file: Flux<FilePart>): Mono<UploadFileResponse> {
        return imageService.createImageByFile(file.flatMap { it.content() })
    }

    @PostMapping(value = ["/uploadByUrl"])
    fun uploadByUrl(@RequestBody request: UploadImageByUrlRequest): Mono<UploadFileResponse> {
        return imageService.createImageByUrl(request)
    }

}