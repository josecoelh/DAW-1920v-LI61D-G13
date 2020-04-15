package Daw2020v.Comment

import Daw2020v.common.ForbiddenResourceException
import Daw2020v.common.model.Comment
import Daw2020v.dao.ModelDao
import Daw2020v.dao.Database
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

@Service
class CommentService @Autowired constructor() {
    val modelDao: ModelDao = Database.getDao(ModelDao::class.java)

    fun getComment(issueId: UUID, commentId: UUID, username: String): Comment {
        return Database.executeDao { modelDao.getComment(issueId, commentId,username) } as Comment
    }

    fun addCommentToIssue(projectId: UUID, issueId: UUID, comment: Comment,username: String) {
        Database.executeDao { modelDao.addCommentToIssue(comment.value, comment.date, comment.id, issueId, username ) }
    }

    fun deleteCommentInIssue(issueId: UUID, commentId: UUID,username: String): Boolean =
            Database.executeDao { modelDao.deleteCommentInIssue(issueId, commentId,username) } as Boolean

    fun getAllComments(projectId: UUID, issueId: UUID, username: String): MutableList<Comment> {
        verifyProjectOwnership(projectId,username)
        return (Database.executeDao { modelDao.getIssueComment(issueId) } as MutableList<Comment>)
    }

    fun verifyProjectOwnership(projectId: UUID,username: String) {
        if((Database.executeDao { modelDao.getProjectUser(projectId,username) } as String?) == null) {
            throw ForbiddenResourceException()
        }
    }

}