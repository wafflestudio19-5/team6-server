package waffle.team6.carrot.user.repository

import org.springframework.data.jpa.repository.JpaRepository
import waffle.team6.carrot.user.model.BuyerProfile

interface BuyerProfileRepository: JpaRepository<BuyerProfile, Long?>