package waffle.team6.carrot.location.repository

import org.springframework.data.jpa.repository.JpaRepository
import waffle.team6.carrot.location.model.Location

interface LocationRepository: JpaRepository<Location, Long?> {
    fun findByName(name: String): Location?
}