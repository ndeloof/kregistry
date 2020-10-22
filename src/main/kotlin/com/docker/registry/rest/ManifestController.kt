package com.docker.registry.rest

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

    return Manifest(name = "$organization/$repository", architecture = "amd64", tag = reference)
  }
}