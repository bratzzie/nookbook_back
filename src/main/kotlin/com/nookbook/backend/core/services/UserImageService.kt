package com.nookbook.backend.core.services

import com.google.auth.oauth2.GoogleCredentials
import com.google.cloud.storage.*
import com.nookbook.backend.core.services.exceptions.UserImageException
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.PropertySource
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.*


@Service
@PropertySource(value = ["classpath:gcpstorage.properties"])
class UserImageService(
    @Value("\${project_id}") private val projectId: String,
    @Value("\${bucket_name}") private val bucketName: String
) {
    //TODO: signed urls!
    private val CREDENTIALS_FILENAME = "gcpstorage_credentials.json"

    fun downloadPicture(objectName: String): File {
        val tempDir = Files.createTempDirectory("${objectName}Profile")

        val credentials = GoogleCredentials.fromStream(
            this.javaClass.classLoader.getResource(CREDENTIALS_FILENAME)
                ?.openStream()
        )

        val storage: Storage =
            StorageOptions.newBuilder().setCredentials(credentials).setProjectId(projectId).build().service

        val blob: Blob? = storage.get(BlobId.of(bucketName, "${objectName}Profile/${objectName}"))

        if (blob != null && blob.exists()) {
            val fileType = getFileType(blob.contentType)
            val tempDirPath = tempDir.toFile().absolutePath
            val destFilePath = "${tempDirPath}/${objectName}.${fileType}"

            blob.downloadTo(Path.of(destFilePath))
            //println("Successfully downloaded '$objectName' to '$destFilePath'")

            return File(destFilePath)
        } else {
            throw UserImageException("Image '$objectName' not found in bucket '$bucketName'.")
        }
    }


    fun uploadPicture(username: String, file: MultipartFile) {
        val (objectName, tempDir, tempFile) = createTempFile(file, username)

        val credentials = GoogleCredentials.fromStream(
            this.javaClass.classLoader.getResource(CREDENTIALS_FILENAME)
                ?.openStream()
        )

        val storage = StorageOptions.newBuilder().setCredentials(credentials).setProjectId(projectId).build().service
        val blobId = BlobId.of(bucketName, objectName)
        val blobInfo = BlobInfo.newBuilder(blobId).setContentType(file.contentType).build()

        // Optional: set a generation-match precondition to avoid potential race
        // conditions and data corruptions. The request returns a 412 error if the
        // preconditions are not met.
        val precondition = if (storage[bucketName, objectName] == null) {
            // For a target object that does not yet exist, set the DoesNotExist precondition.
            // This will cause the request to fail if the object is created before the request runs.
            Storage.BlobWriteOption.doesNotExist()
        } else {
            // If the destination already exists in your bucket, instead set a generation-match
            // precondition. This will cause the request to fail if the existing object's generation
            // changes before the request runs.
            Storage.BlobWriteOption.generationMatch(
                storage[bucketName, objectName].generation
            )
        }
        storage.createFrom(blobInfo, Paths.get(tempFile.path), precondition)

        //println("File ${tempFile.path} uploaded to bucket $bucketName as $objectName")

        tempDir.toFile().deleteRecursively()
    }

    fun encodeFile(file: File): ByteArray {
        return Base64.getEncoder().encode(file.readBytes())
    }

    private fun createTempFile(file: MultipartFile, username: String): Triple<String, Path, File> {
        val fileType = getFileType(file.contentType!!)
        val objectName = "${username}Profile/${username}"

        val tempDir = Files.createTempDirectory("${username}Profile")
        val tempDirPath = tempDir.toFile().absolutePath
        val tempFile = File.createTempFile(username, ".${fileType}", File(tempDirPath))
        Files.write(tempFile.toPath(), file.bytes)
        return Triple(objectName, tempDir, tempFile)
    }

    private fun getFileType(contentType: String): String {
        val result = when (contentType) {
            "image/jpeg" -> "jpeg"
            "image/jpg" -> "jpg"
            "image/png" -> "png"
            else -> throw UserImageException("$contentType is not supported")
        }

        return result
    }
}