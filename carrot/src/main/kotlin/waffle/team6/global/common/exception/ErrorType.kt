package waffle.team6.global.common.exception

enum class ErrorType (
    val code: Int
    ) {
    INVALID_REQUEST(0),
    // error codes for BAD REQUEST
    USER_INVALID_REQUEST(100),

    PRODUCT_INVALID_REQUEST(200),


    NOT_ALLOWED(3000),
    // error codes for FORBIDDEN
    USER_NOT_ALLOWED(3100),

    PRODUCT_NOT_ALLOWED(3200),


    DATA_NOT_FOUND(4000),
    // error codes for NOT FOUND
    USER_NOT_FOUND(4100),

    PRODUCT_NOT_FOUND(4200),


    CONFLICT(9000),
    // error codes for CONFLICT
    USER_CONFLICT(9100),

    PRODUCT_CONFLICT(9200),

    SERVER_ERROR(10000)
}