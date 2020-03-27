package Daw2020v.dtos

import Daw2020v.common.Links
import Daw2020v.model.Project

class ProjectOutputModel(project:Project){
    var properties: PairContainer = PairContainer(
            "id" to project.id.toString(),
            "name" to project.name!!.value,
            "description" to project.shortDesc!!.text)
    var entities : List<PairContainer> = listOf<PairContainer>(
            PairContainer(
                    "class" to "Issues",
                    "rel" to "This project's issues",
                    "href" to Links.allIssues(project.id)),
            PairContainer(
                    "class" to "Labels",
                    "rel" to "This project's labels",
                    "href" to Links.allLabels(project.id)))
    var actions : List<PairContainer> = listOf<PairContainer>(
            PairContainer(
                    "name" to "edit-project",
                    "method" to "PUT",
                    "href" to Links.projectPath(project.id)
            ),
            PairContainer(
            "name" to "delete-project",
            "method" to "DELETE",
            "href" to Links.projectPath(project.id)
    ))
}