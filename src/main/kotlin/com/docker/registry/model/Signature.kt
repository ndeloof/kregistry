package com.docker.registry.model

data class Signature(val signature: String,
                     val protected: String,
                     val header: Header,
)

data class Header(val alg: String)