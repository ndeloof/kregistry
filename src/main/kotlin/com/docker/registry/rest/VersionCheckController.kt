package com.docker.registry.rest

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class VersionCheckController {

    @GetMapping("/v2/")
    fun v2(): Any = Unit
}