package waffle.team6.carrot.image.service

import com.amazonaws.services.s3.AmazonS3Client
import com.amazonaws.services.s3.model.CannedAccessControlList
import com.amazonaws.services.s3.model.GetObjectRequest
import com.amazonaws.services.s3.model.PutObjectRequest
import com.amazonaws.services.s3.model.S3ObjectInputStream
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.io.InputStreamResource
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import waffle.team6.carrot.image.dto.ImageDto
import waffle.team6.carrot.image.exception.ImageLocalSaveFailException
import waffle.team6.carrot.image.exception.ImageNotFoundException
import waffle.team6.carrot.image.model.Image
import waffle.team6.carrot.image.repository.ImageRepository
import java.io.File
import java.nio.file.Files
import java.time.LocalDateTime
import java.util.UUID

@Service
class ImageService(
    private val amazonS3Client: AmazonS3Client,
    private val imageRepository: ImageRepository
) {
    @Value("\${cloud.aws.s3.bucket}")
    lateinit var bucket: String

    fun upload(image: MultipartFile): ImageDto.UploadResponse {
        val file = saveFileToLocal(image)
        if (file != null) {
            val fileName = "images/" + UUID.randomUUID() + image.name
            putFileToS3(file, fileName)
            removeLocalFile(file)
            return ImageDto.UploadResponse(imageRepository.save(Image(fileName, image.contentType.toString())))
        } else throw ImageLocalSaveFailException()
    }

    fun download(id: Long): ImageDto.DownloadResponse {
        val image = imageRepository.findByIdOrNull(id) ?: throw ImageNotFoundException()
        return ImageDto.DownloadResponse(InputStreamResource(getFileFromS3(image.fileName)))
    }

    fun getContentType(id: Long): String {
        val image = imageRepository.findByIdOrNull(id) ?: throw ImageNotFoundException()
        return image.contentType
    }

    fun putFileToS3(file: File, fileName: String) {
        amazonS3Client.putObject(PutObjectRequest(bucket, fileName, file).withCannedAcl(CannedAccessControlList.PublicRead))
    }

    fun getFileFromS3(fileName: String): S3ObjectInputStream {
        return amazonS3Client.getObject(GetObjectRequest(bucket, fileName)).objectContent
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