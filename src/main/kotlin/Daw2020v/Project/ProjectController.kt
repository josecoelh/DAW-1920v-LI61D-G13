package Daw2020v.Project

import Daw2020v.Comment.CommentInputModel
import Daw2020v.Comment.CommentOutputModel
import Daw2020v.Issue.IssueInputModel
import Daw2020v.Issue.IssueOutputModel
import Daw2020v.Label.LabelOutputModel
import Daw2020v.common.HttpMethod
import Daw2020v.common.Links
import Daw2020v.common.PROJECT_ENDPOINT
import Daw2020v.common.model.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

import java.util.*

/**
* This class is responsible for the routing and response handling, the routes on this class all start with "githubPremium/project"
 */
@RequestMapping(PROJECT_ENDPOINT)
@RestController
class ProjectController @Autowired constructor(val projectService: ProjectService) {


    @GetMapping(path = arrayOf("{projectId}/issues"))
    fun getAllIssuesFromProject(@PathVariable("projectId") projectId: UUID):ResponseEntity<MutableList<IssueOutputModel>>{
        val issues = projectService.getAllIssues(projectId)
        val outputList : MutableList<IssueOutputModel> = mutableListOf()
        issues.forEach {
            outputList.add(IssueOutputModel(projectId, it))
        }
        return ResponseEntity.ok(outputList)
    }

    @GetMapping(path = arrayOf("{projectId}/labels/{label}"))
    fun getLabelfromProject(@PathVariable("projectId") projectId: UUID,
                            @PathVariable("label") label: String) : ResponseEntity<LabelOutputModel>{
        val res = projectService.getLabelfromProject(projectId,label)
        return ResponseEntity.ok(LabelOutputModel(res, projectId))
    }

    @GetMapping(path = arrayOf("{projectId}/issues/{issueId}/labels/{label}"))
    fun getLabelfromIssue(@PathVariable("projectId") projectId: UUID,
                          @PathVariable("issueId") issueId: UUID,
                          @PathVariable("label") label: String) : ResponseEntity<LabelOutputModel>{
        val res = projectService.getLabelfromIssue(projectId,issueId,label)
        return ResponseEntity.ok(LabelOutputModel(res, projectId, issueId))
    }

    @GetMapping(path = arrayOf("{projectId}/labels"))
    fun getAllLabelsFromProject(@PathVariable("projectId") projectId: UUID):ResponseEntity<MutableList<LabelOutputModel>>{
        val labels = projectService.getAllLabels(projectId)
        val outputList = mutableListOf<LabelOutputModel>()
        labels.forEach { outputList.add(LabelOutputModel(it, projectId)) }
        return ResponseEntity.ok(outputList)
    }

    @GetMapping(path = arrayOf("{projectId}/issues/{issueId}/labels"))
    fun getAllLabelsFromIssues(@PathVariable("projectId") projectId: UUID,@PathVariable("issueId")  issueId:UUID):ResponseEntity<MutableList<LabelOutputModel>>{
        val labels = projectService.getIssue(projectId, issueId).labels
        val outputList = mutableListOf<LabelOutputModel>()
        labels.forEach { outputList.add(LabelOutputModel(it, projectId, issueId)) }
        return ResponseEntity.ok(outputList)
    }

    @GetMapping()
    fun getAllProjects():ResponseEntity<Array<ProjectOutputModel>>{
        val projects = projectService.getAllProjects()
        val res = mutableListOf<ProjectOutputModel>()
        projects.forEach { res.add(ProjectOutputModel(it))}
        return ResponseEntity.ok(res.toTypedArray())
    }

    @GetMapping(path = arrayOf("{projectId}")) //TODO erros
    fun getProject(@PathVariable("projectId") projectId: UUID): ResponseEntity<ProjectOutputModel> {
        val res = projectService.getProject(projectId)
        return ResponseEntity.ok(ProjectOutputModel(res))
    }


    *
     * This method inserts a given [Project] into the database
     * @param project The [Project] to insert
     * @return the return is a SuccessResponse object with the details of what was done
     *//*
    @PostMapping //TODO ERROS
    fun insertProject(@RequestBody project: Project): ResponseEntity<SuccessResponse> {
        projectService.insertProject(project)
        return ResponseEntity.ok(SuccessResponse(Links.projectPath(project.id), HttpMethod.POST))
    }


    @GetMapping(path = arrayOf("{projectId}/issues/{issueId}/comments/{commentId}")) //TODO erros
    fun getComment(@PathVariable("projectId") projectId: UUID,
                   @PathVariable("issueId") issueId: UUID,
                   @PathVariable("commentId")commentId: UUID): ResponseEntity<CommentOutputModel> {
        val res = projectService.getComment(issueId,commentId)
        return ResponseEntity.ok(CommentOutputModel(res, projectId, issueId))
    }




