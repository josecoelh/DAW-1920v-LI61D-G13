package Daw2020v.model

import com.fasterxml.jackson.annotation.JsonProperty
import org.jdbi.v3.core.mapper.RowMapper
import org.jdbi.v3.core.statement.StatementContext
import java.sql.ResultSet
import java.util.*

/*
* Interface to be implemented by projects
 */

class Project(
              @JsonProperty("name") var name: Name?,
              @JsonProperty("description")var shortDesc: ShortDescription?,
              var id :UUID = UUID.randomUUID())
{

    @JsonProperty("labels")var allowedLabels: MutableList<Label> = mutableListOf<Label>()
    @JsonProperty("issues")val issues: MutableList<Issue> = mutableListOf<Issue>()

    class ProjectMapper : RowMapper<Project> {
        override fun map(rs: ResultSet?, ctx: StatementContext?): Project =
            Project(Name(rs!!.getString("_name")),
                    ShortDescription(rs.getString("description")),
                    UUID.fromString(rs.getString("proj_id")))

    }

    /**
     * Update the project's name
     * @param newName the desired name
     */
    fun updateName(newName: Name) {
        name = newName
    }

    /**
     * Update the project's description
     * @param newDesc the desired description
     */
    fun updateDescription(newDesc: ShortDescription) {
        shortDesc = newDesc
    }


    /**
     * Adds an issue to the project
     * @param issue to add
     */
    fun addIssue(issue: Issue) {
        issue.updateLabels(allowedLabels)
        issues.add(issue)
    }

    /**
     * Removes an issue from the project
     * @param issueId The id of the issue that's going to be removed
     */
    fun removeIssue(issueId : UUID) = issues.remove(getIssue(issueId))

    /**
     * Gets the issue with the given issueId
     * @param issueId the id of the issue to be returned
     * @return [Issue] with the corresponding [issueId]
     */
    fun getIssue(issueId : UUID) = issues.find { it.id == issueId }

    /**
     * Remove label from the project and update all the issues from this project
     * @param label The label to remove
     */
    fun removeAllowedLabel(labelId: String) {
        allowedLabels.remove(getAllowedLabel(labelId))
        issues.forEach { it.updateLabels(allowedLabels) }
    }

    /**
     * Gets the label with the given labelId
     * @param labelId the id of the label to be returned
     * @return [Label] with the corresponding [labelId]
     */
    fun getAllowedLabel(labelId: String) = allowedLabels.find { it.identifier == labelId }

    /**
     * Add labels to the project and update all the issues from this project
     * @param labels Single or multiple labels to add/remove
     */
    fun addAllowedLabels(vararg labels: Label) {
        labels.iterator().forEach {labelToAdd->
           if(allowedLabels.find { label-> label.identifier == labelToAdd.identifier } == null) allowedLabels.add(labelToAdd)
        }
        issues.forEach { it.updateLabels(allowedLabels) }
    }

    /**
     * Change the issue state
     * @param Issue the issue to affect
     * @param state State id
     *
     */
    fun changeState(state: IssueState, issue: Issue) = issue.changeState(state)
}

