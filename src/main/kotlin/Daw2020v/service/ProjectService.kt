package Daw2020v.service

import Daw2020v.dao.Database
import Daw2020v.dao.ProjectDao
import Daw2020v.dtos.IssueInputModel
import Daw2020v.dtos.ProjectInputModel
import Daw2020v.model.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.lang.IllegalArgumentException
import java.util.*

@Service
class ProjectService @Autowired constructor() {
    val projectDao: ProjectDao = Database.getProjectDao()


    fun getAllProjects(): List<Project>{
        return  projectDao.getAllProjects()
    }

    fun insertProject(project: Project): Boolean {
        if (project.name == null || project.shortDesc == null) throw IllegalArgumentException("Bad project")
        return Database.executeDao { projectDao.insertProject(project.id, project.name!!.value, project.shortDesc!!.text) } as Boolean
    }

    fun insertIssue(projectId: UUID, issue: Issue): Boolean {
        if(issue.name == null) throw IllegalArgumentException("Bad issue")
        return Database.executeDao { projectDao.putIssue(projectId,issue.id, issue.name!!.value) } as Boolean
    }

    fun getProject(projectId: UUID): Project {
        val project: Project = Database.executeDao { projectDao.getProject(projectId) } as Project
        val projectLabels: MutableList<Label> = Database.executeDao { projectDao.getProjectLabels(projectId) } as MutableList<Label>
        val issues: List<Issue> = Database.executeDao { projectDao.getProjectIssues(projectId) } as List<Issue>
        project.allowedLabels = projectLabels
        issues.forEach {
            it.allowedLabels = projectLabels;
            it.addComment(*(Database.executeDao { projectDao.getIssueComment(it.id) } as List<Comment>).toTypedArray())
            it.addLabel(*(Database.executeDao { projectDao.getIssueLabels(projectId,it.id) } as List<Label>).toTypedArray())
            project.addIssue(it)
        }
        return project
    }


    fun putProject(projectId: UUID, project: ProjectInputModel): Boolean {
        Database.executeDao { projectDao.putProject(project.name!!.value, project.description!!.text, projectId) }
        if (project.name == null && project.description == null) throw IllegalArgumentException("cant update with every field null dumbass")
        if (project.name == null) {
            return Database.executeDao { projectDao.changeProjectDescription(project.description!!.text, projectId) } as Boolean
        } else {
            if (project.description == null) {
                return Database.executeDao { projectDao.changeProjectName(project.name!!.value,projectId) } as Boolean
            } else {
                return Database.executeDao { projectDao.putProject(project.name.value, project.description!!.text,projectId) } as Boolean
            }
        }
    }

    fun putLabels(projectId: UUID, labels: Array<Label>): UUID {
        labels.forEach { Database.executeDao { projectDao.putLabel(projectId, it.identifier) } }
        return projectId
    }

    fun createIssue(projectId: UUID, issue: Issue): UUID {
        Database.executeDao { projectDao.createIssue(projectId, issue.id, issue.name!!.value) }
        return projectId
    }

    fun deleteIssue(issueId: UUID): Boolean {
        Database.executeDao { projectDao.deleteIssueComment(issueId) }
        Database.executeDao { projectDao.deleteIssueLabels(issueId) }
        return Database.executeDao { projectDao.deleteIssue(issueId) } as Boolean
    }

    fun deleteAllowedLabel(projectId: UUID, label: String): Boolean {
        Database.executeDao { projectDao.deleteLabel(label, projectId) }
        Database.executeDao { projectDao.deleteAllowedLabel(projectId, label) }
        return true
    }

    fun deleteProject(projectId: UUID): Boolean {
        Database.executeDao { projectDao.deleteAllProjectIssueLabels(projectId) }
        Database.executeDao { projectDao.deleteAllowedLabels(projectId) }
        Database.executeDao { projectDao.deleteIssueCommentsFromProject(projectId) }
        Database.executeDao { projectDao.deleteProjectIssues(projectId) }
        Database.executeDao { projectDao.deleteProject(projectId) }
        return true
    }


