package Daw2020v.Authentication

import Daw2020v.common.model.Name
import Daw2020v.common.model.ShortDescription
import org.jdbi.v3.core.mapper.RowMapper
import org.jdbi.v3.core.statement.StatementContext
import java.sql.ResultSet
import java.util.*

data class User(val username : String, val password: String){
    class UserMapper : RowMapper<User> {
        override fun map(rs: ResultSet?, ctx: StatementContext?): User =
                User(rs!!.getString("username"), rs.getString("_password"))
    }
}