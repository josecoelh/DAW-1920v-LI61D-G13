package Daw2020v.Project

import Daw2020v.dao.Database
import Daw2020v.dao.ProjectDao
import Daw2020v.Issue.IssueInputModel
import Daw2020v.common.model.*
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

    fun insertProject(project: Project): Project {
        if (project.name == null || project.shortDesc == null) throw IllegalArgumentException("Bad project")
        Database.executeDao { projectDao.createProject(project.id, project.name!!.value, project.shortDesc!!.text) }
        return getProject(projectId = project.id)
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


    fun updateProject(projectId: UUID, project: ProjectInputModel): Project {
        Database.executeDao { projectDao.updateProject(project.name!!.value, project.description!!.text, projectId) }
        if (project.name == null && project.description == null) throw IllegalArgumentException("cant update with every field null dumbass")
        if (project.name == null) {
             Database.executeDao { projectDao.changeProjectDescription(project.description!!.text, projectId) }
        } else {
            if (project.description == null) {
                Database.executeDao { projectDao.changeProjectName(project.name!!.value,projectId) }
            } else {
                Database.executeDao { projectDao.updateProject(project.name.value, project.description!!.text,projectId) }
            }
        }
            return getProject(projectId)
    }

    fun addAllowedLabelInProject(projectId: UUID, labels: Array<Label>) {
        labels.forEach { Database.executeDao { projectDao.addAllowedLabelInProject(projectId, it.identifier) } }
    }

    fun createIssue(projectId: UUID, issue: Issue): Issue {
        if(issue.name == null) throw IllegalArgumentException("Bad issue")
        Database.executeDao { projectDao.createIssue(projectId,issue.id, issue.name!!.value) }
        return getIssue(projectId, issue.id)
    }

    fun deleteIssue(issueId: UUID): Boolean {
        Database.executeDao { projectDao.deleteIssueComment(issueId) }
        Database.executeDao { projectDao.deleteIssueLabels(issueId) }
        return Database.executeDao { projectDao.deleteIssue(issueId) } as Boolean
    }

    fun deleteAllowedLabel(projectId: UUID, label: String) {
        Database.executeDao { projectDao.deleteLabel(label, projectId) }
        Database.executeDao { projectDao.deleteAllowedLabel(projectId, label) }
    }

    fun deleteProject(projectId: UUID): Boolean {
        Database.executeDao { projectDao.deleteAllProjectIssueLabels(projectId) }
        Database.executeDao { projectDao.deleteAllowedLabels(projectId) }
        Database.executeDao { projectDao.deleteIssueCommentsFromProject(projectId) }
        Database.executeDao { projectDao.deleteProjectIssues(projectId) }
        Database.executeDao { projectDao.deleteProject(projectId) }
        return true
    }


    fun putLabelinIssue(projectId: UUID, issueId: UUID, label: String): Label {
        val projectLabel: Label? = Database.executeDao { projectDao.getProjectLabel(projectId, label) } as Label?
        if (projectLabel != null) {
            Database.executeDao { projectDao.putLabelInIssue(issueId, label) } as Boolean
            return getLabelfromIssue(projectId, issueId, label)
        } else throw IllegalArgumentException("label not allowed")
    }

    fun deleteLabelInIssue(issueId: UUID, label: String): Boolean {
        return Database.executeDao { projectDao.deleteLabelInIssue(issueId, label) } as Boolean
    }

    fun updateIssue(projectId: UUID, issueId: UUID, issue: IssueInputModel): Issue {
        if (issue.name == null && issue.state == null) throw IllegalArgumentException("cant update with every field null dumbass")
        if (issue.name == null) {
             Database.executeDao { projectDao.changeIssueState(projectId, issueId, issue.state.toString()) }
        } else {
            if (issue.state == null) {
                Database.executeDao { projectDao.changeIssueName(projectId, issueId, issue.name.value) }
            } else {
                Database.executeDao { projectDao.updateIssue(projectId, issueId, issue.name.value, issue.state.toString()) }
            }
        }
        return getIssue(projectId, issueId)
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

    fun addCommentToIssue(projectId: UUID, issueId: UUID, comment: Comment) {
        Database.executeDao { projectDao.addCommentToIssue(comment.value, comment.date, comment.id, issueId) }
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