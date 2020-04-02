package Daw2020v.Comment

import Daw2020v.common.model.Comment
import Daw2020v.dao.Dao
import Daw2020v.dao.Database
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

@Service
class CommentService @Autowired constructor() {
    val dao: Dao = Database.getProjectDao()

    fun getComment(issueId: UUID, commentId: UUID): Comment {
        return Database.executeDao { dao.getComment(issueId, commentId) } as Comment
    }

    fun addCommentToIssue(projectId: UUID, issueId: UUID, comment: Comment) {
        Database.executeDao { dao.addCommentToIssue(comment.value, comment.date, comment.id, issueId) }
    }

    fun deleteCommentInIssue(issueId: UUID, commentId: UUID): Boolean =
            Database.executeDao { dao.deleteCommentInIssue(issueId, commentId) } as Boolean

    fun getAllComments(projectId: UUID, issueId: UUID): MutableList<Comment> {
        return (Database.executeDao { dao.getIssueComment(issueId) } as MutableList<Comment>)
    }

}