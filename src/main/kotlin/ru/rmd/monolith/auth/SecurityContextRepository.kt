package ru.rmd.monolith.auth

import org.springframework.http.HttpHeaders
import org.springframework.security.authentication.ReactiveAuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.core.context.SecurityContextImpl
import org.springframework.security.web.server.context.ServerSecurityContextRepository
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono

@Component
class SecurityContextRepository(
        private val authenticationManager: ReactiveAuthenticationManager
) : ServerSecurityContextRepository {

    override fun save(exchange: ServerWebExchange?, context: SecurityContext?): Mono<Void> {
        TODO("not implemented")
    }

    override fun load(exchange: ServerWebExchange?): Mono<SecurityContext> {
       return exchange?.request?.headers
               ?.getFirst(HttpHeaders.AUTHORIZATION)
               ?.takeIf { authHeader -> authHeader.startsWith(AUTHORIZATION_PREFIX) }
               ?.let { authHeader -> authHeader.substring(AUTHORIZATION_PREFIX.length) }
               .let { authToken -> UsernamePasswordAuthenticationToken(authToken, authToken) }
               .let { authenticationManager.authenticate(it) }
               .map { SecurityContextImpl(it) }
    }

    companion object {
        const val AUTHORIZATION_PREFIX = "Bearer "
    }
}