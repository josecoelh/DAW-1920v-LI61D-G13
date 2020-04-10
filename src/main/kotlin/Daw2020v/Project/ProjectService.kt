package Daw2020v.Project

import Daw2020v.common.BadProjectException
import Daw2020v.dao.Database
import Daw2020v.dao.ModelDao
import Daw2020v.common.model.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

/**
 *
 */
@Service
class ProjectService @Autowired constructor() {
    val modelDao: ModelDao = Database.getDao(ModelDao::class.java)


    fun getAllProjects(): List<Project> {
        return modelDao.getAllProjects()
    }

    fun insertProject(project: Project): Project {
        if (project.name == null || project.shortDesc == null) throw BadProjectException()
        Database.executeDao { modelDao.createProject(project.id, project.name!!.value, project.shortDesc!!.text) }
        return getProject(projectId = project.id)
    }


    fun getProject(projectId: UUID): Project {
        val project: Project = Database.executeDao { modelDao.getProject(projectId) } as Project
        val projectLabels: MutableList<String> = Database.executeDao { modelDao.getProjectLabels(projectId) } as MutableList<String>
        val issues: List<Issue> = Database.executeDao { modelDao.getProjectIssues(projectId) } as List<Issue>
        project.allowedLabels = projectLabels
        issues.forEach {
            it.allowedLabels = projectLabels;
            it.addComment(*(Database.executeDao { modelDao.getIssueComment(it.id) } as List<Comment>).toTypedArray())
            it.addLabel(*(Database.executeDao { modelDao.getIssueLabels(projectId, it.id) } as List<String>).toTypedArray())
            project?.addIssue(it)

        }
            return project
    }


    fun updateProject(projectId: UUID, project: ProjectInputModel): Project {
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
        return getProject(projectId)
    }

    fun addAllowedLabelInProject(projectId: UUID, labels: Array<String>) {
        labels.forEach { Database.executeDao { modelDao.addAllowedLabelInProject(projectId, it) } }
    }


    fun deleteAllowedLabel(projectId: UUID, label: String) {
        Database.executeDao { modelDao.deleteLabel(label, projectId) }
        Database.executeDao { modelDao.deleteAllowedLabel(projectId, label) }
    }

    fun deleteProject(projectId: UUID): Boolean {
        Database.executeDao { modelDao.deleteAllProjectIssueLabels(projectId) }
        Database.executeDao { modelDao.deleteAllowedLabels(projectId) }
        Database.executeDao { modelDao.deleteIssueCommentsFromProject(projectId) }
        Database.executeDao { modelDao.deleteProjectIssues(projectId) }
        Database.executeDao { modelDao.deleteProject(projectId) }
        return true
    }

    fun getAllLabels(projectId: UUID): MutableList<String> = Database.executeDao { modelDao.getProjectLabels(projectId) } as MutableList<String>

}