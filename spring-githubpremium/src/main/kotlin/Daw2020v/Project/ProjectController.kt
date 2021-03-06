package Daw2020v.Project


import Daw2020v.Label.LabelOutputModel
import Daw2020v.common.*
import Daw2020v.common.model.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import Daw2020v.BaseControllerClass
import org.springframework.http.HttpStatus

import java.util.*

/**
 * This class is responsible for the routing and response handling, the routes on this class all start with "githubPremium/project"
 */
@RequestMapping(PROJECT_ENDPOINT)
@RestController
class ProjectController @Autowired constructor(val projectService: ProjectService) : BaseControllerClass() {


    /** Retrieves all the [Label]'s from a [Project]
     * @param projectId the id of the [Project] that contains the [Label]
     * @return [LabelOutputModel] wrapped in a [ResponseEntity]
     */
    @GetMapping(path = arrayOf("/{projectId}/labels"))
    fun getAllLabelsFromProject(@PathVariable("projectId") projectId: UUID, @RequestHeader("Authorization") logintoken : String): ResponseEntity<MutableList<LabelOutputModel>> {
        val labels = projectService.getAllLabels(projectId,  projectService.decodeUsername(logintoken))
        val outputList = mutableListOf<LabelOutputModel>()
        labels.forEach { outputList.add(LabelOutputModel(it, projectId)) }
        return ResponseEntity.ok(outputList)
    }

    /** Retrieves all the [Project]
     * @return [ProjectOutputModel] wrapped in a [ResponseEntity]
     */
    @GetMapping(/*params = arrayOf("page","size")*/)
    fun getAllProjects(@RequestHeader("Authorization") logintoken : String,
                       @RequestParam("size", required = false, defaultValue = "-1") size: Int,
                       @RequestParam("page", required = false, defaultValue = "-1") page: Int): ResponseEntity<Array<ProjectOutputModel>> {
        val projects = if (size ==-1)
            projectService.getAllProjects(projectService.decodeUsername(logintoken))
        else
            projectService.getAllProjects(projectService.decodeUsername(logintoken), page, size)
        val res = mutableListOf<ProjectOutputModel>()
        projects.forEach { res.add(it.toDto()) }
        return ResponseEntity.ok(res.toTypedArray())
    }

    /** Retrieves a specific [Project]
     * @param projectId the id of the [Project] to get
     * @return [ProjectOutputModel] wrapped in a [ResponseEntity]
     */
    @GetMapping(path = arrayOf("/{projectId}"))
    fun getProject(@PathVariable("projectId") projectId: UUID, @RequestHeader("Authorization") logintoken : String): ResponseEntity<ProjectOutputModel> {
        val project: Project = projectService.getProject(projectId, projectService.decodeUsername(logintoken))
        return ResponseEntity.ok(project.toDto())
    }


    /**
     * This method inserts a given [Project] into the database
     * @param project The [Project] to insert
     * @return the return is a SuccessResponse object with the details of what was done
     */
    @PostMapping
    fun createProject(@RequestBody project: ProjectInputModel, @RequestHeader("Authorization") logintoken : String): ResponseEntity<ProjectOutputModel> {
        val res: Project = projectService.insertProject(project, projectService.decodeUsername(logintoken))
        return ResponseEntity.status(HttpStatus.CREATED).body(res.toDto())
    }


    /**
     * This method updates the name and/or description of a given [Project]
     * @param project A container with the name/description to update
     * @param projectId The id of the [Project] to update
     * @return the return is a SuccessResponse object with the details of what was done
     */
    @PutMapping(path = arrayOf("/{projectId}"))
    fun updateProject(@PathVariable("projectId") projectId: UUID, @RequestBody project: ProjectInputModel, @RequestHeader("Authorization") logintoken : String): ResponseEntity<ProjectOutputModel> {
        val res = projectService.updateProject(projectId, projectService.decodeUsername(logintoken), project)
        return ResponseEntity.ok(res.toDto())
    }

    /**
     * This method deletes the specified [Project]
     * @param projectId The id of the [Project] to delete
     * @return the return is a SuccessResponse object with the details of what was done or a badRequest in case something fails
     */
    @DeleteMapping(path = arrayOf("/{projectId}"))
    fun deleteProject(@PathVariable("projectId") projectId: UUID, @RequestHeader("Authorization") logintoken : String): ResponseEntity<ProjectOutputModel.ProjectDeletedOutputModel> {
        projectService.deleteProject(projectId, projectService.decodeUsername(logintoken))
        return ResponseEntity.ok(ProjectOutputModel.ProjectDeletedOutputModel(projectId))
    }

    /**
     * This method updates the [Label]s of a given [Project]
     * @param labels An array containing the [Label]s to add
     * @param projectId The id of the [Project] to update
     * @return the return is a SuccessResponse object with the details of what was done
     */
    @PutMapping(path = arrayOf("/{projectId}/labels"))
    fun putAllowedLabels(@PathVariable("projectId") id: UUID, @RequestBody labels: Array<String>, @RequestHeader("Authorization") logintoken : String): ResponseEntity<List<LabelOutputModel>> {
        projectService.addAllowedLabelInProject(id, projectService.decodeUsername(logintoken), labels)
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
    fun deleteAllowedLabel(@PathVariable("projectId") projectId: UUID, @PathVariable("labelId") labelId: String, @RequestHeader("Authorization") logintoken : String): ResponseEntity<LabelOutputModel.LabelDeletedOutputModel> {
        projectService.deleteAllowedLabel(projectId, projectService.decodeUsername(logintoken), labelId)
        return ResponseEntity.ok(LabelOutputModel.LabelDeletedOutputModel(labelId, projectId))
    }
}