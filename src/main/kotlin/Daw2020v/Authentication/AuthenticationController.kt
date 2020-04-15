package Daw2020v.Authentication

import Daw2020v.BaseConstrollerClass
import Daw2020v.RequireSession
import Daw2020v.common.*
import org.postgresql.util.PSQLException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.view.RedirectView
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpSession

const val USER_SESSION: String = "USER_SESSION"

@RequestMapping(HOME)
@RestController
class AuthenticationController @Autowired constructor(val authService: AuthService) : BaseConstrollerClass() {

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
    fun login(session:HttpSession, @RequestHeader("Authorization") codedUser : String): RedirectView {
        val coded = codedUser.split(" ")[1]
        val username = authService.decodeUsername(coded)
        if(authService.verifyCredentials(coded)) {
            session.setAttribute(USER_SESSION, username)
            return RedirectView(ALL_PROJECTS)
        }
        throw WrongCredentialsException()
    }

    @GetMapping(path = arrayOf("/login"))
    fun loginForm(): ResponseEntity<String> = ResponseEntity.ok("imagina um form de login ")

    @PutMapping(path = arrayOf("/register"))
    @Authorized
    fun register(@RequestHeader("Authorization") codedUser : String, session:HttpSession): RedirectView {
        val coded = codedUser.split(" ")[1]
        val username = authService.decodeUsername(coded)
        if (authService.register(username, coded)) {
            session.setAttribute(USER_SESSION, username)
            return RedirectView(ALL_PROJECTS)
        }
        return RedirectView(LOGIN)
    }

    @PutMapping(path = arrayOf("/logout"))
    @RequireSession
    fun logout(request: HttpServletRequest): RedirectView {
        request.session.invalidate()
        return RedirectView(LOGIN)
    }
}