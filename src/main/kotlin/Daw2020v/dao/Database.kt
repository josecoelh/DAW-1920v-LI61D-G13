package Daw2020v.dao

import Daw2020v.dao.ProjectDao
import Daw2020v.model.Comment
import Daw2020v.model.Issue
import Daw2020v.model.Label
import Daw2020v.model.Project
import ch.qos.logback.core.db.DataSourceConnectionSource
import org.jdbi.v3.core.Handle
import org.jdbi.v3.core.Jdbi
import org.postgresql.ds.PGSimpleDataSource
import org.springframework.stereotype.Repository
import java.util.*
import org.jdbi.v3.sqlobject.SqlObjectPlugin
import org.springframework.stereotype.Component
import java.sql.Connection

class Database {
    companion object {
        private lateinit var jdbi: Jdbi
        private val handle : Handle by lazy{
            jdbi.open() //never closed as so far in development, there is only 1 instance of handle that accesses de DB
        }

        init {
            val dataSource = PGSimpleDataSource()
            dataSource.databaseName = "postgres"
            dataSource.user = "postgres"
            dataSource.password = "1212"
            dataSource.portNumber = 5432
            jdbi = Jdbi.create(dataSource)
            jdbi.installPlugin(SqlObjectPlugin())
        }

        fun getProjectDao() : ProjectDao {
            return jdbi.onDemand(ProjectDao::class.java)
        }

        fun executeDao(daoFunc : ()->Any):Any{
            handle.begin()
            val ret = daoFunc()
            handle.commit()
            return ret
        }
    }
}