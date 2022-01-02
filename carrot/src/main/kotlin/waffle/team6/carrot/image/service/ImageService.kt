package waffle.team6.carrot.image.service

import com.amazonaws.services.s3.AmazonS3Client
import com.amazonaws.services.s3.model.*
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.io.InputStreamResource
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import waffle.team6.carrot.image.dto.ImageDto
import waffle.team6.carrot.image.exception.*
import waffle.team6.carrot.image.model.Image
import waffle.team6.carrot.image.repository.ImageRepository
import waffle.team6.carrot.user.model.User
import java.io.File
import java.time.LocalDateTime
import java.util.UUID

@Service
class ImageService(
    private val amazonS3Client: AmazonS3Client,
    private val imageRepository: ImageRepository
) {
    @Value("\${cloud.aws.s3.bucket}")
    lateinit var bucket: String

    fun upload(image: MultipartFile, user: User): ImageDto.ImageResponse {
        val file = saveFileToLocal(image) ?: throw ImageLocalSaveFailException()
        val fileName = "images/server/" + UUID.randomUUID() + image.name
        putFileToS3(file, fileName)
        removeLocalFile(file)
        return ImageDto.ImageResponse(imageRepository.save(Image(fileName, image.contentType.toString(), user.id)))
    }

    fun download(id: Long): ImageDto.ImageResource {
        val image = imageRepository.findByIdOrNull(id) ?: throw ImageNotFoundException()
        return ImageDto.ImageResource(InputStreamResource(getFileFromS3(image.fileName)))
    }

    fun update(image: MultipartFile, id: Long, user: User): ImageDto.ImageResponse {
        val imageEntity = imageRepository.findByIdOrNull(id) ?: throw ImageNotFoundException()
        if (imageEntity.userId != user.id) throw ImageUpdateByInvalidUserException()
        deleteFileInS3(imageEntity.fileName)
        val file = saveFileToLocal(image) ?: throw ImageLocalSaveFailException()
        val fileName = "images/server/" + UUID.randomUUID() + image.name
        putFileToS3(file, fileName)
        removeLocalFile(file)
        imageEntity.fileName = fileName
        imageEntity.contentType = image.contentType.toString()
        imageRepository.flush()
        return ImageDto.ImageResponse(imageEntity)
    }

    fun delete(id: Long, user: User) {
        val imageEntity = imageRepository.findByIdOrNull(id) ?: throw ImageNotFoundException()
        if (imageEntity.userId != user.id) throw ImageDeleteByInvalidUserException()
        deleteFileInS3(imageEntity.fileName)
        imageRepository.delete(imageEntity)
    }

    fun getImageByIdAndCheckAuthorization(imageId: Long, user: User): Image {
        val image = imageRepository.findByIdOrNull(imageId) ?: throw ImageNotFoundException()
        if (image.userId != user.id) throw ImageAccessByInvalidUserException()
        return image
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

    fun deleteFileInS3(fileName: String) {
        amazonS3Client.deleteObject(DeleteObjectRequest(bucket, fileName))
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