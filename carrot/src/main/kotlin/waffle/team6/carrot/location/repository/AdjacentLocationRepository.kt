package waffle.team6.carrot.location.repository

import org.springframework.data.jpa.repository.JpaRepository
import waffle.team6.carrot.location.model.AdjacentLocation

interface AdjacentLocationRepository: JpaRepository<AdjacentLocation, Long?> {
}