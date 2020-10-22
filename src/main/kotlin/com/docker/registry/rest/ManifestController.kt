package com.docker.registry.rest

import com.docker.registry.model.Blob
import com.docker.registry.model.Manifest
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
class ManifestController {

  @GetMapping(path = arrayOf("/v2/{organization}/{repository}/manifests/{reference}"),
              produces = arrayOf(ManifestV2)
  )
  fun getManifest(@PathVariable organization: String,
                  @PathVariable repository: String,
                  @PathVariable reference: String
  ): Manifest {
    return Manifest(name = "$organization/$repository", architecture = "amd64", tag = reference, fsLayers = listOf(
            Blob(blobSum = "sha256:a3ed95caeb02ffe68cdd9fd84406680ae93d633cb16422d00e8a7c22955b46d4"),
            Blob(blobSum = "sha256:9cbd0633c6585465510232471ceeb4f0e7034887fc22aa2181e680b71726015a")
    ))
  }
}