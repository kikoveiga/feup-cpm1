package com.acme.supermarket.server.controller;

import com.acme.supermarket.server.domain.Voucher
import com.acme.supermarket.server.dto.CreateVoucherDto
import com.acme.supermarket.server.repository.UserRepository;
import com.acme.supermarket.server.repository.VoucherRepository;
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/api/vouchers")
class VoucherController(
    private val userRepository:UserRepository,
    private val voucherRepository:VoucherRepository
) {


    @PostMapping("/create")
    fun createVoucher(@RequestBody dto: CreateVoucherDto): ResponseEntity<Any> {
        val user = userRepository.findByUserUuid(dto.uuid)
            ?: return ResponseEntity.badRequest().body("User not found")

        val voucher = Voucher(
            uuid = UUID.randomUUID().toString(),
            user = user,
            used = false
        )
        voucherRepository.save(voucher)
        return ResponseEntity.ok().body("Voucher created")
    }

}

