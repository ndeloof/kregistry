package com.docker.registry.store

interface BlobStore {
    fun Get(digest: String) : ByteArray

    fun CreateUpload() : String

    fun Upload(uuid: String, content: ByteArray, digest: String) : Unit

    fun GetLastUploadOffset(uuid: String) : Long

    fun Append(uuid: String, content: ByteArray): Long

    fun Delete(uuid: String)
}