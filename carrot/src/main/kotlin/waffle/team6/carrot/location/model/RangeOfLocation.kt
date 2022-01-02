package waffle.team6.carrot.location.model

import waffle.team6.carrot.location.exception.InvalidRangeOfLocationLevelException

enum class RangeOfLocation(
    val level: Int
) {
    LEVEL_ZERO(0),
    LEVEL_ONE(1),
    LEVEL_TWO(2),
    LEVEL_THREE(3);

    companion object {
        fun from (findLevel: Int): RangeOfLocation {
            return try {
                values().first { it.level == findLevel }
            } catch (e: NoSuchElementException) {
                throw InvalidRangeOfLocationLevelException()
            }
        }
    }
}