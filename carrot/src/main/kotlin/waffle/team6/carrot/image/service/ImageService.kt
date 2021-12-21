package waffle.team6.carrot.image.service

import com.amazonaws.services.s3.AmazonS3Client
import com.amazonaws.services.s3.model.CannedAccessControlList
import com.amazonaws.services.s3.model.PutObjectRequest
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.time.LocalDateTime
import java.util.UUID

@Service
class ImageService(
    private val amazonS3Client: AmazonS3Client,
    @Value("\${cloud.aws.s3.bucket}") var bucket: String
) {
    fun upload(images: List<MultipartFile>): List<String> {
        val urls: MutableList<String> = mutableListOf()
        for (image in images) {
            val file = saveFileToLocal(image)
            if (file != null) {
                urls.add(putFileToS3(file, "/images/" + UUID.randomUUID() + image.name))
                removeLocalFile(file)
            }
        }
        return urls.toList()
    }

    fun putFileToS3(file: File, fileName: String): String {
        amazonS3Client.putObject(PutObjectRequest(bucket, fileName, file).withCannedAcl(CannedAccessControlList.PublicRead))
        return amazonS3Client.getResourceUrl(bucket, fileName)
    }

    fun saveFileToLocal(file: MultipartFile): File? {
        val name: String? = file.originalFilename
        return if (name != null) {
            val savedFile = File("/tmp/${LocalDateTime.now()}$name")
            file.transferTo(savedFile)
            savedFile
        } else null
    }

    fun removeLocalFile(file: File) {
        file.delete()
    }
}