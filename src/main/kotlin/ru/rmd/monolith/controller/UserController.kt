package ru.rmd.monolith.controller

import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Mono
import ru.rmd.monolith.dto.UserInfoDTO
import ru.rmd.monolith.dto.request.RegisterUserRequest
import ru.rmd.monolith.service.UserService

@RestController
@RequestMapping("/api/v1/users")
@CrossOrigin(origins = ["*"])
class UserController(
        private val userService: UserService
) {

    @PostMapping(value = [""])
    fun create(@RequestBody request: RegisterUserRequest): Mono<UserInfoDTO> {
        return userService.registerUser(request)
    }

    @GetMapping(value = ["/current"])
    fun getCurrentUser(): Mono<Authentication> {
        return userService.getCurrentUser()
    }

}