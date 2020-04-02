package Daw2020v.Comment

import Daw2020v.common.Links
import Daw2020v.common.model.Comment
import Daw2020v.common.PairContainer
import java.util.*

class CommentOutputModel(comment : Comment, projectId :UUID, issueId : UUID) {
    var properties: PairContainer = PairContainer(
            "value" to comment.value,
            "id" to comment.id.toString(),
            "date" to comment.date)
    var actions: List<PairContainer> = listOf<PairContainer>(
            PairContainer(
                    "name" to "delete-comment-label",
                    "method" to "DELETE",
                    "href" to Links.commentFromIssue(projectId, issueId, comment.id)))
    var links : List<PairContainer> = listOf(
            PairContainer(
                    "rel" to "self",
                    "href" to Links.commentFromIssue(projectId, issueId, comment.id)))

}