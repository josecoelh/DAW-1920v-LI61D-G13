package Daw2020v.dao

import Daw2020v.common.model.*
import org.jdbi.v3.sqlobject.config.RegisterRowMapper
import org.jdbi.v3.sqlobject.statement.SqlQuery
import org.jdbi.v3.sqlobject.statement.SqlUpdate
import java.util.*

/*
* Interface to be implemented by who wants to access the DB
* */


interface Dao {


    @SqlQuery("SELECT * FROM project")
    @RegisterRowMapper(Project.ProjectMapper::class)
    fun getAllProjects():List<Project>
    /**
     * Inserts a [Project] in the DB
     * @param project the [Project] to add
     * @return the project
     */
    @SqlUpdate("INSERT INTO project(proj_id , _name,description) VALUES (?, ?, ?)")
    fun createProject(id:UUID, name:String, desc:String): Boolean

    @SqlUpdate("INSERT INTO issues values (:name, :issueId, :projectId, OPEN")
    fun createIssue(projectId: UUID, issueId: UUID, name: String) : Boolean

    @SqlQuery("select al._value from project as proj  join allowed_labels as al on(proj.proj_id = al.proj_id) where proj.proj_id = ?")
    @RegisterRowMapper(Label.LabelMapper::class)
    fun getProjectLabels(id: UUID) : List<Label>

    @SqlQuery("select _value from allowed_labels where proj_id = ? and _value= ?")
    @RegisterRowMapper(Label.LabelMapper::class)
    fun getProjectLabel(projectId: UUID, label:String ) : Label

    @SqlQuery("select iss.issue_id, iss._name , iss._state from project as proj join issues as iss on(proj.proj_id = iss.proj_id) where proj.proj_id = ?")
    @RegisterRowMapper(Issue.IssueMapper::class)
    fun getProjectIssues(id: UUID) : List<Issue>


    @SqlQuery("select com._comment, com._date, com.comment_id from issues as i join _comments as com on (i.issue_id = com.issue_id) where com.issue_id = ?")
    @RegisterRowMapper(Comment.CommentMapper::class)
    fun getIssueComment(id:UUID) : List<Comment>

    @SqlQuery("select il._value from issues as i join issue_labels as il on (i.issue_id = il.issue_id) where i.proj_id = ? and i.issue_id = ?")
    @RegisterRowMapper(Label.LabelMapper::class)
    fun getIssueLabels(projectId: UUID, issueId: UUID) : List<Label>

    @SqlQuery("select il._value from issues as i join issue_labels as il on (i.issue_id = il.issue_id) where i.proj_id = ? and i.issue_id = ? and il._value = ?")
    @RegisterRowMapper(Label.LabelMapper::class)
    fun getIssueLabel(projectId: UUID, issueId: UUID,label: String): Label
    /**
     * Gets a [Project] from the DB
     * @param projectId the id of the [Project] to search
     * @return the [Project]
     */
    @SqlQuery("select * from project where proj_id = ?")
    @RegisterRowMapper(Project.ProjectMapper::class)
    fun getProject(id: UUID): Project

    /**
     * Updates the name/description of a [Project] if such project doesn't already exists, it is inserted into the DB
     * @param projectId identifier of the [Project] to update
     * @param project container with info to update
     * @throws IllegalArgumentException if the [Project] doesn't exist
     * @return the updated [Project]
     */
    @SqlUpdate("update project set _name = ? , description = ? where proj_id = ?")
    fun updateProject(name : String, desc: String, projectId: UUID): Boolean

    @SqlUpdate("update project set _name = ? where proj_id = ?")
    fun changeProjectName(name : String, projectId: UUID): Boolean

    @SqlUpdate("update project set description = ? where proj_id = ?")
    fun changeProjectDescription(desc: String , projectId: UUID): Boolean

    /**
     * Updates the [Label] of a [Project]
     * @param label  [Label] to add in string form
     * @param projectId identifier of the [Project] to update
     * @return the updated [Project]
     * @throws IllegalArgumentException if the [Project] doesn't exist
     */
    @SqlUpdate("insert into allowed_labels values(:label, :projectId)")
    fun addAllowedLabelInProject(projectId: UUID, label : String) : Boolean


    /**
     *
     * Updates the [Issue] of a [Project]
     * @param issue List containing the [Issue] to add
     * @param projectId identifier of the [Project] to update
     * @return the updated [Project]
     * @throws IllegalArgumentException if the [Project] doesn't exist
     */




    @SqlUpdate("update issues set _name = :name, _state = :state where proj_id = :projectId and issue_id = issueId)")
    fun changeIssueName(projectId: UUID, issueId : UUID, name: String) : Boolean

