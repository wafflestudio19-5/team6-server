package waffle.team6.carrot.image.repository

import org.springframework.data.jpa.repository.JpaRepository
import waffle.team6.carrot.image.model.Image

interface ImageRepository:JpaRepository<Image, Long?> {
}