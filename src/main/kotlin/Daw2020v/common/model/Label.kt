package Daw2020v.common.model

import com.fasterxml.jackson.annotation.JsonProperty
import org.jdbi.v3.core.mapper.RowMapper
import org.jdbi.v3.core.statement.StatementContext
import java.sql.ResultSet


data class Label(@JsonProperty("value") val identifier: String) {

    class LabelMapper : RowMapper<Label> {
        override fun map(rs: ResultSet?, ctx: StatementContext?): Label {
            return Label(rs!!.getString("_value"))
        }
    }
}

