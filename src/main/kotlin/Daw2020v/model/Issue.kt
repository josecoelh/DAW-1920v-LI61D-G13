package Daw2020v.model


import com.fasterxml.jackson.annotation.JsonProperty
import java.util.*
import org.jdbi.v3.core.mapper.RowMapper
import org.jdbi.v3.core.statement.StatementContext
import java.sql.ResultSet


/*
*  Data type for representing Issues in the context of this app.
 */
data class Issue(@JsonProperty("name")  var name: Name?,
                  var allowedLabels: MutableList<Label> = mutableListOf<Label>(),
                  val id : UUID = UUID.randomUUID(),
                  var state: IssueState?
) {

    class IssueMapper : RowMapper<Issue>{
        override fun map(rs: ResultSet?, ctx: StatementContext?): Issue {
            return Issue(Name(rs!!.getString("_name")),
                    id = UUID.fromString(rs.getString("issue_id")),
                    state = IssueState.valueOf(rs.getString("_state"))
                    )
        }
    }

    var labels: MutableList<Label> = mutableListOf<Label>()

    @JsonProperty("comments") val comments = mutableListOf<Comment>()


    /**
     * Adds a comment to the issue
     * @param comment to add
     */
    fun addComment(vararg value: Comment) = value.forEach { comments.add(it) }

    /**
     * Removes a comment from the issue
     * @param commentId The ID of the [Comment] to remove
     */
    fun removeComment(commentId: UUID) = comments.remove(getComment(commentId))

    /**
     * Gets a [Comment] given an ID
     * @param commentId the ID of the comment
     * @return The comment with the given [commentId]
     */
    fun getComment(commentId : UUID) = comments.find{ it.id == commentId}


    /**
     * Adds a label to the issue
     * @param value the label to add
     * @return [true] if is added with success otherwise returns [false]
     */
    fun addLabel(vararg value: Label) {
        value.forEach { if (allowedLabels.contains(it)) labels.add(it) }
    }



    /**
     * Removes a label to the issue
     * @param value the label to add
     * @return [true] if is removed with success otherwise returns [false]
     */
    fun removeLabel(value: Label): Boolean = labels.remove(value)

    /**
     * Updates the issue labels when the allowed labels are changed
     * @param newAllowed The new list of allowed labels
     */
    fun updateLabels(newAllowed: MutableList<Label>) {
        allowedLabels = newAllowed
        labels = labels.filter { allowedLabels.contains(it) }.toMutableList()
    }

    /**
     * Change this Issue [state]
     * @param value new state
     */
    fun changeState(value: IssueState) {
        state = value
    }

    /**
     * Change this Issue [name]
     * @param value new name
     */
    fun changeName(value : Name) {
        name = value
    }



}