    /**
     * This method updates the name and/or description of a given [Project]
     * @param project A container with the name/description to update
     * @param projectId The id of the [Project] to update
     * @return the return is a SuccessResponse object with the details of what was done
     */
    @PutMapping(path = arrayOf("{projectId}"))
    fun putProject(@PathVariable("projectId") projectId: UUID, @RequestBody project: ProjectInputModel): ResponseEntity<SuccessResponse> {
        projectService.putProject(projectId, project)
        return ResponseEntity.ok(SuccessResponse( Links.projectPath(projectId), HttpMethod.PUT))
    }

    /**
     * This method deletes the specified [Project]
     * @param projectId The id of the [Project] to delete
     * @return the return is a SuccessResponse object with the details of what was done or a badRequest in case something fails
     */
    @DeleteMapping(path = arrayOf("{projectId}"))
    fun deleteProject(@PathVariable("projectId") projectId: UUID): ResponseEntity<SuccessResponse> {
        val res = projectService.deleteProject(projectId)
        if (res != null) {
            return ResponseEntity.ok(SuccessResponse( Links.projectPath(projectId), HttpMethod.DELETE))
        }
        return ResponseEntity.badRequest().build()
    }

    /**
     * This method updates the [Label]s of a given [Project]
     * @param labels An array containing the [Label]s to add
     * @param projectId The id of the [Project] to update
     * @return the return is a SuccessResponse object with the details of what was done
     */
    @PutMapping(path = arrayOf("{projectId}/labels"))
    fun putAllowedLabels(@PathVariable("projectId") id: UUID, @RequestBody labels: Array<Label>): ResponseEntity<SuccessResponse> {
        val res = projectService.putLabels(id, labels)
        return ResponseEntity.ok(SuccessResponse( Links.projectPath(id), HttpMethod.PUT))
    }

    /**
     * This method deletes the specified [Label]s of a given [Project]
     * @param projectId The id of the [Project] to update
     * @param labelId identifier of the [Label] to be removed
     * @return the return is a SuccessResponse object with the details of what was done or a badRequest in case something fails
     */
    @DeleteMapping(path = arrayOf("{projectId}/labels/{labelId}"))
    fun deleteAllowedLabel(@PathVariable("projectId") projectId: UUID, @PathVariable("labelId") labelId: String): ResponseEntity<SuccessResponse> {
        val res = projectService.deleteAllowedLabel(projectId, labelId)
        return ResponseEntity.ok(SuccessResponse( Links.projectPath(projectId), HttpMethod.DELETE))
    }

    /**
     * This method adds an [Issue] to a given [Project]
     * @param issue the [Issue] to be added
     * @param projectId The id of the [Project] to update
     * @return the return is a SuccessResponse object with the details of what was done
     */
    @PutMapping(path = arrayOf("{projectId}/issues"))
    fun createIssue(@PathVariable("projectId") projectId: UUID, @RequestBody issue: Issue): ResponseEntity<SuccessResponse> {
        val res = projectService.createIssue(projectId, issue)
        return ResponseEntity.ok(SuccessResponse( Links.issuePath(projectId, issue.id), HttpMethod.PUT))
    }

    /**
     * This method gets an [Issue] from a given [Project]
     * @param issueId the ID of the [Issue] to be returned
     * @param projectId The id of the [Project] where the [Issue] is
     * @return [Issue] with the corresponding [issueId]
     */
    @GetMapping(path = arrayOf("{projectId}/issues/{issueId}"))
    fun getIssue(@PathVariable("projectId") projectId: UUID, @PathVariable("issueId") issueId: UUID): ResponseEntity<Issue> {
        val res = projectService.getIssue(projectId,issueId)
        return ResponseEntity.ok(res)
    }

    @PostMapping(path = arrayOf("{projectId}/issues"))
    fun postIssue(@PathVariable("projectId") projectId: UUID, @PathVariable("issueId") issue: Issue): ResponseEntity<SuccessResponse> {
        val res = projectService.insertIssue(projectId,issue)
        return ResponseEntity.ok(SuccessResponse(Links.issuePath(projectId,issue.id), HttpMethod.POST))
    }

    /**
     * This method deletes an [Issue] from a given [Project]
     * @param issueId the [Issue] to be removed
     * @param projectId The id of the [Project] to update
     * @return the return is a SuccessResponse object with the details of what was done
     */
    @DeleteMapping(path = arrayOf("{projectId}/issues/{issueId}"))
    fun deleteIssue(@PathVariable("projectId") projectId: UUID, @PathVariable("issueId") issueId: UUID):ResponseEntity<SuccessResponse> {
        val res = projectService.deleteIssue(issueId)
        return ResponseEntity.ok(SuccessResponse( Links.projectPath(projectId), HttpMethod.DELETE))
    }

