package Daw2020v.dtos

import Daw2020v.common.Links
import Daw2020v.model.Issue
import java.util.*

class IssueOutputModel (projectID: UUID, issue: Issue){
    var properties: PairContainer = PairContainer(
            "id" to issue.id.toString(),
            "name" to issue.name!!.value,
            "state" to issue.state.toString(),
            "allowed labels" to issue.allowedLabels.joinToString { it.identifier })
    var entities : List<PairContainer> = listOf<PairContainer>(
            PairContainer(
                    "class" to "Comments",
                    "rel" to "Comments on this issue",
                    "href" to Links.allCommentsFromIssue(projectID, issue.id)),
            PairContainer(
                    "class" to "Labels",
                    "rel" to "This issues's labels",
                    "href" to Links.allLabelsFromIssues(projectID, issue.id)))
    var actions : List<PairContainer> = listOf<PairContainer>(
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
}