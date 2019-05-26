package ru.rmd.monolith.auth

import mu.KotlinLogging
import org.springframework.security.authentication.ReactiveAuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import ru.rmd.monolith.configuration.AppProperties
import ru.rmd.monolith.dto.AuthorityPrincipal
import ru.rmd.monolith.dto.Privilege
import ru.rmd.monolith.utils.JwtUtils
import java.util.*

@Component
class AuthenticationManager(
        private val props: AppProperties
) : ReactiveAuthenticationManager {

    private val logger = KotlinLogging.logger {}

    override fun authenticate(authentication: Authentication?): Mono<Authentication> {
        return Mono.justOrEmpty(authentication?.credentials.toString())
                .map { JwtUtils.convertJwtToObject(it, AuthorityPrincipal::class, props.jwtAuthSigningKey) }
                .onErrorResume { Mono.empty() }
                .map { UsernamePasswordAuthenticationToken(it, null, privilegesToAuthorities(it.privileges)) }
    }

    private fun privilegesToAuthorities(privileges: EnumSet<Privilege>) = privileges.map { SimpleGrantedAuthority("ROLE_${it.name}") }
}