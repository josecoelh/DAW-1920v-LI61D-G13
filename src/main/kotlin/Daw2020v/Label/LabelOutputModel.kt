package Daw2020v.Label

import Daw2020v.common.Links
import Daw2020v.common.model.Label
import Daw2020v.common.PairContainer
import java.util.*

class LabelOutputModel(label : Label, projectId : UUID, issueId : UUID? = null) {
    var properties: PairContainer = PairContainer(
            "identifier" to label.identifier)
    var actions: MutableList<PairContainer> = mutableListOf<PairContainer>()
    var links : MutableList<PairContainer> = mutableListOf()
    init{
        if(issueId != null){
            actions.add(PairContainer(
                    "name" to "delete-issue-label",
                    "method" to "DELETE",
                    "href" to Links.labelFromIssue(projectId, issueId, label.identifier)))
            links.add(PairContainer(
                    "rel" to "self",
                    "href" to Links.labelFromIssue(projectId, issueId, label.identifier)))
        }
        else {
            actions.add(PairContainer(
                    "name" to "delete-project-allowedlabel",
                    "method" to "DELETE",
                    "href" to Links.labelFromProject(projectId, label.identifier)))
            links.add(PairContainer(
                    "rel" to "self",
                    "href" to Links.labelFromProject(projectId, label.identifier)))
        }
    }


    class LabelDeletedOutputModel(label: String,projectId : UUID, issueId:UUID? = null) {
        val details = PairContainer(
                "class" to "[label]",
                "description" to "Label $label from ${if(issueId!=null) "the issue$issueId" else "from project $projectId"} successfully deleted"
        )
        val links: List<PairContainer> = listOf(PairContainer(
                "rel" to if(issueId != null)"issue" else "project",
                "href" to if(issueId != null)Links.issuePath(projectId, issueId) else Links.projectPath(projectId)
        ))
    }
}