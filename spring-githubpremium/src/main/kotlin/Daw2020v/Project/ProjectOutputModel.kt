package Daw2020v.Project

import Daw2020v.common.ALL_PROJECTS
import Daw2020v.common.Links
import Daw2020v.common.model.Project
import Daw2020v.common.PairContainer
import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import java.util.*

/**
 * This class is to be returned wrapped on a [ResponseEntity], it is supposed to be conversed to a JSON object and it follows Siren style
 */
class ProjectOutputModel(project: Project) {
    var properties: PairContainer = PairContainer(
            "id" to project.id.toString(),
            "name" to project.name!!.value,
            "description" to project.shortDesc!!.text)
    val entities: MutableList<PairContainer> = mutableListOf(
            PairContainer(
                    "class" to "Issues",
                    "rel" to "This project's issues",
                    "href" to Links.allIssues(project.id)),
            PairContainer(
                    "class" to "Labels",
                    "rel" to "This project's labels",
                    "href" to Links.allLabels(project.id))
            )
    var actions: List<PairContainer> = listOf(
            PairContainer(
                    "name" to "edit-project",
                    "method" to "PUT",
                    "href" to Links.projectPath(project.id)
            ),
            PairContainer(
                    "name" to "delete-project",
                    "method" to "DELETE",
                    "href" to Links.projectPath(project.id)
            ),
            PairContainer(
                    "name" to "get-project",
                    "method" to "GET",
                    "href" to Links.projectPath(project.id)
            ))
    var links: List<PairContainer> = listOf(
            PairContainer(
                    "rel" to "self",
                    "href" to Links.projectPath(project.id)
            )
    )


    /**
     * Specific Class for HTTP DELETE methods
     */
    class ProjectDeletedOutputModel(projectId: UUID) {
        val details = PairContainer(
                "class" to "[project]",
                "description" to "Project $projectId successfully deleted"
        )
        val links: List<PairContainer> = listOf(PairContainer(
                "rel" to "all-projects",
                "href" to ALL_PROJECTS
        ))
    }
}



























