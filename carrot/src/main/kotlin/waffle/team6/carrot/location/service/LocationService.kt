package waffle.team6.carrot.location.service

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import waffle.team6.carrot.location.dto.LocationDto
import waffle.team6.carrot.location.exception.LocationNotFoundException
import waffle.team6.carrot.location.model.RangeOfLocation
import waffle.team6.carrot.location.repository.LocationRepository

@Service
class LocationService(
    private val locationRepository: LocationRepository
) {
    fun findLocationByName(name: String): LocationDto.LocationResponse {
        return LocationDto.LocationResponse(locationRepository.findByName(name))
    }

    fun findLocationByCode(code: Long): LocationDto.LocationResponse {
        return LocationDto.LocationResponse(locationRepository.findByIdOrNull(code))
    }

    fun findAdjacentLocationsByCode(code: Long, rangeOfLocation: RangeOfLocation): List<String> {
        val location = locationRepository.findByIdOrNull(code) ?: throw LocationNotFoundException()
        val result = mutableListOf(location.name)
        if (rangeOfLocation.level >= RangeOfLocation.LEVEL_ZERO.level) result.addAll(location.levelZero.map { it.name })
        if (rangeOfLocation.level >= RangeOfLocation.LEVEL_ONE.level) result.addAll(location.levelOne.map { it.name })
        if (rangeOfLocation.level >= RangeOfLocation.LEVEL_TWO.level) result.addAll(location.levelTwo.map { it.name })
        if (rangeOfLocation.level >= RangeOfLocation.LEVEL_THREE.level) result.addAll(location.levelThree.map { it.name })
        return result.toList()
    }
}