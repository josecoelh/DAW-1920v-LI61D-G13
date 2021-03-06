package Daw2020v.Authentication

import Daw2020v.BaseServiceClass
import Daw2020v.common.WrongCredentialsException
import Daw2020v.dao.Database
import Daw2020v.dao.UserDao
import org.postgresql.util.PSQLException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

@Service
class AuthService @Autowired constructor() : BaseServiceClass() {
    private val userDao: UserDao = Database.getDao(UserDao::class.java)


    fun verifyCredentials(codedUser: String): Boolean {
        return (Database.executeDao { userDao.getCodedUser(decodeUsername(codedUser)) } as User?)?.codedUser == codedUser.split("Basic ")[1]
    }

    fun register(username: String, hashedUser: String): Boolean {
        return Database.executeDao { userDao.createUser(username, hashedUser) } as Boolean //catch if the user already exists (database deve lançar exceçao)
    }
}