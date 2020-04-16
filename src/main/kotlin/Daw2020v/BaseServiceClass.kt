package Daw2020v

import Daw2020v.common.ForbiddenResourceException
import Daw2020v.dao.Database
import Daw2020v.dao.ModelDao
import org.springframework.stereotype.Service
import java.util.*

@Service
class BaseServiceClass {
    val modelDao: ModelDao = Database.getDao(ModelDao::class.java)

    fun verifyProjectOwnership(projectId: UUID, username: String) {
        if((Database.executeDao { modelDao.getProjectUser(projectId,username) } as String?) == null) {
            throw ForbiddenResourceException()
        }
    }
}