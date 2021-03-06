package Daw2020v

import Daw2020v.common.ProblemJson
import Daw2020v.common.WIKI_PATH
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestController
import javax.servlet.http.HttpServletRequest

@RestController
class BaseControllerClass {


    @ExceptionHandler(IllegalArgumentException::class, TypeCastException::class)
    fun notFound(request: HttpServletRequest) = ResponseEntity
            .badRequest()
            .contentType(MediaType.APPLICATION_PROBLEM_JSON)
            .body(ProblemJson(
                    type = WIKI_PATH,
                    title = "Not Found",
                    detail = "Resource not found",
                    status = 404,
                    location = request.requestURI
            ))
}