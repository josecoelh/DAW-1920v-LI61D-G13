package Daw2020v.dtos

import Daw2020v.common.Links
import Daw2020v.model.Label
import Daw2020v.model.Project
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
                            "href" to Links.labelFromIssue(projectId,issueId,label.identifier)))
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

}