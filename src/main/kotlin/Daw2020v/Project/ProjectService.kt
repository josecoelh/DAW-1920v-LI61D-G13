package Daw2020v.Project

import Daw2020v.common.BadProjectException
import Daw2020v.common.ForbiddenResourceException
import Daw2020v.dao.Database
import Daw2020v.dao.ModelDao
import Daw2020v.common.model.*
import netscape.security.ForbiddenTargetException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

/**
 *
 */
@Service
class ProjectService @Autowired constructor() {
    val modelDao: ModelDao = Database.getDao(ModelDao::class.java)


    fun getAllProjects(username:String): List<Project> {
        return modelDao.getAllProjects(username)
    }

    fun insertProject(project: ProjectInputModel, username: String): Project {
        if (project.name == null || project.description == null) throw BadProjectException()
        Database.executeDao { modelDao.createProject(project.id, project.name!!.value, project.description!!.text) }
        Database.executeDao { modelDao.createProjectUser(project.id,username) }
        return getProject(projectId = project.id, username = username)
    }


    fun getProject(projectId: UUID, username: String): Project {
        val project: Project = Database.executeDao { modelDao.getProject(projectId,username) } as Project
        val projectLabels: MutableList<String> = Database.executeDao { modelDao.getProjectLabels(projectId,username) } as MutableList<String>
        val issues: List<Issue> = Database.executeDao { modelDao.getProjectIssues(projectId,username) } as List<Issue>
        project.allowedLabels = projectLabels
        issues.forEach {
            it.allowedLabels = projectLabels;
            it.addComment(*(Database.executeDao { modelDao.getIssueComment(it.id) } as List<Comment>).toTypedArray())
            it.addLabel(*(Database.executeDao { modelDao.getIssueLabels(projectId, it.id) } as List<String>).toTypedArray())
            project?.addIssue(it)

        }
            return project
    }


    fun updateProject(projectId: UUID, username: String,project: ProjectInputModel): Project {
        verifyProjectOwnership(projectId,username)
        Database.executeDao { modelDao.updateProject(project.name!!.value, project.description!!.text, projectId) }
        if (project.name == null && project.description == null) throw BadProjectException()
        if (project.name == null) {
            Database.executeDao { modelDao.changeProjectDescription(project.description!!.text, projectId) }
        } else {
            if (project.description == null) {
                Database.executeDao { modelDao.changeProjectName(project.name!!.value, projectId) }
            } else {
                Database.executeDao { modelDao.updateProject(project.name.value, project.description!!.text, projectId) }
            }
        }
        return getProject(projectId, username)
    }

    fun addAllowedLabelInProject(projectId: UUID,username: String, labels: Array<String>) {
        verifyProjectOwnership(projectId,username)
        labels.forEach { Database.executeDao { modelDao.addAllowedLabelInProject(projectId, it) } }
    }


    fun deleteAllowedLabel(projectId: UUID, username: String,label: String) {
        verifyProjectOwnership(projectId,username)
        Database.executeDao { modelDao.deleteLabel(label, projectId,username) }
        Database.executeDao { modelDao.deleteAllowedLabel(projectId,username, label) }
    }

    fun deleteProject(projectId: UUID,username: String): Boolean {
        verifyProjectOwnership(projectId,username)
        Database.executeDao { modelDao.deleteAllProjectIssueLabels(projectId) }
        Database.executeDao { modelDao.deleteAllowedLabels(projectId) }
        Database.executeDao { modelDao.deleteIssueCommentsFromProject(projectId) }
        Database.executeDao { modelDao.deleteProjectIssues(projectId) }
        Database.executeDao { modelDao.deleteProjectUser(projectId,username) }
        Database.executeDao { modelDao.deleteProject(projectId) }
        return true
    }

    fun verifyProjectOwnership(projectId: UUID,username: String) {
        if((Database.executeDao { modelDao.getProjectUser(projectId,username) } as String?) == null) {
            throw ForbiddenResourceException()
        }
    }

    fun getAllLabels(projectId: UUID,username: String): MutableList<String> {
        return Database.executeDao { modelDao.getProjectLabels(projectId,username) } as MutableList<String>
    }

}