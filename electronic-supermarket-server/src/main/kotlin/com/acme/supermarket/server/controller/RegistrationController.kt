package com.acme.supermarket.server.controller

import com.acme.supermarket.server.dto.RegistrationRequest
import com.acme.supermarket.server.dto.UserResponse
import com.acme.supermarket.server.service.UserService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api")
class RegistrationController (
    private val userService: UserService
) {

    @PostMapping("/register")
    fun registerUser(@RequestBody request: RegistrationRequest): ResponseEntity<UserResponse> {
        val response = userService.registerUser(request)

        return ResponseEntity.ok(response)
    }
}