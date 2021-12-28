package waffle.team6.carrot

import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import java.time.LocalDateTime
import javax.persistence.*

@MappedSuperclass
open class BaseTimeEntity (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    open val id: Long = 0,

    @CreatedDate
    open val createdAt: LocalDateTime = LocalDateTime.now(),

    @LastModifiedDate
    open var updatedAt: LocalDateTime = LocalDateTime.now()
)