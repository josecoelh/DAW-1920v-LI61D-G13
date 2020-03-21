package Daw2020v.model

import Daw2020v.model.ShortDescription.Companion.of
import com.fasterxml.jackson.annotation.JsonProperty
import org.jdbi.v3.core.mapper.RowMapper
import org.jdbi.v3.core.statement.StatementContext
import java.sql.ResultSet
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDateTime
import java.util.*

/**
 * Data type for representing Comments in the context of this app.
 */
class Comment private constructor(val value: String,
                                  val id: UUID = UUID.randomUUID(),
                                  val date: String = SimpleDateFormat("dd/MM/yyy HH mm").format(Date(Instant.now().toEpochMilli()))) {


    class CommentMapper : RowMapper<Comment> {
        override fun map(rs: ResultSet?, ctx: StatementContext?): Comment =
                Comment(rs!!.getString("_comment"),
                        UUID.fromString(rs.getString("comment_id")),
                        rs.getString("_date"))
    }

    companion object {
        val MAX: Int = 300
        val MIN: Int = 1

        /**
         * Factory method that returns a [Comment] instance with the specified [value] and the current Date
         *
         * @param   value   The comment in question
         * @return  The corresponding [Comment] instance, or null if the [value] length is not within the admissible interval
         */
        fun of(value: String): Comment? =
                if (value.length in MIN..MAX) Comment(value)
                else null


        fun of(value: String, id: UUID, date: String): Comment? {
            return if (value.length in MIN..MAX) Comment(value, id, date)
            else null
        }

        /**
         * Overload of the function call operator to have the same behavior as the [of] function
         */
        operator fun invoke(value: String): Comment? = of(value)

        operator fun invoke(value: String, id: UUID, date: String): Comment? = of(value, id, date)
    }
}