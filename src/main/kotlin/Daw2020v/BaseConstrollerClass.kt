package Daw2020v

import Daw2020v.Authentication.USER_SESSION
import Daw2020v.common.HOME
import Daw2020v.common.ProblemJson
import Daw2020v.common.WIKI_PATH
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.HandlerInterceptor
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@RestController
class BaseConstrollerClass(){



    @ExceptionHandler(IllegalArgumentException::class, TypeCastException::class)
    fun notFound() = ResponseEntity
            .badRequest()
            .contentType(MediaType.APPLICATION_PROBLEM_JSON)
            .body(ProblemJson(
                    type = WIKI_PATH,
                    title = "Not Found",
                    detail = "Resource not found",
                    status = 404
            ))
}