package Daw2020v.service

import Daw2020v.dao.Database
import Daw2020v.dao.ProjectDao
import Daw2020v.model.Comment
import Daw2020v.model.Issue
import Daw2020v.model.Label
import Daw2020v.model.Project
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.lang.IllegalArgumentException
import java.util.*

@Service
class ProjectService @Autowired constructor(){
    val projectDao: ProjectDao = Database.getProjectDao()



    fun insertProject(project: Project) : Boolean {
        if(project.name == null || project.shortDesc == null) throw IllegalArgumentException("Bad project")
        val res = Database.executeDao {projectDao.insertProject(project.id, project.name!!.value, project.shortDesc!!.text)} as Boolean
        if(res) return true
        //TODO se der erro?
        return false
    }

    fun getProject(projectId : UUID) : Project {
        val project : Project = Database.executeDao { projectDao.getProject(projectId) } as Project
        val projectLabels : MutableList<Label> = Database.executeDao { projectDao.getProjectLabels(projectId) } as MutableList<Label>
        val issues : List<Issue> = Database.executeDao { projectDao.getProjectIssues(projectId) } as List<Issue>
        project.allowedLabels= projectLabels
        issues.forEach{
            it.allowedLabels = projectLabels;
            it.addComment(*(Database.executeDao { projectDao.getIssueComment(it.id) } as List<Comment>).toTypedArray())
            it.addLabel(*(Database.executeDao { projectDao.getIssueLabel(it.id) } as List<Label>).toTypedArray())
            project.addIssue(it)
        }
        return project
    }


    fun putProject(projectId : UUID, project: Project) : UUID {
        Database.executeDao { projectDao.putProject(project.name!!.value,project.shortDesc!!.text, projectId) }
        return projectId
    }

    fun putLabels(projectId : UUID, labels: Array<Label>) : UUID {
        labels.forEach { Database.executeDao { projectDao.putLabel(projectId,it.identifier) } }
        return projectId
    }

    fun putIssue(projectId: UUID, issue: Issue) : UUID {
        Database.executeDao { projectDao.putIssue(projectId, issue.id.toString(), issue.name!!.value,issue.state.toString()) }
        return projectId
    }

    fun deleteIssue( issueId : UUID): Boolean {
        Database.executeDao { projectDao.deleteIssueComment(issueId) }
        Database.executeDao { projectDao.deleteIssueLabels(issueId) }
        return Database.executeDao { projectDao.deleteIssue(issueId) } as Boolean
    }

    fun deleteAllowedLabel(projectId: UUID, label: String): Boolean {
        Database.executeDao { projectDao.deleteLabel(label, projectId)}
        Database.executeDao { projectDao.deleteAllowedLabel(projectId,label) }
        return true
    }

    fun deleteProject(projectId: UUID): Boolean {
        Database.executeDao { projectDao.deleteAllProjectIssueLabels(projectId)}
        Database.executeDao { projectDao.deleteAllowedLabels(projectId)}
        Database.executeDao { projectDao.deleteIssueCommentsFromProject(projectId)}
        Database.executeDao { projectDao.deleteProjectIssues(projectId) }
        Database.executeDao { projectDao.deleteProject(projectId) }
        return true
    }


    fun putLabelinIssue(projectId: UUID, issueId: UUID, label: String): Boolean {
        val ProjectLabel : Label? = Database.executeDao { projectDao.getProjectLabel(projectId, label)  } as Label?
        if(ProjectLabel != null){
            return Database.executeDao { projectDao.putLabelInIssue(issueId,label) } as Boolean
        }
        return false
    }

    fun deleteLabelInIssue(issueId: UUID, label: String): Boolean {
        return Database.executeDao { projectDao.deleteLabelInIssue(issueId,label) } as Boolean
    }

    fun updateIssue(projectId: UUID, issueId: UUID, issue: Issue): Boolean = projectDao!!.updateIssue(projectId,issueId,issue)

    fun deleteCommentInIssue(projectId: UUID, issueId: UUID, commentId: UUID): Boolean = projectDao!!.deleteCommentInIssue(projectId,issueId,commentId)

    fun getIssue(projectId: UUID, issueId: UUID): Boolean = projectDao!!.getIssue(projectId,issueId)

    fun addCommentToIssue(projectId: UUID, issueId: UUID, comment: Comment) : Boolean = projectDao.addCommentToIssue(projectId, issueId, comment)
}