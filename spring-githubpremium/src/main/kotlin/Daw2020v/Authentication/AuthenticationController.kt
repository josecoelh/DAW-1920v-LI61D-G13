package Daw2020v.Authentication

import Daw2020v.BaseControllerClass
import Daw2020v.common.*
import org.postgresql.util.PSQLException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.http.MediaType
import javax.servlet.http.HttpServletRequest


@RequestMapping(HOME)
@RestController
class AuthenticationController @Autowired constructor(val authService: AuthService) : BaseControllerClass() {

    @ExceptionHandler(PSQLException::class)
     fun duplicateUser(request: HttpServletRequest) = ResponseEntity
             .badRequest()
             .contentType(MediaType.APPLICATION_PROBLEM_JSON)
             .body(ProblemJson(
                     type = WIKI_PATH,
                     title = "Unavailable username",
                     detail = "Username already taken",
                     status = 401,
                     location = request.requestURI
             ))

    @PutMapping(path = arrayOf("/login"))
    fun login( @RequestHeader("Authorization") codedUser: String): ResponseEntity<Boolean> {
        val username = authService.decodeUsername(codedUser)
        if (authService.verifyCredentials(codedUser)) {
            return ResponseEntity.ok(true)
        } else return ResponseEntity.status(HttpStatus.FORBIDDEN).header("Content-Type", "application/json").body(false)

    }

    @PutMapping(path = arrayOf("/register"))
    fun register(@RequestHeader("Authorization") codedUser: String): ResponseEntity<Boolean> {
        val username = authService.decodeUsername(codedUser)
        if (authService.register(username, codedUser.split("Basic ")[1])) {
            return ResponseEntity.ok(true)
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).header("Content-Type", "application/json").body(false)
    }
}