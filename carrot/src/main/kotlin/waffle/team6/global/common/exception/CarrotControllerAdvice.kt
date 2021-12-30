package waffle.team6.global.common.exception

import com.amazonaws.AmazonServiceException
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import javax.validation.ConstraintViolationException

@RestControllerAdvice
class CarrotControllerAdvice() {
    private val logger = LoggerFactory.getLogger(this.javaClass.name)
    @ExceptionHandler(value = [DataNotFoundException::class])
    fun notfound(e: CarrotException) =
        ResponseEntity(ErrorResponse(e.errorType.code, e.errorType.name, e.detail), HttpStatus.NOT_FOUND)

    @ExceptionHandler(value = [InvalidRequestException::class])
    fun badRequest(e: CarrotException) =
        ResponseEntity(ErrorResponse(e.errorType.code, e.errorType.name, e.detail), HttpStatus.BAD_REQUEST)

    @ExceptionHandler(value = [NotAllowedException::class])
    fun notAllowed(e: CarrotException) =
        ResponseEntity(ErrorResponse(e.errorType.code, e.errorType.name, e.detail), HttpStatus.FORBIDDEN)

    @ExceptionHandler(value = [ConflictException::class])
    fun conflict(e: CarrotException) =
        ResponseEntity(ErrorResponse(e.errorType.code, e.errorType.name, e.detail), HttpStatus.CONFLICT)

    @ExceptionHandler(value = [ServerErrorException::class])
    fun serverError(e: CarrotException) =
        ResponseEntity(ErrorResponse(e.errorType.code, e.errorType.name, e.detail), HttpStatus.INTERNAL_SERVER_ERROR)

    @ExceptionHandler(value = [AmazonServiceException::class])
    fun s3Error(e: AmazonServiceException) =
        ResponseEntity(ErrorResponse(e.errorCode.toInt(), e.errorType.toString(), e.errorMessage), HttpStatus.INTERNAL_SERVER_ERROR)

    @ExceptionHandler(value = [ConstraintViolationException::class])
    fun invalidRequestParameter(e: ConstraintViolationException) =
        ResponseEntity(ErrorResponse(0, "INVALID_REQUEST_PARAMETER", e.message ?: ""), HttpStatus.BAD_REQUEST)
}