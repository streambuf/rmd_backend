package ru.rmd.monolith.utils

import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec

object HashGenerator {

    fun generateHash(password: String, login: String): String {
        val keySpec = PBEKeySpec(password.toCharArray(), login.toByteArray(), 65536, 128)
        val factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        return factory.generateSecret(keySpec).encoded.toString();
    }
}