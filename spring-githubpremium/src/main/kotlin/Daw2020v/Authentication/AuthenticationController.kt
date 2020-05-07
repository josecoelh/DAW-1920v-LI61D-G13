package Daw2020v.Authentication

import Daw2020v.BaseControllerClass
import Daw2020v.RequireSession
import Daw2020v.common.*
import org.postgresql.util.PSQLException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.http.MediaType
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpSession

const val USER_SESSION: String = "USER_SESSION"

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
    @Authorized
    fun login(session: HttpSession, @RequestHeader("Authorization") codedUser: String): ResponseEntity<Boolean> {
        val coded = codedUser.split(" ")[1]
        val username = authService.decodeUsername(coded)
        if (authService.verifyCredentials(coded)) {
            session.setAttribute(USER_SESSION, username)
            return ResponseEntity.ok(true)
        } else return ResponseEntity.status(HttpStatus.FORBIDDEN).body(false)

    }

    @PutMapping(path = arrayOf("/register"))
    @Authorized
    fun register(@RequestHeader("Authorization") codedUser: String, session: HttpSession): ResponseEntity<Boolean> {
        val coded = codedUser.split(" ")[1]
        val username = authService.decodeUsername(coded)
        if (authService.register(username, coded)) {
            session.setAttribute(USER_SESSION, username)
            return ResponseEntity.ok(true)
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(false)
    }

    @PutMapping(path = arrayOf("/logout"))
    @RequireSession
    fun logout(request: HttpServletRequest): ResponseEntity<Boolean> {
        request.session.invalidate()
        return ResponseEntity.ok(true)
    }
}