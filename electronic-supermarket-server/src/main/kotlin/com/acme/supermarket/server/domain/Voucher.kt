package com.acme.supermarket.server.domain

import com.acme.supermarket.server.dto.VoucherDto
import jakarta.persistence.*

@Entity
@Table(name = "vouchers")
data class Voucher(
    @Id
    val uuid: String,

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    val user: User,

    @Column(nullable = false)
    var used: Boolean = false
)

fun Voucher.toDto(): VoucherDto {
    return VoucherDto(
        uuid = this.uuid,
        userUuid = this.user.userUuid,
        used = this.used
    )
}
