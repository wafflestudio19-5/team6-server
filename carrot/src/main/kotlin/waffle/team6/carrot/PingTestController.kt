package waffle.team6.carrot

import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping

@Controller
class PingTestController {
    @Operation(summary = "핑테스트", description = "서버 연결 확인을 위한 핑테스트입니다")
    @GetMapping("/ping-test/")
    fun pingTest(): ResponseEntity<String> {
        return ResponseEntity.ok("ping test")
    }
}