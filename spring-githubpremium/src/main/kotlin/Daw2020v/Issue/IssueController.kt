package Daw2020v.Issue

import Daw2020v.BaseControllerClass
import Daw2020v.Label.LabelOutputModel
import Daw2020v.common.ISSUE_ENDPOINT
import Daw2020v.common.model.Issue
import Daw2020v.common.model.Project
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

@RequestMapping(ISSUE_ENDPOINT)
@RestController
class IssueController @Autowired constructor(val issueService: IssueService) : BaseControllerClass() {


    /**
     * Gets all the [Issue] within a given [Project]
     * @param projectId the id of specified [Project]
     * @return [IssueOutputModel] wrapped in a [ResponseEntity]
     */
    @GetMapping()
    fun getAllIssuesFromProject(@PathVariable("projectId") projectId: UUID, @RequestHeader("Authorization") logintoken : String,
                                @RequestParam("size", required = false, defaultValue = "-1") size: Int,
                                @RequestParam("page", required = false, defaultValue = "-1") page: Int): ResponseEntity<MutableList<IssueOutputModel>> {
        val issues =
                if (size == -1)
                    issueService.getAllIssues(projectId, issueService.decodeUsername(logintoken))
                else
                    issueService.getAllIssues(projectId, issueService.decodeUsername(logintoken), page, size)
        val outputList: MutableList<IssueOutputModel> = mutableListOf()
        issues.forEach {
            outputList.add(it.toDto(projectId))
        }
        return ResponseEntity.ok(outputList)
    }


    /**
     * Retrieves all the [Label] from a given [Issue]
     * @param projectId the id of specified [Project]
     * @param issueId the id of the specified [Issue]
     * @return List of [LabelOutputModel] wrapped into a [ResponseEntity]
     */
    @GetMapping(path = arrayOf("/{issueId}/labels"))
    fun getAllLabelsFromIssues(@PathVariable("projectId") projectId: UUID, @PathVariable("issueId") issueId: UUID, @RequestHeader("Authorization") logintoken : String): ResponseEntity<MutableList<LabelOutputModel>> {
        val labels = issueService.getIssue(projectId, issueId, issueService.decodeUsername(logintoken)).labels
        val outputList = mutableListOf<LabelOutputModel>()
        labels.forEach { outputList.add(LabelOutputModel(it, projectId, issueId)) }
        return ResponseEntity.ok(outputList)
    }

    /**
     * This method adds an [Issue] to a given [Project]
     * @param issue the [Issue] to be added
     * @param projectId The id of the [Project] to update
     * @return the return is a SuccessResponse object with the details of what was done
     */
    @PostMapping()
    fun createIssue(@PathVariable("projectId") projectId: UUID, @RequestBody issue: Issue, @RequestHeader("Authorization") logintoken : String): ResponseEntity<IssueOutputModel> {
        val res = issueService.createIssue(projectId, issue, issueService.decodeUsername(logintoken))
        return ResponseEntity.status(HttpStatus.CREATED).body(res.toDto(projectId))
    }

    /**
     * This method gets an [Issue] from a given [Project]
     * @param issueId the ID of the [Issue] to be returned
     * @param projectId The id of the [Project] where the [Issue] is
     * @return [Issue] with the corresponding [issueId]
     */
    @GetMapping(path = arrayOf("/{issueId}"))
    fun getIssue(@PathVariable("projectId") projectId: UUID, @PathVariable("issueId") issueId: UUID, @RequestHeader("Authorization") logintoken : String): ResponseEntity<IssueOutputModel> {
        val res = issueService.getIssue(projectId, issueId, issueService.decodeUsername(logintoken))
        return ResponseEntity.ok(res.toDto(projectId))
    }

    /**
     * This method deletes an [Issue] from a given [Project]
     * @param issueId the [Issue] to be removed
     * @param projectId The id of the [Project] to update
     * @return the return is a SuccessResponse object with the details of what was done
     */
    @DeleteMapping(path = arrayOf("/{issueId}"))
    fun deleteIssue(@PathVariable("projectId") projectId: UUID, @PathVariable("issueId") issueId: UUID, @RequestHeader("Authorization") logintoken : String): ResponseEntity<IssueOutputModel.IssueDeletedOutputModel> {
        issueService.deleteIssue(projectId, issueId, issueService.decodeUsername(logintoken))
        return ResponseEntity.ok(IssueOutputModel.IssueDeletedOutputModel(projectId, issueId))
    }

    /**
     * This method adds a [Label] to an [Issue] of a given [Project]
     * @param issueId the [Issue] to be updated
     * @param labelId the [Label] to be added
     * @param projectId The id of the [Project] to that contains the issue
     * @return the return is a SuccessResponse object with the details of what was done
     */
    @PutMapping(path = arrayOf("/{issueId}/labels/{labelId}"))
    fun putLabelinIssue(@PathVariable("projectId") projectId: UUID,
                        @PathVariable("issueId") issueId: UUID,
                        @PathVariable("labelId") labelId: String, @RequestHeader("Authorization") logintoken : String): ResponseEntity<LabelOutputModel> {
        val res = issueService.putLabelInIssue(projectId, issueId, labelId, issueService.decodeUsername(logintoken))
        return ResponseEntity.ok(LabelOutputModel(res, projectId, issueId))
    }

    /**
     * This method deletes a [Label] from an [Issue] of a given [Project]
     * @param issueId the [Issue] to be updated
     * @param labelId the [Label] to be removed
     * @param projectId The id of the project to that contains the [Issue]
     * @return the return is a SuccessResponse object with the details of what was done
     */
    @DeleteMapping(path = arrayOf("/{issueId}/labels/{labelId}"))
    fun deleteLabelinIssue(@PathVariable("projectId") projectId: UUID,
                           @PathVariable("issueId") issueId: UUID,
                           @PathVariable("labelId") labelId: String, @RequestHeader("Authorization") logintoken : String): ResponseEntity<LabelOutputModel.LabelDeletedOutputModel> {
        issueService.deleteLabelInIssue(projectId, issueId, labelId, issueService.decodeUsername(logintoken))
        return ResponseEntity.ok(LabelOutputModel.LabelDeletedOutputModel(labelId, projectId, issueId))
    }

    /**
     * This method updates an [Issue] of a given [Project]
     * @param issueId the identifier of the [Issue] to be updated
     * @param issue container with the new fields to update
     * @param projectId The id of the [Project] that contains the [Issue]
     * @return the return is a SuccessResponse object with the details of what was done
     */
    @PutMapping(path = arrayOf("/{issueId}"))
    fun updateIssue(@PathVariable("projectId") projectId: UUID,
                    @PathVariable("issueId") issueId: UUID,
                    @RequestBody issue: IssueInputModel, @RequestHeader("Authorization") logintoken : String): ResponseEntity<IssueOutputModel> {
        val res = issueService.updateIssue(projectId, issueId, issue, issueService.decodeUsername(logintoken))
        return ResponseEntity.ok(res.toDto(projectId))
    }

}