package com.docker.registry.rest

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.net.URI
import java.util.*

@RestController
class BlobController {

    @GetMapping(path = ["/v2/{organization}/{repository}/blobs/{digest}"],
            produces = ["application/octet-stream"])
    @ResponseBody
    fun getBlob(@PathVariable organization: String,
                @PathVariable repository: String,
                @PathVariable digest: String,
    ): ByteArray = ByteArray(0)

    @RequestMapping(path = ["/v2/{organization}/{repository}/blobs/{digest}"],
                    method = [RequestMethod.HEAD])
    fun headBlob(@PathVariable organization: String,
                 @PathVariable repository: String,
                 @PathVariable digest: String,
    ): Any = Unit


    @PostMapping(path = ["/v2/{organization}/{repository}/blobs/uploads"])
    fun mount(@PathVariable organization: String,
              @PathVariable repository: String,
              @RequestParam("mount") digest: String,
              @RequestParam from: String,
    ): ResponseEntity<Any> {
        val uri = URI.create("/v2/$organization/$repository/blobs/$digest")
        return ResponseEntity.created(uri)
                .header("Docker-Upload-UUID", digest)
                .build()
    }

    @PostMapping(path = ["/v2/{organization}/{repository}/blobs/uploads"])
    fun createUpload(@PathVariable organization: String,
                     @PathVariable repository: String,
    ): ResponseEntity<Any> {
        val uuid = UUID.randomUUID().toString()
        val uri = URI.create("/v2/$organization/$repository/blobs/uploads/$uuid")
        return ResponseEntity.accepted()
                .header("Docker-Upload-UUID", uuid)
                .header("Range", "bytes=0-0")
                .location(uri).build()
    }

    @GetMapping(path = ["/v2/{organization}/{repository}/blobs/uploads/{uuid}"])
    fun getUpload(@PathVariable organization: String,
                     @PathVariable repository: String,
                     @PathVariable uuid: String,
    ): ResponseEntity<Any> {
        val uri = URI.create("/v2/$organization/$repository/blobs/uploads/$uuid")
        val offset = 1024
        return ResponseEntity.noContent()
                .header("Docker-Upload-UUID", uuid)
                .header("Range", "bytes=0-$offset")
                .location(uri).build()
    }

    @PutMapping(path = ["/v2/{organization}/{repository}/blobs/uploads/{uuid}"])
    fun monolithicUpload(@PathVariable organization: String,
                  @PathVariable repository: String,
                  @PathVariable uuid: String,
                  @RequestParam digest: String,
    ): ResponseEntity<Any> {
        val uri = URI.create("/v2/$organization/$repository/blobs/$digest")
        return ResponseEntity.created(uri)
                .header("Docker-Content-Digest", digest)
                .build()
    }

    @PatchMapping(path = ["/v2/{organization}/{repository}/blobs/uploads/{uuid}"])
    fun chunckedUpload(@PathVariable organization: String,
                       @PathVariable repository: String,
                       @PathVariable uuid: String,
                       @RequestHeader("Content-Length") length: Int,
                       @RequestHeader("Content-Range") range: String,
    ): ResponseEntity<Any> {
        val uri = URI.create("/v2/$organization/$repository/blobs/uploads/$uuid")
        val offset = 1024 + length
        return ResponseEntity.accepted()
                .header("Docker-Upload-UUID", uuid)
                .header("Range", "bytes=0-$offset")
                .location(uri).build()
    }


    @DeleteMapping(path = ["/v2/{organization}/{repository}/blobs/uploads/{uuid}"])
    fun cancelUpload(@PathVariable organization: String,
                     @PathVariable repository: String,
                     @PathVariable uuid: String,
    ): Any = Unit

    @PutMapping(path = ["/v2/{organization}/{repository}/blobs/uploads/{uuid}"])
    fun completeUpload(@PathVariable organization: String,
                       @PathVariable repository: String,
                       @PathVariable uuid: String,
                       @RequestHeader("Content-Length") length: Int,
                       @RequestHeader("Content-Range") range: String,
                       @RequestParam digest: String,
    ): ResponseEntity<Any> {
        val uri = URI.create("/v2/$organization/$repository/blobs/$digest")
        return ResponseEntity.created(uri)
                .header("Docker-Content-Digest", digest)
                .build()
    }
}