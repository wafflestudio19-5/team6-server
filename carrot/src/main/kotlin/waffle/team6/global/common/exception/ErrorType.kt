package waffle.team6.global.common.exception

enum class ErrorType (
    val code: Int
    ) {
    INVALID_REQUEST(0),
    // error codes for BAD REQUEST
    INVALID_CATEGORY_VALUE(1),

    USER_INVALID_REQUEST(100),
    USER_INVALID_PASSWORD(101),

    PRODUCT_INVALID_REQUEST(200),
    PRODUCT_ALREADY_REQUESTED_FOR_PURCHASE(201),
    PRODUCT_ALREADY_SOLD_OUT(202),
    PRODUCT_PURCHASE_REQUEST_MISMATCH(203),


    NOT_ALLOWED(3000),
    // error codes for FORBIDDEN
    USER_NOT_ALLOWED(3100),

    PRODUCT_NOT_ALLOWED(3200),
    PRODUCT_MODIFY_NOT_ALLOWED(3201),
    PRODUCT_DELETE_NOT_ALLOWED(3202),
    PRODUCT_RESERVE_NOT_ALLOWED(3203),
    PRODUCT_RESERVE_CANCEL_NOT_ALLOWED(3204),
    PRODUCT_PURCHASE_REQUEST_LOOKUP_NOT_ALLOWED(3205),
    PRODUCT_PURCHASE_REQUEST_CONFIRM_NOT_ALLOWED(3206),
    PRODUCT_LIKE_NOT_ALLOWED(3207),
    PRODUCT_CHAT_NOT_ALLOWED(3208),

    IMAGE_NOT_ALLOWED(3300),
    IMAGE_UPDATE_NOT_ALLOWED(3301),
    IMAGE_DELETE_NOT_ALLOWED(3302),

    DATA_NOT_FOUND(4000),
    // error codes for NOT FOUND
    USER_NOT_FOUND(4100),

    PRODUCT_NOT_FOUND(4200),
    PRODUCT_PURCHASE_REQUEST_NOT_FOUND(4201),


    IMAGE_NOT_FOUND(4300),


    CONFLICT(9000),
    // error codes for CONFLICT
    USER_CONFLICT(9100),

    PRODUCT_CONFLICT(9200),

    SERVER_ERROR(10000),
    IMAGE_LOCAL_SAVE_FAIL(10001),
}