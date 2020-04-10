package Daw2020v.Authentication

import Daw2020v.dao.ModelDao
import Daw2020v.dao.Database
import Daw2020v.dao.UserDao
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.awt.image.DataBuffer

@Service
class AutheService @Autowired constructor() {
        private val userDao: UserDao = Database.getDao(UserDao::class.java)

        fun login(user : User) : Boolean{
               val dbUser = Database.executeDao { userDao.getUser(user.username) } as User //catch null if doesnt exist
                return dbUser.password.equals(user.password)
        }

        fun register(user: User) : Boolean{
            return Database.executeDao { userDao.createUser(user.username, user.password) } as Boolean //catch if the user already exists (database deve lançar exceçao)
        }


}