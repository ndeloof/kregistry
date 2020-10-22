package com.docker.registry.model

data class Manifest(val name: String,
                    val tag: String,
                    val architecture: String,
                    val fsLayers: List<Blob>,
                    val signature: List<Signature>,
)