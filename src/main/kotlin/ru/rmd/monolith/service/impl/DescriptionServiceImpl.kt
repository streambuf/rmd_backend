package ru.rmd.monolith.service.impl

import org.springframework.stereotype.Service
import ru.rmd.monolith.service.DescriptionService

@Service
class DescriptionServiceImpl : DescriptionService {

    override fun createDescription(personName: String, serviceName: String, city: String): String {
        val rnd = (0..templates.size - 1).random()
        val template = templates[rnd]
        return template
                .replace(PERSON_NAME_PLACEHOLDER, personName)
                .replace(SERVICE_NAME_PLACEHOLDER, serviceName)
                .replace(CITY_PLACEHOLDER, city)
    }

    companion object {
        const val PERSON_NAME_PLACEHOLDER = "{personName}"
        const val SERVICE_NAME_PLACEHOLDER = "{serviceName}"
        const val CITY_PLACEHOLDER = "{city}"
        // todo from mongo collection
        val templates = listOf(
                "История свидания с $PERSON_NAME_PLACEHOLDER - что бывает, когда знакомишься на $SERVICE_NAME_PLACEHOLDER. Анонимный отзыв о свидании с человеком с сайта знакомств.",
                "Читайте анонимную историю свидания с $PERSON_NAME_PLACEHOLDER c $SERVICE_NAME_PLACEHOLDER - отзыв о знакомстве и встрече с человеком с сайта знакомств",
                "Как $PERSON_NAME_PLACEHOLDER c $SERVICE_NAME_PLACEHOLDER вела себя на свидании? Узнай в анонимном отзыве о свидании с этим человеком на RatemyDate.ru",
                "Как $PERSON_NAME_PLACEHOLDER из города $CITY_PLACEHOLDER c $SERVICE_NAME_PLACEHOLDER вела себя на свидании? Читай в анонимном отзыве о свидании с этим человеком на RatemyDate.ru")
    }

}