package ru.rmd.monolith.controller

import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Mono
import ru.rmd.monolith.dto.AuthorityPrincipal
import ru.rmd.monolith.dto.UserInfo
import ru.rmd.monolith.dto.request.LoginUserRequest
import ru.rmd.monolith.dto.request.RegisterUserRequest
import ru.rmd.monolith.service.UserService

@RestController
@RequestMapping("/api/v1/users")
class UserController(
        private val userService: UserService
) {

    @PostMapping(value = [""])
    fun create(@RequestBody request: RegisterUserRequest): Mono<UserInfo> {
        return userService.registerUser(request)
    }

    @PostMapping(value = ["login"])
    fun login(@RequestBody request: LoginUserRequest): Mono<UserInfo> {
        return userService.loginUser(request)
    }

    @GetMapping(value = ["/current"])
    fun getCurrentUser(@AuthenticationPrincipal principal: AuthorityPrincipal): Mono<AuthorityPrincipal> {
        return userService.getCurrentUser(principal)
    }

}