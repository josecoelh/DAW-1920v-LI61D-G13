package Daw2020v.Authentication

import Daw2020v.BaseConstrollerClass
import Daw2020v.common.HOME
import Daw2020v.common.Links
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.view.RedirectView
import javax.servlet.http.HttpSession

const val USER_SESSION: String = "USER_SESSION"

@RequestMapping(HOME)
@RestController
class AuthenticationController @Autowired constructor(val authService: AutheService) : BaseConstrollerClass() {

    @PutMapping(path = arrayOf("/login"))
    fun login(@RequestBody user: User, session: HttpSession): RedirectView {
        if (authService.login(user)) {
            session.setAttribute(USER_SESSION, user.username)
            return RedirectView(Links.ALL_PROJECTS)
        }
        return RedirectView("$HOME/login")
    }

    @GetMapping(path = arrayOf("/login"))
    fun loginForm(): ResponseEntity<String> = ResponseEntity.ok("imagina um form de login ")

    @PutMapping(path = arrayOf("/register"))
    fun register(@RequestBody user: User, session: HttpSession): RedirectView {
        if (authService.register(user)) session.setAttribute(USER_SESSION, user.username)
        return RedirectView(Links.ALL_PROJECTS)
    }

    @PutMapping(path = arrayOf("/logout"))
    fun logout(@RequestBody user: User, session: HttpSession): RedirectView {
        session.invalidate()
        return RedirectView("login")
    }
}