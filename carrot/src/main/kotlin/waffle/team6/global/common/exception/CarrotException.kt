package waffle.team6.global.common.exception

import java.lang.RuntimeException

abstract class CarrotException(val errorType: ErrorType, val detail: String = "") : RuntimeException(errorType.name)

abstract class InvalidRequestException(errorType: ErrorType, detail: String = "") : CarrotException(errorType, detail)
abstract class DataNotFoundException(errorType: ErrorType, detail: String = "") : CarrotException(errorType, detail)
abstract class NotAllowedException(errorType: ErrorType, detail: String = "") : CarrotException(errorType, detail)
abstract class ConflictException(errorType: ErrorType, detail: String = "") : CarrotException(errorType, detail)
abstract class ServerErrorException(errorType: ErrorType, detail: String = "") : CarrotException(errorType, detail)