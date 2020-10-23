package com.docker.registry.store

import org.apache.commons.codec.digest.DigestUtils
import org.apache.tomcat.util.http.fileupload.FileUtils
import org.springframework.stereotype.Service
import java.io.File
import java.io.FileInputStream
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.*

@Service
class NaiveBlobStore: BlobStore {

    var root: String = "/tmp/registry"

    override fun Get(digest: String): ByteArray {
        return ByteArray(0)
    }

    override fun CreateUpload(): String {
        val uuid = UUID.randomUUID().toString()
        val file = File(root, uuid)
        file.createNewFile()
        return uuid
    }

    override fun Upload(uuid: String, content: ByteArray, digest: String) {
        val file = File(root, uuid)
        file.appendBytes(content)

        val i = digest.indexOf(':')
        val algorithm = digest.substring(0,i)
        val expected = digest.substring(i+1)
        var digested: String
        when (algorithm) {
            "sha256" -> digested = DigestUtils.sha256Hex(FileInputStream(file))
            else -> throw NoSuchAlgorithmException(algorithm)
        }

        if (digested != expected) {
            throw IllegalArgumentException("Invalid digest, $digested != $digest")
        }
    }

    override fun GetLastUploadOffset(uuid: String): Long {
        return File(root, uuid).length()
    }

    override fun Append(uuid: String, content: ByteArray): Long {
        File(root, uuid).appendBytes(content)
        return GetLastUploadOffset(uuid)
    }

    override fun Delete(uuid: String) {
        File(root, uuid).delete()
    }


}