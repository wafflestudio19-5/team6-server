package waffle.team6.global.common.exception

import com.google.gson.Gson
import org.apache.http.HttpStatus
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.web.access.AccessDeniedHandler
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class CustomAccessDeniedHandler : AccessDeniedHandler{
    override fun handle(
        request: HttpServletRequest,
        response: HttpServletResponse,
        accessDeniedException: AccessDeniedException
    ) {
        val gson = Gson()
        val errorResponse = ErrorResponse(ErrorType.USER_KAKAO_NOT_ALLOWED.code, ErrorType.USER_KAKAO_NOT_ALLOWED.name)
        response.contentType = "application/json";
        response.characterEncoding = "utf-8";
        response.writer.write(gson.toJson(errorResponse))
        response.status = HttpStatus.SC_FORBIDDEN

        println("response = $response")
    }
}