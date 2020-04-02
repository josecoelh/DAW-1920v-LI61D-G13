package Daw2020v.Project

import Daw2020v.common.Links
import Daw2020v.common.model.Project
import Daw2020v.common.PairContainer
import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.fasterxml.jackson.databind.ser.std.StdSerializer

@JsonSerialize(using = ProjectOutputSerializer::class)
class ProjectOutputModel(project: Project) {
    var properties: PairContainer = PairContainer(
            "id" to project.id.toString(),
            "name" to project.name!!.value,
            "description" to project.shortDesc!!.text)
    val entities : MutableList<PairContainer> = mutableListOf()
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
            ))
    var links: List<PairContainer> = listOf(
            PairContainer(
                    "rel" to "self",
                    "href" to Links.projectPath(project.id)
            )
    )
    init {
        if (!project.issues.isEmpty() || !project.allowedLabels.isEmpty()) {
            if(!project.issues.isEmpty()){
                entities.add( PairContainer(
                        "class" to "Issues",
                        "rel" to "This project's issues",
                        "href" to Links.allIssues(project.id)))
            }
            if(!project.allowedLabels.isEmpty()){
                entities.add(
                        PairContainer(
                        "class" to "Labels",
                        "rel" to "This project's labels",
                        "href" to Links.allLabels(project.id)))
            }
        }
    }
}


class ProjectOutputSerializer : StdSerializer<ProjectOutputModel>(ProjectOutputModel::class.java){
    override fun serialize(value: ProjectOutputModel?, gen: JsonGenerator?, provider: SerializerProvider?) {
        gen!!.writeStartObject()
        gen.writeObjectField("properties",value!!.properties)
        if(!value.entities.isEmpty()) {
            gen.writeArrayFieldStart("entities")
            value.entities.forEach{ gen.writeObject(it) }
            gen.writeEndArray()
        }
        if(!value.actions.isEmpty()) {
            gen.writeArrayFieldStart("actions")
            value.actions.forEach{ gen.writeObject(it) }
            gen.writeEndArray()
        }
        if(!value.links.isEmpty()) {
            gen.writeArrayFieldStart("links")
            value.links.forEach{ gen.writeObject(it) }
            gen.writeEndArray()
        }
        gen.writeEndObject()
    }

}