    fun putLabelinIssue(projectId: UUID, issueId: UUID, label: String): Boolean {
        val ProjectLabel: Label? = Database.executeDao { projectDao.getProjectLabel(projectId, label) } as Label?
        if (ProjectLabel != null) {
            return Database.executeDao { projectDao.putLabelInIssue(issueId, label) } as Boolean
        }
        return false
    }

    fun deleteLabelInIssue(issueId: UUID, label: String): Boolean {
        return Database.executeDao { projectDao.deleteLabelInIssue(issueId, label) } as Boolean
    }

    fun updateIssue(projectId: UUID, issueId: UUID, issue: IssueInputModel): Boolean {
        if (issue.name == null && issue.state == null) throw IllegalArgumentException("cant update with every field null dumbass")
        if (issue.name == null) {
            return Database.executeDao { projectDao.changeIssueState(projectId, issueId, issue.state.toString()) } as Boolean
        } else {
            if (issue.state == null) {
                return Database.executeDao { projectDao.changeIssueName(projectId, issueId, issue.name.value) } as Boolean
            } else {
                return Database.executeDao { projectDao.putIssue(projectId, issueId, issue.name.value, issue.state.toString()) } as Boolean
            }
        }
    }

    fun deleteCommentInIssue(issueId: UUID, commentId: UUID): Boolean =
            Database.executeDao { projectDao.deleteCommentInIssue(issueId, commentId) } as Boolean


    fun getIssue(projectId: UUID, issueId: UUID): Issue {
        val issue = Database.executeDao { projectDao.getIssue(projectId, issueId) } as Issue
            issue.allowedLabels = Database.executeDao { projectDao.getProjectLabels(projectId) } as MutableList<Label>;
            issue.addComment(*(Database.executeDao { projectDao.getIssueComment(issue.id) } as List<Comment>).toTypedArray())
            issue.addLabel(*(Database.executeDao { projectDao.getIssueLabels(projectId,issue.id) } as List<Label>).toTypedArray())
        return issue
    }

    fun addCommentToIssue(projectId: UUID, issueId: UUID, comment: Comment): Boolean =
            Database.executeDao { projectDao.addCommentToIssue(comment.value, comment.date, comment.id, issueId) } as Boolean

    fun changeIssueState(projectId: UUID, issueId: UUID, state: IssueState) : Boolean{
        return Database.executeDao { projectDao.changeIssueState(projectId, issueId, state.name) } as Boolean
    }

    fun getComment(issueId: UUID, commentId: UUID): Comment {
        return Database.executeDao { projectDao.getComment(issueId,commentId) } as Comment
    }

    fun getAllComments(projectId: UUID, issueId: UUID): MutableList<Comment> {
        return (Database.executeDao { projectDao.getIssueComment(issueId) } as MutableList<Comment>)
    }

    fun getAllIssues(projectId: UUID): List<Issue> {
        val issues: List<Issue> = Database.executeDao { projectDao.getProjectIssues(projectId) } as List<Issue>
        issues.forEach {
            it.allowedLabels =  Database.executeDao { projectDao.getProjectLabels(projectId) } as MutableList<Label>
            it.addComment(*(Database.executeDao { projectDao.getIssueComment(it.id) } as List<Comment>).toTypedArray())
            it.addLabel(*(Database.executeDao { projectDao.getIssueLabels(projectId,it.id) } as List<Label>).toTypedArray())
        }
        return issues
    }


    fun getAllLabels(projectId: UUID): MutableList<Label> = Database.executeDao { projectDao.getProjectLabels(projectId) } as MutableList<Label>

    fun getLabelfromProject(projectId: UUID, label: String): Label = Database.executeDao { projectDao.getProjectLabel(projectId,label) } as Label

    fun getLabelfromIssue(projectId: UUID,issueId: UUID,label: String): Label = Database.executeDao { projectDao.getIssueLabel(projectId,issueId,label) } as Label

}