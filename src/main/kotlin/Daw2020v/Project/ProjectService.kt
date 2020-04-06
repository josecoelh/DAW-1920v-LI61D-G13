package Daw2020v.Project

import Daw2020v.common.BadProjectException
import Daw2020v.dao.Database
import Daw2020v.dao.Dao
import Daw2020v.common.model.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

/**
 *
 */
@Service
class ProjectService @Autowired constructor() {
    val dao: Dao = Database.getDao()


    fun getAllProjects(): List<Project> {
        return dao.getAllProjects()
    }

    fun insertProject(project: Project): Project {
        if (project.name == null || project.shortDesc == null) throw BadProjectException()
        Database.executeDao { dao.createProject(project.id, project.name!!.value, project.shortDesc!!.text) }
        return getProject(projectId = project.id)
    }


    fun getProject(projectId: UUID): Project {
        val project: Project = Database.executeDao { dao.getProject(projectId) } as Project
        val projectLabels: MutableList<String> = Database.executeDao { dao.getProjectLabels(projectId) } as MutableList<String>
        val issues: List<Issue> = Database.executeDao { dao.getProjectIssues(projectId) } as List<Issue>
        project.allowedLabels = projectLabels
        issues.forEach {
            it.allowedLabels = projectLabels;
            it.addComment(*(Database.executeDao { dao.getIssueComment(it.id) } as List<Comment>).toTypedArray())
            it.addLabel(*(Database.executeDao { dao.getIssueLabels(projectId, it.id) } as List<String>).toTypedArray())
            project?.addIssue(it)

        }
            return project
    }


    fun updateProject(projectId: UUID, project: ProjectInputModel): Project {
        Database.executeDao { dao.updateProject(project.name!!.value, project.description!!.text, projectId) }
        if (project.name == null && project.description == null) throw BadProjectException()
        if (project.name == null) {
            Database.executeDao { dao.changeProjectDescription(project.description!!.text, projectId) }
        } else {
            if (project.description == null) {
                Database.executeDao { dao.changeProjectName(project.name!!.value, projectId) }
            } else {
                Database.executeDao { dao.updateProject(project.name.value, project.description!!.text, projectId) }
            }
        }
        return getProject(projectId)
    }

    fun addAllowedLabelInProject(projectId: UUID, labels: Array<String>) {
        labels.forEach { Database.executeDao { dao.addAllowedLabelInProject(projectId, it) } }
    }


    fun deleteAllowedLabel(projectId: UUID, label: String) {
        Database.executeDao { dao.deleteLabel(label, projectId) }
        Database.executeDao { dao.deleteAllowedLabel(projectId, label) }
    }

    fun deleteProject(projectId: UUID): Boolean {
        Database.executeDao { dao.deleteAllProjectIssueLabels(projectId) }
        Database.executeDao { dao.deleteAllowedLabels(projectId) }
        Database.executeDao { dao.deleteIssueCommentsFromProject(projectId) }
        Database.executeDao { dao.deleteProjectIssues(projectId) }
        Database.executeDao { dao.deleteProject(projectId) }
        return true
    }

    fun getAllLabels(projectId: UUID): MutableList<String> = Database.executeDao { dao.getProjectLabels(projectId) } as MutableList<String>

}