package waffle.team6.carrot.product.model

import waffle.team6.carrot.product.exception.InvalidForAgeValueException

enum class ForAge(
    val value: Int
) {
    ZERO_TO_SIX_MONTH(1),
    SEVEN_TO_TWELVE_MONTH(2),
    OVER_ONE_TO_TWO(3),
    THREE_TO_FIVE(4),
    SIX_TO_EIGHT(5),
    OVER_NINE(6);

    companion object {
        fun from (findValue: Int): ForAge {
            return try {
                ForAge.values().first { it.value == findValue }
            } catch (e: NoSuchElementException) {
                throw InvalidForAgeValueException()
            }
        }
    }
}