package waffle.team6.carrot.product.repository

import org.springframework.data.jpa.repository.JpaRepository
import waffle.team6.carrot.product.model.Like

interface LikeRepository: JpaRepository<Like, Long?> {
}