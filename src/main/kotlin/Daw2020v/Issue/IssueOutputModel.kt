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

@JsonSerialize(using = IssueOutputSerializer::class)
class IssueOutputModel (projectID: UUID, issue: Issue){

    var properties: PairContainer = PairContainer(
            "id" to issue.id.toString(),
            "name" to issue.name!!.value,
            "state" to issue.state.toString())
    var entities : MutableList<PairContainer> = mutableListOf<PairContainer>()
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
    var links : List<PairContainer> = listOf(
            PairContainer(
                    "rel" to "self",
                    "href" to Links.issuePath(projectID, issue.id)
            )
    )
    init {
        if(!issue.allowedLabels.isEmpty()) {
            properties.map.put("allowed labels",issue.allowedLabels.joinToString { it.identifier })
        }
        if (!issue.comments.isEmpty() || !issue.labels.isEmpty()) {
            if(!issue.comments.isEmpty()){
                entities.add( PairContainer(
                        "class" to "Comments",
                        "rel" to "Comments on this issue",
                        "href" to Links.allCommentsFromIssue(projectID, issue.id)))
            }
            if(!issue.labels.isEmpty()){
                entities.add(
                        PairContainer(
                                "class" to "Labels",
                                "rel" to "This issues's labels",
                                "href" to Links.allLabelsFromIssues(projectID, issue.id)))
            }
        }
    }
}

class IssueOutputSerializer : StdSerializer<IssueOutputModel>(IssueOutputModel::class.java) {
    override fun serialize(value: IssueOutputModel?, gen: JsonGenerator?, provider: SerializerProvider?) {
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
