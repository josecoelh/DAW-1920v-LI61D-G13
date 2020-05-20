package Daw2020v.Comment

import Daw2020v.common.Links
import Daw2020v.common.model.Comment
import Daw2020v.common.PairContainer
import java.util.*

class CommentOutputModel(comment : Comment, projectId :UUID, issueId : UUID) {
    var properties: PairContainer = PairContainer(
            "value" to comment.value,
            "id" to comment.id.toString(),
            "date" to comment.date,
            "user" to comment.user
    )
    var actions: List<PairContainer> = listOf<PairContainer>(
            PairContainer(
                    "name" to "delete-comment",
                    "method" to "DELETE",
                    "href" to Links.commentFromIssue(projectId, issueId, comment.id)),
            PairContainer(
                    "name" to "get-comment",
                    "method" to "GET",
                    "href" to Links.commentFromIssue(projectId, issueId, comment.id)))
    var links : List<PairContainer> = listOf(
            PairContainer(
                    "rel" to "self",
                    "href" to Links.commentFromIssue(projectId, issueId, comment.id)))

    class CommentDeletedOutputModel(projectId : UUID, issueId:UUID, commentId: UUID) {
        val details = PairContainer(
                "class" to "[comment]",
                "description" to "Comment $commentId from issue $issueId successfully deleted"
        )
        val links: List<PairContainer> = listOf(PairContainer(
                "rel" to "issue",
                "href" to Links.issuePath(projectId, issueId)
        ))
    }

}