package waffle.team6.carrot.image.api

import org.springframework.core.io.InputStreamResource
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import waffle.team6.carrot.image.dto.ImageDto
import waffle.team6.carrot.image.service.ImageService

@RestController
@RequestMapping("/api/v1/images")
class ImageController(
    private val imageService: ImageService
) {
    @PostMapping("/")
    fun upload(@RequestPart image: MultipartFile): ResponseEntity<ImageDto.UploadResponse> {
        return ResponseEntity.status(HttpStatus.CREATED).body(imageService.upload(image))
    }

    @GetMapping("/{image_id}/")
    fun download(@PathVariable("image_id") imageId: Long): ResponseEntity<InputStreamResource> {
        return ResponseEntity.status(HttpStatus.OK)
            .header(HttpHeaders.CONTENT_TYPE, imageService.getContentType(imageId))
            .body(imageService.download(imageId).image)
    }
}