package waffle.team6.carrot.product.repository

import org.springframework.data.jpa.repository.JpaRepository
import waffle.team6.carrot.product.model.CategoryOfInterest

interface CategoryOfInterestRepository: JpaRepository<CategoryOfInterest, Long?> {
}