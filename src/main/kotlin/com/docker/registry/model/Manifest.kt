package com.docker.registry.model

data class Manifest(val name: String,
                    val tag: String,
                    val architecture: String
)