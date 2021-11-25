package waffle.team6.carrot.image

import org.springframework.core.io.InputStreamResource
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.nio.file.Files
import java.time.LocalDateTime
import kotlin.io.path.Path

@RestController
@RequestMapping("/api/v1/images")
class ImageController {
    @PostMapping("/")
    @ResponseStatus(HttpStatus.CREATED)
    fun upload(@RequestPart files: List<MultipartFile>): ResponseEntity<List<String>> {
        val images: MutableList<String> = mutableListOf()
        for (file in files) {
            val name: String? = file.originalFilename
            if (name != null) {
                val pathName = "/tmp/${LocalDateTime.now()}$name"
                val path  = File(pathName)
                file.transferTo(path)
                images.add(pathName)
            }
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(images)
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