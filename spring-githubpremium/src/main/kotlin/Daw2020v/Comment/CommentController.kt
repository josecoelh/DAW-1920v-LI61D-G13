package Daw2020v.Comment

import Daw2020v.Authentication.USER_SESSION
import Daw2020v.BaseControllerClass
import Daw2020v.RequireSession
import Daw2020v.common.COMMENT_ENDPOINT
import Daw2020v.common.model.Comment
import Daw2020v.common.model.Issue
import Daw2020v.common.model.Project
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*
import javax.servlet.http.HttpSession

@RequestMapping(COMMENT_ENDPOINT)
@RestController
class CommentController @Autowired constructor(val commentService: CommentService) : BaseControllerClass() {


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
                   @PathVariable("commentId") commentId: UUID, session: HttpSession): ResponseEntity<CommentOutputModel> {
        val res = commentService.getComment(projectId, issueId, commentId, session.getAttribute(USER_SESSION) as String)
        return ResponseEntity.ok(res.toDto(projectId, issueId))
    }

    /**
     * Adds a [Comment] to an [Issue] of a given [Project]
     * @param issueId the identifier of the [Issue] to be updated
     * @param comment to add to the [Issue]
     * @param projectId the id of the [Project] that contains the [Issue]
     * @return the return is a SuccessResponse object with the details of what was done
     */
    @PostMapping()
    @RequireSession
    fun addCommentToIssue(@PathVariable("projectId") projectId: UUID,
                          @PathVariable("issueId") issueId: UUID,
                          @RequestBody comment: CommentInputModel, session: HttpSession): ResponseEntity<CommentOutputModel> {
        val newComment = Comment(comment.value, session.getAttribute(USER_SESSION) as String)!!
        commentService.addCommentToIssue(projectId, issueId, newComment, session.getAttribute(USER_SESSION) as String)
        return ResponseEntity.status(HttpStatus.CREATED).body(newComment.toDto(projectId, issueId))
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
                             @PathVariable("commentId") commentId: UUID, session: HttpSession): ResponseEntity<CommentOutputModel.CommentDeletedOutputModel> {
        commentService.deleteCommentInIssue(issueId, commentId, session.getAttribute(USER_SESSION) as String)
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
                                @PathVariable("issueId") issueId: UUID, session: HttpSession,
                                @RequestParam("size", required = false, defaultValue = "-1") size: Int,
                                @RequestParam("page", required = false, defaultValue = "-1") page: Int
    ): ResponseEntity<MutableList<CommentOutputModel>> {
        val comments = if (size == -1) commentService.getAllComments(projectId, issueId, session.getAttribute(USER_SESSION) as String)
        else commentService.getAllComments(projectId, issueId, session.getAttribute(USER_SESSION) as String, page, size)
        val outputList = mutableListOf<CommentOutputModel>()
        comments.forEach { outputList.add(it.toDto(projectId, issueId)) }
        return ResponseEntity.ok(outputList)
    }
}