package ru.rmd.monolith.utils

import java.util.stream.Collectors

object TranslitUtils {

    val translitMap = mapOf('а' to "a", 'б' to "b", 'в' to "v", 'г' to "g", 'д' to "d", 'е' to "e", 'ё' to "jo", 'ж' to "zh",
            'з' to "z", 'и' to "i", 'й' to "i", 'к' to "k", 'л' to "l", 'м' to "m", 'н' to "n", 'о' to "o", 'п' to "p", 'р' to "r",
            'с' to "s", 'т' to "t", 'у' to "u", 'ф' to "f", 'х' to "h", 'ц' to "ts", 'ш' to "sh", 'щ' to "sch", 'ъ' to "",
            'ы' to "i", 'ь' to "", 'э' to "e", 'ю' to "ju", 'я' to "ja")

    fun translit(text: String) = text.toLowerCase().chars()
            .mapToObj { c -> translitMap[c.toChar()] ?: c.toChar().toString()}
            .collect(Collectors.joining())

}