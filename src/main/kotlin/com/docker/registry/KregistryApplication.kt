package com.docker.registry

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class KregistryApplication

fun main(args: Array<String>) {
	runApplication<KregistryApplication>(*args)
}
