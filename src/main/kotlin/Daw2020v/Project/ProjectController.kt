package Daw2020v.Project


import Daw2020v.Label.LabelOutputModel
import Daw2020v.common.*
import Daw2020v.common.model.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import Daw2020v.BaseConstrollerClass

import java.util.*

/**
 * This class is responsible for the routing and response handling, the routes on this class all start with "githubPremium/project"
 */
@RequestMapping(PROJECT_ENDPOINT)
@RestController
class ProjectController @Autowired constructor(val projectService: ProjectService) : BaseConstrollerClass() {


    /** Retrieves all the [Label]'s from a [Project]
     * @param projectId the id of the [Project] that contains the [Label]
     * @return [LabelOutputModel] wrapped in a [ResponseEntity]
     */
    @GetMapping(path = arrayOf("/{projectId}/labels"))
    fun getAllLabelsFromProject(@PathVariable("projectId") projectId: UUID): ResponseEntity<MutableList<LabelOutputModel>> {
        val labels = projectService.getAllLabels(projectId)
        val outputList = mutableListOf<LabelOutputModel>()
        labels.forEach { outputList.add(LabelOutputModel(it, projectId)) }
        return ResponseEntity.ok(outputList)
    }

    /** Retrieves all the [Project]
     * @return [ProjectOutputModel] wrapped in a [ResponseEntity]
     */
    @GetMapping()
    fun getAllProjects(): ResponseEntity<Array<ProjectOutputModel>> {
        val projects = projectService.getAllProjects()
        val res = mutableListOf<ProjectOutputModel>()
        projects.forEach { res.add(it.toDto()) }
        return ResponseEntity.ok(res.toTypedArray())
    }

    /** Retrieves a specific [Project]
     * @param projectId the id of the [Project] to get
     * @return [ProjectOutputModel] wrapped in a [ResponseEntity]
     */
    @GetMapping(path = arrayOf("/{projectId}")) //TODO erros
    fun getProject(@PathVariable("projectId") projectId: UUID): ResponseEntity<ProjectOutputModel> {
        val project : Project = projectService.getProject(projectId)
        return ResponseEntity.ok(project.toDto())
    }


    /**
     * This method inserts a given [Project] into the database
     * @param project The [Project] to insert
     * @return the return is a SuccessResponse object with the details of what was done
     */
    @PostMapping //TODO ERROS
    fun createProject(@RequestBody project: Project): ResponseEntity<ProjectOutputModel> {
        val res : Project = projectService.insertProject(project)
        return ResponseEntity.ok(res.toDto())
    }


    /**
     * This method updates the name and/or description of a given [Project]
     * @param project A container with the name/description to update
     * @param projectId The id of the [Project] to update
     * @return the return is a SuccessResponse object with the details of what was done
     */
    @PutMapping(path = arrayOf("/{projectId}"))
    fun updateProject(@PathVariable("projectId") projectId: UUID, @RequestBody project: ProjectInputModel): ResponseEntity<ProjectOutputModel> {
        val res = projectService.updateProject(projectId, project)
        return ResponseEntity.ok(res.toDto())
    }

    /**
     * This method deletes the specified [Project]
     * @param projectId The id of the [Project] to delete
     * @return the return is a SuccessResponse object with the details of what was done or a badRequest in case something fails
     */
    @DeleteMapping(path = arrayOf("/{projectId}"))
    fun deleteProject(@PathVariable("projectId") projectId: UUID): ResponseEntity<ProjectOutputModel.ProjectDeletedOutputModel> {
        projectService.deleteProject(projectId)
        return ResponseEntity.ok(ProjectOutputModel.ProjectDeletedOutputModel(projectId))
    }

    /**
     * This method updates the [Label]s of a given [Project]
     * @param labels An array containing the [Label]s to add
     * @param projectId The id of the [Project] to update
     * @return the return is a SuccessResponse object with the details of what was done
     */
    @PutMapping(path = arrayOf("/{projectId}/labels"))
    fun putAllowedLabels(@PathVariable("projectId") id: UUID, @RequestBody labels: Array<String>): ResponseEntity<List<LabelOutputModel>> {
        projectService.addAllowedLabelInProject(id, labels)
        val res: List<LabelOutputModel> = labels.map { LabelOutputModel(it, id) }
        return ResponseEntity.ok(res)
    }

    /**
     * This method deletes the specified [Label]s of a given [Project]
     * @param projectId The id of the [Project] to update
     * @param labelId identifier of the [Label] to be removed
     * @return the return is a SuccessResponse object with the details of what was done or a badRequest in case something fails
     */
    @DeleteMapping(path = arrayOf("/{projectId}/labels/{labelId}"))
    fun deleteAllowedLabel(@PathVariable("projectId") projectId: UUID, @PathVariable("labelId") labelId: String): ResponseEntity<LabelOutputModel.LabelDeletedOutputModel> {
        projectService.deleteAllowedLabel(projectId, labelId)
        return ResponseEntity.ok(LabelOutputModel.LabelDeletedOutputModel(labelId, projectId))
    }
}