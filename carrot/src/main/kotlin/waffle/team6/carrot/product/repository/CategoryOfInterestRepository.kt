package waffle.team6.carrot.product.repository

import org.springframework.data.jpa.repository.JpaRepository
import waffle.team6.carrot.product.model.CategoryOfInterest
import waffle.team6.carrot.user.model.User

interface CategoryOfInterestRepository: JpaRepository<CategoryOfInterest, Long?> {
    fun findAllByUser(user: User): List<CategoryOfInterest>
}