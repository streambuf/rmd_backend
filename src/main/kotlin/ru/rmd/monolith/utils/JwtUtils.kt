package ru.rmd.monolith.utils

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import io.jsonwebtoken.Claims
import io.jsonwebtoken.JwtException
import io.jsonwebtoken.Jwts
import ru.rmd.monolith.exception.JwtParseException
import javax.crypto.SecretKey
import kotlin.reflect.KClass

object JwtUtils {

    private val mapper: ObjectMapper = ObjectMapper()

    init {
        mapper.registerModule(KotlinModule())
    }


    fun <T: Any> convertJwtToObject(jwt: String, clazz: KClass<T>, signingKey: SecretKey) : T {
        try {
            val payload = getTokenBody(signingKey, jwt)
            return mapper.convertValue(payload, clazz.java)
        } catch (e: java.lang.Exception) {
            throw JwtParseException("Error parse ${clazz.simpleName}: ${e.message}")
        }
    }

    fun <T> generateJwt(payload: T, signingKey: SecretKey): String {
        try {
            val strPayload = ObjectMapper().writeValueAsString(payload)
            return Jwts.builder().setPayload(strPayload).signWith(signingKey).compact()
        } catch (e: Exception) {
            throw JwtParseException("Error parse $payload: ${e.message}")
        }
    }

    private fun getTokenBody(key: SecretKey, jwt: String): Claims {
        try {
            return Jwts.parser().setSigningKey(key).parseClaimsJws(jwt).body
        } catch (e: JwtException) {
            throw JwtParseException("Invalid jwt token: ${e.message}")
        }
    }
}