package waffle.team6.carrot.user.repository

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import waffle.team6.carrot.user.model.User

interface UserRepository : JpaRepository<User, Long?> {
    fun findByName(name: String): User?
    fun existsByName(name: String): Boolean
    fun findAllByNameContainingOrNicknameContaining(pageable: Pageable, name: String, nickname: String): Page<User>
}