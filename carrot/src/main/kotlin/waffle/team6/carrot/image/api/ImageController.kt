package waffle.team6.carrot.image.api

import org.springframework.core.io.InputStreamResource
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import waffle.team6.carrot.image.dto.ImageDto
import waffle.team6.carrot.image.service.ImageService
import waffle.team6.carrot.user.model.User
import waffle.team6.global.auth.CurrentUser

@RestController
@RequestMapping("/api/v1/images")
class ImageController(
    private val imageService: ImageService
) {
    @PostMapping("/")
    fun upload(@CurrentUser user: User, @RequestPart image: MultipartFile): ResponseEntity<ImageDto.Response> {
        return ResponseEntity.status(HttpStatus.CREATED).body(imageService.upload(image, user))
    }

    @GetMapping("/{image_id}/")
    fun download(@PathVariable("image_id") imageId: Long): ResponseEntity<InputStreamResource> {
        return ResponseEntity.status(HttpStatus.OK)
            .header(HttpHeaders.CONTENT_TYPE, imageService.getContentType(imageId))
            .body(imageService.download(imageId).image)
    }

    @PutMapping("/{image_id}/")
    fun update(
        @CurrentUser user: User,
        @RequestPart image: MultipartFile,
        @PathVariable("image_id") imageId: Long
    ): ResponseEntity<ImageDto.Response> {
        return ResponseEntity.status(HttpStatus.OK).body(imageService.update(image, imageId, user))
    }

    @DeleteMapping("/{image_id}/")
    fun delete(@CurrentUser user: User, @PathVariable("image_id") imageId: Long): ResponseEntity<Any> {
        imageService.delete(imageId, user)
        return ResponseEntity.ok().build()
    }
}