package com.acme.supermarket.server.controller

import com.acme.supermarket.server.domain.User
import com.acme.supermarket.server.dto.RegisterUserRequestDto
import com.acme.supermarket.server.dto.UserResponse
import com.acme.supermarket.server.service.UserService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api")
class RegistrationController (
    private val userService: UserService
) {

    @PostMapping("/register")
    fun registerUser(@RequestBody request: RegisterUserRequestDto): ResponseEntity<UserResponse> {
        val response = userService.registerUser(request)

        return ResponseEntity.ok(response)
    }

    @GetMapping("/customer/{userUuid}")
    fun getCustomerByUuid(@PathVariable userUuid: String): ResponseEntity<User> {
        val customer = userService.findByUserUuid(userUuid)
        return if (customer != null) {
            ResponseEntity.ok(customer)
        } else {
            ResponseEntity.notFound().build()
        }
    }
}