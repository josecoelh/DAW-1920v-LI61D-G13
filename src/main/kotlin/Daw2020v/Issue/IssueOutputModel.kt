package Daw2020v.Issue

import Daw2020v.Project.ProjectOutputModel
import Daw2020v.common.Links
import Daw2020v.common.model.Issue
import Daw2020v.common.PairContainer
import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import java.util.*

class IssueOutputModel(projectID: UUID, issue: Issue) {

    var properties: PairContainer = PairContainer(
            "id" to issue.id.toString(),
            "name" to issue.name!!.value,
            "state" to issue.state.toString(),
            "allowed labels" to issue.allowedLabels.joinToString { it.identifier })
    var entities = listOf<PairContainer>(
            PairContainer(
                    "class" to "Comments",
                    "rel" to "Comments on this issue",
                    "href" to Links.allCommentsFromIssue(projectID, issue.id)),
            PairContainer(
                    "class" to "Labels",
                    "rel" to "This issues's labels",
                    "href" to Links.allLabelsFromIssues(projectID, issue.id))
    )
    var actions: List<PairContainer> = listOf<PairContainer>(
            PairContainer(
                    "name" to "edit-issue",
                    "method" to "PUT",
                    "href" to Links.issuePath(projectID, issue.id)
            ),
            PairContainer(
                    "name" to "delete-issue",
                    "method" to "DELETE",
                    "href" to Links.issuePath(projectID, issue.id)
            ))
    var links: List<PairContainer> = listOf(
            PairContainer(
                    "rel" to "self",
                    "href" to Links.issuePath(projectID, issue.id)
            )
    )

    class IssueDeletedOutputModel(projectId: UUID, issueId: UUID) {
        val details = PairContainer(
                "class" to "[issue]",
                "description" to "Issue $issueId from project $projectId successfully deleted"
        )
        val links: List<PairContainer> = listOf(PairContainer(
                "rel" to "project",
                "href" to Links.projectPath(projectId)
        ))
    }
}
