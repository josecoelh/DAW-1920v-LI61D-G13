package Daw2020v.Authentication

import Daw2020v.dao.Database
import Daw2020v.dao.UserDao
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

@Service
class AuthService @Autowired constructor() {
    private val userDao: UserDao = Database.getDao(UserDao::class.java)

    fun decodeUsername(codedUser: String) = String(Base64.getDecoder().decode(codedUser), Charsets.UTF_8).split(":")[0]

    fun verifyCredentials(codedUser: String): Boolean {
        return  (Database.executeDao { userDao.getCodedUser(decodeUsername(codedUser))} as User?)?.codedUser == codedUser
    }

    fun register(username: String, hashedUser: String): Boolean {
        return Database.executeDao { userDao.createUser(username, hashedUser) } as Boolean //catch if the user already exists (database deve lançar exceçao)
    }
}