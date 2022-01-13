package waffle.team6.global.common.exception

enum class ErrorType (
    val code: Int
    ) {
    INVALID_REQUEST(0),
    // error codes for BAD REQUEST
    INVALID_CATEGORY_VALUE(1),
    INVALID_AGE_VALUE(2),
    INVALID_RANGE_OF_LOCATION_LEVEL(3),

    USER_INVALID_REQUEST(100),
    USER_INVALID_PASSWORD(101),

    PRODUCT_INVALID_REQUEST(200),
    PRODUCT_ALREADY_SOLD_OUT(201),
    PRODUCT_PURCHASE_REQUEST_MISMATCH(202),
    PRODUCT_BUMP_ONLY_ONCE_IN_A_DAY(203),

    LOCATION_INVALID_REQUEST(300),
    LOCATION_NAME_AND_CODE_MISSING(301),

    IMAGE_INVALID_REQUEST(400),
    IMAGE_INVALID_CONTENT_TYPE(401),

    PURCHASE_ORDER_INVALID_REQUEST(500),
    PURCHASE_ORDER_ALREADY_HANDLED(501),
    PURCHASE_ORDER_CONFIRMED_ONLY_WHEN_ACCEPTED(502),


    NOT_ALLOWED(3000),
    // error codes for FORBIDDEN
    USER_NOT_ALLOWED(3100),

    PRODUCT_NOT_ALLOWED(3200),
    PRODUCT_MODIFY_NOT_ALLOWED(3201),
    PRODUCT_DELETE_NOT_ALLOWED(3202),
    PRODUCT_STATUS_CHANGE_NOT_ALLOWED(3203),
    PRODUCT_LIKE_NOT_ALLOWED(3207),
    PRODUCT_CHAT_NOT_ALLOWED(3208),
    PRODUCT_HIDE_NOT_ALLOWED(3209),
    PRODUCT_SHOW_NOT_ALLOWED(3210),
    PRODUCT_BUMP_NOT_ALLOWED(3211),
    HIDDEN_PRODUCT_RETRIEVE_NOT_ALLOWED(3212),

    IMAGE_NOT_ALLOWED(3300),
    IMAGE_UPDATE_NOT_ALLOWED(3301),
    IMAGE_DELETE_NOT_ALLOWED(3302),
    IMAGE_ACCESS_NOT_ALLOWED(3303),

    PURCHASE_ORDER_NOT_ALLOWED(3400),
    PURCHASE_ORDER_DELETE_NOT_ALLOWED(3401),
    PURCHASE_ORDER_LOOKUP_NOT_ALLOWED(3402),
    PURCHASE_ORDER_HANDLE_NOT_ALLOWED(3403),
    PURCHASE_ORDER_UPDATE_NOT_ALLOWED(3404),

    DATA_NOT_FOUND(4000),
    // error codes for NOT FOUND
    USER_NOT_FOUND(4100),

    PRODUCT_NOT_FOUND(4200),
    PRODUCT_PURCHASE_REQUEST_NOT_FOUND(4201),


    IMAGE_NOT_FOUND(4300),

    LOCATION_NOT_FOUND(4400),


    CONFLICT(9000),
    // error codes for CONFLICT
    USER_CONFLICT(9100),

    PRODUCT_CONFLICT(9200),
    PRODUCT_IMAGE_CONFLICT(9201),
    PRODUCT_PURCHASE_REQUEST_CONFLICT(9202),

    SERVER_ERROR(10000),
    IMAGE_LOCAL_SAVE_FAIL(10001),
}