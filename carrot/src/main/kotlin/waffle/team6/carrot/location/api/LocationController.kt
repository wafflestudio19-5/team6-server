package waffle.team6.carrot.location.api

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import waffle.team6.carrot.location.dto.LocationDto
import waffle.team6.carrot.location.exception.LocationMissingParameterException
import waffle.team6.carrot.location.service.LocationService

@RestController
@RequestMapping("/api/v1/location")
class LocationController(
    private val locationService: LocationService
) {
    @GetMapping("/")
    fun findLocation(
        @RequestParam(required = false) name: String?,
        @RequestParam(required = false) code: Long?
    ): ResponseEntity<LocationDto.LocationResponse> {
        return if (name != null) ResponseEntity.ok().body(locationService.findLocationByName(name))
        else if (code != null) ResponseEntity.ok().body(locationService.findLocationByCode(code))
        else throw LocationMissingParameterException()
    }
}