package Daw2020v.Issue

import Daw2020v.Authentication.USER_SESSION
import Daw2020v.BaseConstrollerClass
import Daw2020v.Label.LabelOutputModel
import Daw2020v.RequireSession
import Daw2020v.common.ISSUE_ENDPOINT
import Daw2020v.common.model.Issue
import Daw2020v.common.model.Project
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*
import javax.servlet.http.HttpSession

@RequestMapping(ISSUE_ENDPOINT)
@RestController
class IssueController @Autowired constructor(val issueService: IssueService) : BaseConstrollerClass() {


    /**
     * Gets all the [Issue] within a given [Project]
     * @param projectId the id of specified [Project]
     * @return [IssueOutputModel] wrapped in a [ResponseEntity]
     */
    @GetMapping()
    @RequireSession
    fun getAllIssuesFromProject(@PathVariable("projectId") projectId: UUID, session: HttpSession): ResponseEntity<MutableList<IssueOutputModel>> {
        val issues = issueService.getAllIssues(projectId, session.getAttribute(USER_SESSION) as String)
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
    @RequireSession
    fun getAllLabelsFromIssues(@PathVariable("projectId") projectId: UUID, @PathVariable("issueId") issueId: UUID,session: HttpSession): ResponseEntity<MutableList<LabelOutputModel>> {
        val labels = issueService.getIssue(projectId, issueId,session.getAttribute(USER_SESSION) as String).labels
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
    @RequireSession
    fun createIssue(@PathVariable("projectId") projectId: UUID, @RequestBody issue: Issue, session: HttpSession): ResponseEntity<IssueOutputModel> {
        val res = issueService.createIssue(projectId, issue,session.getAttribute(USER_SESSION) as String)
        return ResponseEntity.ok(res.toDto(projectId))
    }

    /**
     * This method gets an [Issue] from a given [Project]
     * @param issueId the ID of the [Issue] to be returned
     * @param projectId The id of the [Project] where the [Issue] is
     * @return [Issue] with the corresponding [issueId]
     */
    @GetMapping(path = arrayOf("/{issueId}"))
    @RequireSession
    fun getIssue(@PathVariable("projectId") projectId: UUID, @PathVariable("issueId") issueId: UUID,session: HttpSession): ResponseEntity<IssueOutputModel> {
        val res = issueService.getIssue(projectId, issueId,session.getAttribute(USER_SESSION) as String)
        return ResponseEntity.ok(res.toDto(projectId))
    }

    /**
     * This method deletes an [Issue] from a given [Project]
     * @param issueId the [Issue] to be removed
     * @param projectId The id of the [Project] to update
     * @return the return is a SuccessResponse object with the details of what was done
     */
    @DeleteMapping(path = arrayOf("/{issueId}"))
    @RequireSession
    fun deleteIssue(@PathVariable("projectId") projectId: UUID, @PathVariable("issueId") issueId: UUID,session: HttpSession): ResponseEntity<IssueOutputModel.IssueDeletedOutputModel> {
        issueService.deleteIssue(projectId, issueId,session.getAttribute(USER_SESSION) as String)
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
    @RequireSession
    fun putLabelinIssue(@PathVariable("projectId") projectId: UUID,
                        @PathVariable("issueId") issueId: UUID,
                        @PathVariable("labelId") labelId: String,session: HttpSession): ResponseEntity<LabelOutputModel> {
        val res = issueService.putLabelInIssue(projectId, issueId, labelId,session.getAttribute(USER_SESSION) as String)
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
    @RequireSession
    fun deleteLabelinIssue(@PathVariable("projectId") projectId: UUID,
                           @PathVariable("issueId") issueId: UUID,
                           @PathVariable("labelId") labelId: String, session: HttpSession): ResponseEntity<LabelOutputModel.LabelDeletedOutputModel> {
        issueService.deleteLabelInIssue(projectId, issueId, labelId,session.getAttribute(USER_SESSION) as String)
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
    @RequireSession
    fun updateIssue(@PathVariable("projectId") projectId: UUID,
                    @PathVariable("issueId") issueId: UUID,
                    @RequestBody issue: IssueInputModel,session: HttpSession): ResponseEntity<IssueOutputModel> {
        val res = issueService.updateIssue(projectId, issueId, issue,session.getAttribute(USER_SESSION) as String)
        return ResponseEntity.ok(res.toDto(projectId))
    }

}