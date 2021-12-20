package waffle.team6.carrot

import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping

@Controller
class PingTestController {
    @GetMapping("/ping-test/")
    fun pingTest(): ResponseEntity<String> {
        return ResponseEntity.ok("ping test")
    }
}