    @SqlUpdate("update issue set state = :state where proj_id = :projectId and issue_id = :issueId ")
    fun changeIssueState(projectId: UUID, issueId: UUID, state: String) : Boolean

    @SqlUpdate(" delete from _comments where issue_id = ?")
    fun deleteIssueComment(issueId: UUID) : Boolean

    @SqlUpdate(" delete from issue_labels where issue_id = ?")
    fun deleteIssueLabels(issueId: UUID) : Boolean

    @SqlUpdate("delete from issue_labels as al where _value=? and al.issue_id in (select iss.issue_id from issues as iss where iss.proj_id = ?)")
    fun deleteLabel(label: String, projectId: UUID)

    @SqlUpdate("delete from issue_labels as al where al.issue_id in (select iss.issue_id from issues as iss where iss.proj_id = ?)")
    fun deleteAllProjectIssueLabels(projectId: UUID)
    /**
     *  Deletes an [Issue] of a [Project]
     *  @param projectId identifier of the [Project] to update
     *  @param issueId identifier of the [Issue] to be removed
     *  @return the updated [Project]
     *  @throws IllegalArgumentException if the [Project] doesn't exist
     */
    @SqlUpdate("delete from issues where  issue_id = ?")
    fun deleteIssue(issueId: UUID): Boolean

    @SqlUpdate("delete from issues where proj_id = ?")
    fun deleteProjectIssues( projectId: UUID)


    /**
     * Deletes a [Label] from the allowed [Label]s list in a [Project]
     * @param projectId identifier of the [Project] to update
     * @param labelId identifier of the label to be removed
     * @return the updated [Project]
     * @throws IllegalArgumentException if the [Project] doesn't exist
     */
    @SqlUpdate("delete from allowed_labels where proj_id = ? and _value = ?")
    fun deleteAllowedLabel(projectId: UUID, label: String): Boolean


    @SqlUpdate("delete from allowed_labels where proj_id = ?")
    fun deleteAllowedLabels(projectId: UUID): Boolean

    @SqlUpdate("delete from _comments as com where com.issue_id in (select iss.issue_id from issues as iss where iss.proj_id = ?)")
    fun deleteIssueCommentsFromProject(projectId: UUID) : Boolean

    /**
     * deletes a [Project] from the DB
     * @param projectId the id of the [Project] to delete
     * @return the removed [Project]
     */
    @SqlUpdate("delete from project where proj_id = ? ")
    fun deleteProject(projectId: UUID): Boolean

    /**
     * Puts a [Label] in a given [Issue]
     * @param projectId the project where the issue is found
     * @param issueId the [Issue] where the [Label] is going to be added
     * @param labelId the [Label] to be added
     * @return the updated project
     * @throws IllegalArgumentException if the [Project] doesn't exist or the [Issue] doesn't exist
     */



    @SqlUpdate("insert into issue_labels values(?,?)")
    fun putLabelInIssue(issueId: UUID, labelId: String): Boolean

    /**
     * Deletes a [Label] in a given [Issue]
     * @param projectId the project where the [Issue] is found
     * @param issueId the [Issue] where the [Label] is going to be deleted
     * @param labelId the [Label] to be deleted
     * @return the updated project
     * @throws IllegalArgumentException if the [Project] doesn't exist or the [Issue] doesn't exist
     */
    @SqlUpdate("delete from issue_labels where issue_id = ? and _value = ?")
    fun deleteLabelInIssue(issueId: UUID, labelId: String): Boolean

    /**
     * Changes an [Issue] name and/or state
     * @param projectId the project where the issue is found
     * @param issueId the issue where the values in [Issue] are going to be applied
     * @param issue contains the values to be updated
     * @return the updated project
     * @throws IllegalArgumentException if the [Project] doesn't exist or the [Issue] doesn't exist
     */
    @SqlUpdate("update issues set _name = :name, _state = :state where proj_id = :projectId and issue_id = issueId)")
    fun updateIssue(projectId: UUID, issueId : UUID, name: String, state: String) : Boolean

    @SqlUpdate("insert into _comments values(?,?,?,?")
    fun addCommentToIssue(comment : String, date : String, commentId: UUID, issueId: UUID): Boolean

    @SqlUpdate("delete from _comments where issue_id = ? and comment_id = ? ")
    fun deleteCommentInIssue( issueId: UUID, commentId: UUID): Boolean

    @SqlQuery("select issue_id, _state, _name from issues where proj_id=? and issue_id = ?")
    @RegisterRowMapper(Issue.IssueMapper::class)
    fun getIssue(projectId: UUID, issueId: UUID): Issue

    @SqlQuery("select _comment, _date, comment_id from _comments where issue_id=? and comment_id=?" )
    @RegisterRowMapper(Comment.CommentMapper::class)
    fun getComment(issueId: UUID, commentId: UUID) : Comment




}