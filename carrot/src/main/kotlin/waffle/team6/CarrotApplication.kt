package waffle.team6

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import java.util.*
import javax.annotation.PostConstruct

@SpringBootApplication
@EnableJpaAuditing
@EnableFeignClients
class CarrotApplication

fun main(args: Array<String>) {
    runApplication<CarrotApplication>(*args)

    @PostConstruct
    fun started() {
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"));
    }
}
