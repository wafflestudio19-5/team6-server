package waffle.team6.carrot.image.api

import org.springframework.core.io.InputStreamResource
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import waffle.team6.carrot.image.exception.ImageNotFoundException
import waffle.team6.carrot.image.service.ImageService
import java.nio.file.Files
import kotlin.io.path.Path

@RestController
@RequestMapping("/api/v1/images")
class ImageController(
    private val imageService: ImageService
) {
    @PostMapping("/")
    fun upload(@RequestPart files: List<MultipartFile>): ResponseEntity<List<String>> {
        return ResponseEntity.status(HttpStatus.CREATED).body(imageService.upload(files))
    }

    @GetMapping("/")
    @ResponseStatus(HttpStatus.OK)
    fun download(@RequestParam(required = true) path: String): ResponseEntity<InputStreamResource> {
        return try {
            val contentType = Files.probeContentType(Path(path))
            val resource = InputStreamResource(Files.newInputStream(Path(path)))
            ResponseEntity.status(HttpStatus.OK).header(HttpHeaders.CONTENT_TYPE, contentType).body(resource)
        } catch (e: java.nio.file.NoSuchFileException) {
            throw ImageNotFoundException()
        }
    }

}