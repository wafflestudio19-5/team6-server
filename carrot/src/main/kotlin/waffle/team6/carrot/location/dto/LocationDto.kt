package waffle.team6.carrot.location.dto

import waffle.team6.carrot.location.model.Location
import waffle.team6.carrot.location.model.RangeOfLocation

class LocationDto {
    data class LocationResponse(
        val exists: Boolean,
        val name: String?,
        val code: Long?,
        val levelZeroCount: Int?,
        val levelOneCount: Int?,
        val levelTwoCount: Int?,
        val levelThreeCount: Int?
    ) {
        constructor(locationEntity: Location?): this(
            exists = locationEntity != null,
            name = locationEntity?.name,
            code = locationEntity?.code,
            levelZeroCount = locationEntity?.levelZero?.size,
            levelOneCount = locationEntity?.levelOne?.size,
            levelTwoCount = locationEntity?.levelTwo?.size,
            levelThreeCount = locationEntity?.levelThree?.size
        )
    }
}