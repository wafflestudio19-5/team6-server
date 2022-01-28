package waffle.team6.carrot.product.repository

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import waffle.team6.carrot.product.model.Like
import waffle.team6.carrot.product.model.Product
import waffle.team6.carrot.user.model.User

interface LikeRepository: JpaRepository<Like, Long?> {

    fun findAllByUser(user: User): List<Like>

    fun findAllByUserId(pageable: Pageable, userId: Long): Page<Like>

    fun existsByUserAndProduct(user: User, product: Product): Boolean
}