package waffle.team6

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import java.util.*
import javax.annotation.PostConstruct

@SpringBootApplication
@EnableJpaAuditing
class CarrotApplication

fun main(args: Array<String>) {
    runApplication<CarrotApplication>(*args)

    @PostConstruct
    fun started() {
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"));
    }
}
