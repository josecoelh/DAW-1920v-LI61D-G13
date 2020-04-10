package Daw2020v.dao

import org.jdbi.v3.core.Handle
import org.jdbi.v3.core.Jdbi
import org.postgresql.ds.PGSimpleDataSource
import org.jdbi.v3.sqlobject.SqlObjectPlugin

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

        fun <T>getDao(daoKlass: Class<out T>) : T {
            return jdbi.onDemand(daoKlass)
        }



        fun executeDao(daoFunc : ()->Any):Any{
            handle.begin()
            val ret = daoFunc()
            handle.commit()
            return ret
        }
    }
}