package ru.rmd.monolith.service.impl

import org.apache.commons.io.IOUtils
import org.springframework.core.io.buffer.DataBuffer
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.core.publisher.switchIfEmpty
import ru.rmd.monolith.configuration.AppProperties
import ru.rmd.monolith.dto.UploadFile
import ru.rmd.monolith.dto.UploadFileResponse
import ru.rmd.monolith.dto.UploadImageByUrlRequest
import ru.rmd.monolith.entity.ImageEntity
import ru.rmd.monolith.exception.NotFoundException
import ru.rmd.monolith.repository.ImageRepository
import ru.rmd.monolith.service.ImageService
import java.io.InputStream
import java.io.SequenceInputStream

@Service
class ImageServiceImpl(
        private val imageRepository: ImageRepository,
        private val props: AppProperties
) : ImageService {

    override fun getImageById(id: String) = imageRepository.findById(id)
            .switchIfEmpty { Mono.error(NotFoundException("Image with id $id not found")) }


    override fun createImageByFile(data: Flux<DataBuffer>) = data.collect({ InputStreamCollector() },
            { isCollector, dataBuffer -> isCollector.collectInputStream(dataBuffer.asInputStream()) })
            .map {
                IOUtils.toByteArray(it.inputStream)
            }.flatMap {
                imageRepository.insert(ImageEntity(it))
            }.map {
                UploadFileResponse(file = (UploadFile("${props.serverHost}/api/v1/images/${it.id}")))
            }

    override fun createImageByUrl(request: UploadImageByUrlRequest): Mono<UploadFileResponse> {
        val data = WebClient.create(request.url).get().retrieve().bodyToFlux(DataBuffer::class.java)
        return createImageByFile(data);
    }

    private class InputStreamCollector {
        var inputStream: InputStream? = null
            private set

        fun collectInputStream(inputStream: InputStream) {
            if (this.inputStream == null) this.inputStream = inputStream
            this.inputStream = SequenceInputStream(this.inputStream, inputStream)
        }
    }


}