    /**
     * This method adds a [Label] to an [Issue] of a given [Project]
     * @param issueId the [Issue] to be updated
     * @param labelId the [Label] to be added
     * @param projectId The id of the [Project] to that contains the issue
     * @return the return is a SuccessResponse object with the details of what was done
     */
    @PutMapping(path = arrayOf("{projectId}/issues/{issueId}/labels/{labelId}"))
    fun putLabelinIssue(@PathVariable("projectId") projectId: UUID,
                        @PathVariable("issueId") issueId: UUID,
                        @PathVariable("labelId") labelId: String): ResponseEntity<SuccessResponse> {
        projectService.putLabelinIssue(projectId, issueId, labelId)
        return ResponseEntity.ok(SuccessResponse( Links.issuePath(projectId,issueId), HttpMethod.PUT))
    }

    /**
     * This method deletes a [Label] from an [Issue] of a given [Project]
     * @param issueId the [Issue] to be updated
     * @param labelId the [Label] to be removed
     * @param projectId The id of the project to that contains the [Issue]
     * @return the return is a SuccessResponse object with the details of what was done
     */
    @DeleteMapping(path = arrayOf("{projectId}/issues/{issueId}/labels/{labelId}"))
    fun deleteLabelinIssue(@PathVariable("projectId") projectId: UUID,
                           @PathVariable("issueId") issueId: UUID,
                           @PathVariable("labelId") labelId: String): ResponseEntity<SuccessResponse> {
        projectService.deleteLabelInIssue( issueId, labelId)
        return ResponseEntity.ok(SuccessResponse( Links.issuePath(projectId,issueId), HttpMethod.DELETE))
    }

    /**
     * This method updates an [Issue] of a given [Project]
     * @param issueId the identifier of the [Issue] to be updated
     * @param issue container with the new fields to update
     * @param projectId The id of the [Project] that contains the [Issue]
     * @return the return is a SuccessResponse object with the details of what was done
     */
    @PutMapping(path = arrayOf("{projectId}/issues/{issueId}"))
    fun updateIssue(@PathVariable("projectId") projectId: UUID,
                    @PathVariable("issueId") issueId: UUID,
                    @RequestBody issue: IssueInputModel): ResponseEntity<SuccessResponse> {
        projectService.updateIssue(projectId, issueId, issue)
        return ResponseEntity.ok(SuccessResponse( Links.issuePath(projectId,issueId), HttpMethod.PUT))
    }

    /**
     * Adds a [Comment] to an [Issue] of a given [Project]
     * @param issueId the identifier of the [Issue] to be updated
     * @param comment to add to the [Issue]
     * @param projectId the id of the [Project] that contains the [Issue]
     * @return the return is a SuccessResponse object with the details of what was done
     */
    @PutMapping(path = arrayOf("{projectId}/issues/{issueId}/comments"))
    fun addCommentToIssue(@PathVariable("projectId") projectId: UUID,
                          @PathVariable("issueId") issueId: UUID,
                          @RequestBody comment: CommentInputModel) : ResponseEntity<SuccessResponse>{
        projectService.addCommentToIssue(projectId, issueId, Comment(comment.value)!!)
        return ResponseEntity.ok(SuccessResponse( Links.issuePath(projectId,issueId), HttpMethod.PUT))
    }

    /**
     * Removes a [Comment] in an [Issue] of a given [Project]
     * @param issueId the identifier of the [Issue] to be updated
     * @param commentId the identifier of the [Comment] to be removed
     * @param projectId the id of the [Project] that contains the [Issue]
     * @return the return is a SuccessResponse object with the details of what was done
     */
    @DeleteMapping(path = arrayOf("{projectId}/issues/{issueId}/comments/{commentId}"))
    fun deleteCommentInIssue(@PathVariable("projectId") projectId: UUID,
                             @PathVariable("issueId") issueId: UUID,
                             @PathVariable("commentId") commentId: UUID) : ResponseEntity<SuccessResponse> {
        projectService.deleteCommentInIssue(issueId, commentId)
        return ResponseEntity.ok(SuccessResponse( Links.issuePath(projectId,issueId),HttpMethod.DELETE))
    }

    @GetMapping(path = arrayOf("{projectId}/issues/{issueId}/comments"))
    fun getAllCommentsFromIssue(@PathVariable("projectId") projectId: UUID,
                             @PathVariable("issueId") issueId: UUID) : ResponseEntity<MutableList<CommentOutputModel>> {
        val comments = projectService.getAllComments(projectId, issueId)
        val outputList = mutableListOf<CommentOutputModel>()
        comments.forEach { outputList.add(CommentOutputModel(it, projectId, issueId)) }
        return ResponseEntity.ok(outputList)
    }

}