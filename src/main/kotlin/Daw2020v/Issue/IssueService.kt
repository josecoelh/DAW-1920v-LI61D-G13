package Daw2020v.Issue

import Daw2020v.common.BadIssueException
import Daw2020v.common.ForbiddenResourceException
import Daw2020v.common.LabelNotAllowedException
import Daw2020v.common.LabelRepeatedException
import Daw2020v.common.model.Comment
import Daw2020v.common.model.Issue
import Daw2020v.dao.Database
import Daw2020v.dao.ModelDao
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

/**
 * This class handles errors and makes calls to the DAO
 */
@Service
class IssueService @Autowired constructor() {
    val modelDao: ModelDao = Database.getDao(ModelDao::class.java)

    fun getIssue(projectId: UUID, issueId: UUID, username: String): Issue {
        val issue = Database.executeDao { modelDao.getIssue(projectId, username, issueId) } as Issue
        issueFinalizer(projectId,issue, username)
        return issue
    }

    fun getAllIssues(projectId: UUID,username : String): List<Issue> {
        val issues: List<Issue> = Database.executeDao { modelDao.getProjectIssues(projectId,username) } as List<Issue>
        issues.forEach {
            issueFinalizer(projectId,it, username)
        }
        return issues
    }



    fun issueFinalizer(projectId: UUID, issue: Issue, username: String){
        issue.allowedLabels = Database.executeDao { modelDao.getProjectLabels(projectId,username) } as MutableList<String>;
        issue.addComment(*(Database.executeDao { modelDao.getIssueComment(issue.id) } as List<Comment>).toTypedArray())
        issue.addLabel(*(Database.executeDao { modelDao.getIssueLabels(projectId, issue.id) } as List<String>).toTypedArray())
    }

    fun createIssue(projectId: UUID, issue: Issue,username: String): Issue {
        verifyProjectOwnership(projectId,username)
        if (issue.name == null) throw BadIssueException()
        Database.executeDao { modelDao.createIssue(projectId, issue.id, issue.name!!.value) }
        return getIssue(projectId, issue.id,username)
    }

    fun deleteIssue(projectId: UUID, issueId: UUID,username: String): Boolean {
        verifyProjectOwnership(projectId,username)
        Database.executeDao { modelDao.deleteIssueComment(issueId) }
        Database.executeDao { modelDao.deleteIssueLabels(issueId) }
        return Database.executeDao { modelDao.deleteIssue(issueId) } as Boolean
    }

    fun putLabelInIssue(projectId: UUID, issueId: UUID, label: String,username: String): String {
        val issue = Database.executeDao { modelDao.getIssue(projectId, username,issueId) } as Issue
        if (!issue.allowedLabels.contains(label)) throw LabelNotAllowedException()
        if (issue.labels.contains(label)) throw LabelRepeatedException()
        Database.executeDao { modelDao.putLabelInIssue(issueId, label) }
        return label
    }

    fun deleteLabelInIssue(projectId: UUID, issueId: UUID, label: String,username: String) {
        verifyProjectOwnership(projectId,username)
        Database.executeDao { modelDao.deleteLabelInIssue(issueId, label) }
    }

    fun updateIssue(projectId: UUID, issueId: UUID, issue: IssueInputModel,username: String): Issue {
        if (issue.name == null && issue.state == null) throw BadIssueException()
        if (issue.name == null) {
            Database.executeDao { modelDao.changeIssueState(projectId, username,issueId, issue.state.toString()) }
        } else {
            if (issue.state == null) {
                Database.executeDao { modelDao.changeIssueName(projectId, username,issueId, issue.name.value) }
            } else {
                Database.executeDao { modelDao.updateIssue(projectId, issueId, issue.name.value, issue.state.toString()) }
            }
        }
        return getIssue(projectId, issueId, username)
    }
    fun verifyProjectOwnership(projectId: UUID,username: String) {
        if((Database.executeDao { modelDao.getProjectUser(projectId,username) } as String?) == null) {
            throw ForbiddenResourceException()
        }
    }
}