package Daw2020v.Comment

import Daw2020v.BaseConstrollerClass
import Daw2020v.RequireSession
import Daw2020v.common.COMMENT_ENDPOINT
import Daw2020v.common.model.Comment
import Daw2020v.common.model.Issue
import Daw2020v.common.model.Project
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

@RequestMapping(COMMENT_ENDPOINT)
@RestController
class CommentController @Autowired constructor(val commentService: CommentService) : BaseConstrollerClass() {


    /**
     * Gets a specific [Comment]
     * @param projectId the id of the [Project] where the issue's comment is
     * @param issueId the of of the [Issue] where the comment is
     * @param commentId the id of the [Comment] to retrieve
     */
    @GetMapping(path = arrayOf("{commentId}")) //TODO erros
    @RequireSession
    fun getComment(@PathVariable("projectId") projectId: UUID,
                   @PathVariable("issueId") issueId: UUID,
                   @PathVariable("commentId") commentId: UUID): ResponseEntity<CommentOutputModel> {
        val res = commentService.getComment(issueId, commentId)
        return ResponseEntity.ok(res.toDto(projectId, issueId))
    }

    /**
     * Adds a [Comment] to an [Issue] of a given [Project]
     * @param issueId the identifier of the [Issue] to be updated
     * @param comment to add to the [Issue]
     * @param projectId the id of the [Project] that contains the [Issue]
     * @return the return is a SuccessResponse object with the details of what was done
     */
    @PutMapping()
    @RequireSession
    fun addCommentToIssue(@PathVariable("projectId") projectId: UUID,
                          @PathVariable("issueId") issueId: UUID,
                          @RequestBody comment: CommentInputModel): ResponseEntity<CommentOutputModel> {
        val newComment = Comment(comment.value)!!
        commentService.addCommentToIssue(projectId, issueId, newComment)
        return ResponseEntity.ok(newComment.toDto(projectId, issueId))
    }

    /**
     * Removes a [Comment] in an [Issue] of a given [Project]
     * @param issueId the identifier of the [Issue] to be updated
     * @param commentId the identifier of the [Comment] to be removed
     * @param projectId the id of the [Project] that contains the [Issue]
     * @return the return is a SuccessResponse object with the details of what was done
     */
    @DeleteMapping(path = arrayOf("{commentId}"))
    @RequireSession
    fun deleteCommentInIssue(@PathVariable("projectId") projectId: UUID,
                             @PathVariable("issueId") issueId: UUID,
                             @PathVariable("commentId") commentId: UUID): ResponseEntity<CommentOutputModel.CommentDeletedOutputModel> {
        commentService.deleteCommentInIssue(issueId, commentId)
        return ResponseEntity.ok(CommentOutputModel.CommentDeletedOutputModel(projectId, issueId, commentId))
    }

    /**
     * Gets a List containing all the [Comment] from an [Issue]
     * @param projectId the id of the [Project] where the [Issue] is located
     * @param issueId the id of the [Issue]
     * @return a list of [CommentOutputModel] wrapped in a [ResponseEntity]
     */
    @GetMapping()
    @RequireSession
    fun getAllCommentsFromIssue(@PathVariable("projectId") projectId: UUID,
                                @PathVariable("issueId") issueId: UUID): ResponseEntity<MutableList<CommentOutputModel>> {
        val comments = commentService.getAllComments(projectId, issueId)
        val outputList = mutableListOf<CommentOutputModel>()
        comments.forEach { outputList.add(it.toDto(projectId, issueId)) }
        return ResponseEntity.ok(outputList)
    }
}