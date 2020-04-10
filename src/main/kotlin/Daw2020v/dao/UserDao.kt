package Daw2020v.dao

import Daw2020v.Authentication.User
import org.jdbi.v3.sqlobject.config.RegisterRowMapper
import org.jdbi.v3.sqlobject.statement.SqlQuery
import org.jdbi.v3.sqlobject.statement.SqlUpdate
import java.util.*

interface UserDao {


    @SqlQuery("SELECT * FROM USERS WHERE username = ?")
    @RegisterRowMapper(User.UserMapper::class)
    fun getUser(username : String): User


    @SqlUpdate("INSERT INTO USERS(username , _password) VALUES (?, ?)")
    fun createUser(username:String, password:String): Boolean
}