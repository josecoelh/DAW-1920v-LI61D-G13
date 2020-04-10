package Daw2020v.Comment

import Daw2020v.common.model.Comment
import Daw2020v.dao.ModelDao
import Daw2020v.dao.Database
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

@Service
class CommentService @Autowired constructor() {
    val modelDao: ModelDao = Database.getDao(ModelDao::class.java)

    fun getComment(issueId: UUID, commentId: UUID): Comment {
        return Database.executeDao { modelDao.getComment(issueId, commentId) } as Comment
    }

    fun addCommentToIssue(projectId: UUID, issueId: UUID, comment: Comment) {
        Database.executeDao { modelDao.addCommentToIssue(comment.value, comment.date, comment.id, issueId) }
    }

    fun deleteCommentInIssue(issueId: UUID, commentId: UUID): Boolean =
            Database.executeDao { modelDao.deleteCommentInIssue(issueId, commentId) } as Boolean

    fun getAllComments(projectId: UUID, issueId: UUID): MutableList<Comment> {
        return (Database.executeDao { modelDao.getIssueComment(issueId) } as MutableList<Comment>)
    }

}