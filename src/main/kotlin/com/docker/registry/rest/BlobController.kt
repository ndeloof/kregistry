package com.docker.registry.rest

import com.docker.registry.store.BlobStore
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.io.InputStream
import java.net.URI

@RestController
class BlobController {

    @Autowired
    lateinit var blobs: BlobStore

    @GetMapping(path = ["/v2/{organization}/{repository}/blobs/{digest}"],
            produces = ["application/octet-stream"])
    @ResponseBody
    fun getBlob(
            @PathVariable organization: String,
            @PathVariable repository: String,
            @PathVariable digest: String,
    ): ByteArray = blobs.Get(digest)

    @RequestMapping(path = ["/v2/{organization}/{repository}/blobs/{digest}"],
            method = [RequestMethod.HEAD])
    fun headBlob(
            @PathVariable organization: String,
            @PathVariable repository: String,
            @PathVariable digest: String,
    ): Any = Unit


    @PostMapping(path = ["/v2/{organization}/{repository}/blobs/uploads"])
    fun createUpload(
            @PathVariable organization: String,
            @PathVariable repository: String,
    ): ResponseEntity<Any> {
        val uuid = blobs.CreateUpload()
        val uri = URI.create("/v2/$organization/$repository/blobs/uploads/$uuid")
        return ResponseEntity.accepted()
                .header("Docker-Upload-UUID", uuid)
                .header("Range", "bytes=0-0")
                .location(uri).build()
    }

    @GetMapping(path = ["/v2/{organization}/{repository}/blobs/uploads/{uuid}"])
    fun getUpload(
            @PathVariable organization: String,
            @PathVariable repository: String,
            @PathVariable uuid: String,
    ): ResponseEntity<Any> {
        val offset = blobs.GetLastUploadOffset(uuid)
        val uri = URI.create("/v2/$organization/$repository/blobs/uploads/$uuid")
        return ResponseEntity.noContent()
                .header("Docker-Upload-UUID", uuid)
                .header("Range", "bytes=0-$offset")
                .location(uri).build()
    }

    @PatchMapping(path = ["/v2/{organization}/{repository}/blobs/uploads/{uuid}"])
    fun chunckedUpload(
            @PathVariable organization: String,
            @PathVariable repository: String,
            @PathVariable uuid: String,
            @RequestHeader("Content-Length") length: Int,
            @RequestHeader("Content-Range") range: String,
            @RequestBody content: ByteArray,
    ): ResponseEntity<Any> {
        var offset = blobs.Append(uuid, content)
        val uri = URI.create("/v2/$organization/$repository/blobs/uploads/$uuid")
        return ResponseEntity.accepted()
                .header("Docker-Upload-UUID", uuid)
                .header("Range", "bytes=0-$offset")
                .location(uri).build()
    }


    @DeleteMapping(path = ["/v2/{organization}/{repository}/blobs/uploads/{uuid}"])
    fun cancelUpload(
            @PathVariable organization: String,
            @PathVariable repository: String,
            @PathVariable uuid: String,
    ): Any {
        blobs.Delete(uuid)
        return Unit
    }


    @PutMapping(path = ["/v2/{organization}/{repository}/blobs/uploads/{uuid}"],
                consumes = ["application/octet-stream"],
                params = ["digest"], headers = ["Content-Range"])
    fun completeUpload(
            @PathVariable organization: String,
            @PathVariable repository: String,
            @PathVariable uuid: String,
            @RequestHeader("Content-Length") length: Int,
            @RequestHeader("Content-Range") range: String,
            @RequestParam digest: String,
            @RequestBody content: ByteArray,
    ): ResponseEntity<Any> {
        blobs.Upload(uuid, content, digest)
        val uri = URI.create("/v2/$organization/$repository/blobs/$digest")
        return ResponseEntity.created(uri)
        .header ("Docker-Content-Digest", digest)
        .build()
    }

    @PutMapping(path = ["/v2/{organization}/{repository}/blobs/uploads/{uuid}"],
                consumes = ["application/octet-stream"],
                params = ["digest"])
    fun monolithUpload(
            @PathVariable organization: String,
            @PathVariable repository: String,
            @PathVariable uuid: String,
            @RequestHeader("Content-Length") length: Int,
            @RequestParam digest: String,
            @RequestBody content: ByteArray,
    ): ResponseEntity<Any> {
        blobs.Upload(uuid, content, digest)
        val uri = URI.create("/v2/$organization/$repository/blobs/$digest")
        return ResponseEntity.created(uri)
                .header ("Docker-Content-Digest", digest)
                .build()
    }


    @PutMapping(path = ["/v2/{organization}/{repository}/blobs/uploads/{uuid}"],
                consumes = ["application/octet-stream"],
                params = ["mount", "from"])
    fun mount(@PathVariable organization: String,
                       @PathVariable repository: String,
                       @PathVariable uuid: String,
                       @RequestParam("mount") digest: String,
                       @RequestParam from: String?,
                       @RequestBody content: ByteArray,
    ): ResponseEntity<Any> {
        // TODO create mount
        val uri = URI.create("/v2/$organization/$repository/blobs/$digest")
        return ResponseEntity.created(uri)
                .header("Docker-Content-Digest", digest)
                .build()
    }
}