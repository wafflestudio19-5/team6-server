package waffle.team6.carrot.image.api

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import org.springframework.core.io.InputStreamResource
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import springfox.documentation.annotations.ApiIgnore
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
    @Operation(summary = "이미지 업로드", description = "이미지가 S3 저장소에 업로드 됩니다. 지원하는 이미지 타입:  " +
            ".bmp, .gif, .png, .jpeg, .svg, .webp, .tiff", responses = [
        ApiResponse(responseCode = "201", description = "Success Response"),
        ApiResponse(responseCode = "400", description = "images 파라미터가 없는 경우"),
        ApiResponse(responseCode = "0401", description = "이미지 타입이 아니거나 null 인 경우"),
        ApiResponse(responseCode = "10001", description = "이미지 업로드 실패한 경우")
    ])
    fun upload(
        @CurrentUser @ApiIgnore user: User,
        @RequestPart images: List<MultipartFile>
    ): ResponseEntity<ImageDto.ImageListResponse> {
        return ResponseEntity.status(HttpStatus.CREATED).body(imageService.upload(images, user))
    }

    @GetMapping("/{image_id}/")
    @Operation(summary = "이미지 다운로드", description = "S3 저장소에 업로드된 이미지를 다운로드 합니다", responses = [
        ApiResponse(responseCode = "201", description = "Success Response"),
        ApiResponse(responseCode = "4300", description = "해당 이미지가 없는 경우")
    ])
    fun download(@PathVariable("image_id") imageId: Long): ResponseEntity<InputStreamResource> {
        return ResponseEntity.status(HttpStatus.OK)
            .header(HttpHeaders.CONTENT_TYPE, imageService.getContentType(imageId))
            .body(imageService.download(imageId).image)
    }

    @PutMapping("/{image_id}/")
    @Operation(summary = "이미지 업데이트", description = "S3 저장소에 업로드된 이미지와 교체됩니다", responses = [
        ApiResponse(responseCode = "201", description = "Success Response"),
        ApiResponse(responseCode = "400", description = "images 파라미터가 없는 경우"),
        ApiResponse(responseCode = "0401", description = "이미지 타입이 아니거나 null 인 경우"),
        ApiResponse(responseCode = "3301", description = "등록자가 아닌 다른 사용자가 요청을 시도한 경우"),
        ApiResponse(responseCode = "4300", description = "해당 이미지가 없는 경우"),
        ApiResponse(responseCode = "10001", description = "이미지 업로드 실패할 경우")
    ])
    fun update(
        @CurrentUser @ApiIgnore user: User,
        @RequestPart image: MultipartFile,
        @PathVariable("image_id") imageId: Long
    ): ResponseEntity<ImageDto.ImageResponse> {
        return ResponseEntity.status(HttpStatus.OK).body(imageService.update(image, imageId, user))
    }

    @DeleteMapping("/{image_id}/")
    @Operation(summary = "이미지 삭제", description = "이미지가 S3 저장소에서 삭제됩니다", responses = [
        ApiResponse(responseCode = "201", description = "Success Response"),
        ApiResponse(responseCode = "3302", description = "등록자가 아닌 다른 사용자가 요청을 시도한 경우"),
        ApiResponse(responseCode = "4300", description = "해당 이미지가 없는 경우")
    ])
    fun delete(@CurrentUser @ApiIgnore user: User, @PathVariable("image_id") imageId: Long): ResponseEntity<Any> {
        imageService.delete(imageId, user)
        return ResponseEntity.ok().build()
    }
}