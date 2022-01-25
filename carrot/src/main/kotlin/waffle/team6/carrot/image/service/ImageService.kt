package waffle.team6.carrot.image.service

import com.amazonaws.services.s3.AmazonS3Client
import com.amazonaws.services.s3.model.*
import com.amazonaws.util.IOUtils
import org.apache.http.entity.ContentType
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
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
@Transactional(readOnly = true)
class ImageService(
    private val amazonS3Client: AmazonS3Client,
    private val imageRepository: ImageRepository
) {
    @Value("\${cloud.aws.s3.bucket}")
    lateinit var bucket: String

    private val validContentTypes: List<String> = listOf(
        ContentType.IMAGE_BMP.toString(),
        ContentType.IMAGE_GIF.toString(),
        ContentType.IMAGE_PNG.toString(),
        ContentType.IMAGE_JPEG.toString(),
        ContentType.IMAGE_SVG.toString(),
        ContentType.IMAGE_WEBP.toString(),
        ContentType.IMAGE_TIFF.toString()
    )

    @Transactional
    fun upload(images: List<MultipartFile>, user: User): ImageDto.ImageListResponse {
        val imageEntities: MutableList<Image> = mutableListOf()
        for (image in images) {
            val contentType = image.contentType.toString()
            if (!validContentTypes.contains(contentType)) throw ImageInvalidContentTypeException()
            val file = saveFileToLocal(image) ?: throw ImageLocalSaveFailException()
            val fileName = "images/server/" + UUID.randomUUID() + image.name
            val url = "https://waffle-team6.s3.ap-northeast-2.amazonaws.com/$fileName"
            putFileToS3(file, fileName)
            removeLocalFile(file)
            imageEntities.add(Image(fileName, contentType, url, user.id))
        }
        return ImageDto.ImageListResponse(imageRepository.saveAll(imageEntities))
    }

    fun download(id: Long): ImageDto.ImageUrlResponse {
        val image = imageRepository.findByIdOrNull(id) ?: throw ImageNotFoundException()
        return ImageDto.ImageUrlResponse(image.url)
    }

    @Transactional
    fun update(image: MultipartFile, id: Long, user: User): ImageDto.ImageResponse {
        val imageEntity = imageRepository.findByIdOrNull(id) ?: throw ImageNotFoundException()
        if (imageEntity.userId != user.id) throw ImageUpdateByInvalidUserException()
        deleteFileInS3(imageEntity.fileName)
        val contentType = image.contentType.toString()
        if (!validContentTypes.contains(contentType)) throw ImageInvalidContentTypeException()
        val file = saveFileToLocal(image) ?: throw ImageLocalSaveFailException()
        val fileName = "images/server/" + UUID.randomUUID() + image.name
        val url = "https://waffle-team6.s3.ap-northeast-2.amazonaws.com/$fileName"
        putFileToS3(file, fileName)
        removeLocalFile(file)
        imageEntity.fileName = fileName
        imageEntity.contentType = contentType
        imageEntity.url = url
        return ImageDto.ImageResponse(imageEntity)
    }

    @Transactional
    fun delete(id: Long, user: User) {
        val imageEntity = imageRepository.findByIdOrNull(id) ?: throw ImageNotFoundException()
        if (imageEntity.userId != user.id) throw ImageDeleteByInvalidUserException()
        deleteFileInS3(imageEntity.fileName)
        imageRepository.delete(imageEntity)
    }

    @Transactional
    fun deleteByUrl(url: String, userId: Long) {
        val image = imageRepository.findByUrlIs(url)
        if (image.userId != userId) throw ImageDeleteByInvalidUserException()
        deleteFileInS3(image.fileName)
        imageRepository.delete(image)
    }

    fun putFileToS3(file: File, fileName: String) {
        amazonS3Client.putObject(PutObjectRequest(bucket, fileName, file).withCannedAcl(CannedAccessControlList.PublicRead))
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