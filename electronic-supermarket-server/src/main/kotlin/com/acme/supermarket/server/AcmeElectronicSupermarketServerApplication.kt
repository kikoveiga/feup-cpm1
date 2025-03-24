package com.acme.supermarket.server

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class AcmeElectronicSupermarketServerApplication

fun main(args: Array<String>) {
	runApplication<AcmeElectronicSupermarketServerApplication>(*args)
}
