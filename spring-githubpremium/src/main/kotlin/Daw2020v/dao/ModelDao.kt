package Daw2020v.dao

import Daw2020v.common.model.*
import org.jdbi.v3.sqlobject.config.RegisterRowMapper
import org.jdbi.v3.sqlobject.statement.SqlQuery
import org.jdbi.v3.sqlobject.statement.SqlUpdate
import java.util.*

/*
* Interface to be implemented by who wants to access the DB
* */


interface ModelDao {


    @SqlQuery("SELECT * FROM project WHERE proj_id  in (SELECT project_id from PROJECT_USERS where user_name = :username) order by _name limit :size offset :startNumber")
    @RegisterRowMapper(Project.ProjectMapper::class)
    fun getAllProjects(username: String, startNumber: Int, size: Int):List<Project>

    @SqlQuery("SELECT * FROM project WHERE proj_id  in (SELECT project_id from PROJECT_USERS where user_name = :username)")
    @RegisterRowMapper(Project.ProjectMapper::class)
    fun getAllProjects(username: String):List<Project>
    /**
     * Inserts a [Project] in the DB
     * @param project the [Project] to add
     * @return the project
     */
    @SqlUpdate("INSERT INTO project(proj_id , _name,description) VALUES (?, ?, ?)")
    fun createProject(id:UUID, name:String, desc:String): Boolean

    @SqlUpdate("INSERT INTO PROJECT_USERS(project_id,user_name) VALUES(?,?)")
    fun createProjectUser(id:UUID, username: String) : Boolean

    @SqlQuery("Select user_name from PROJECT_USERS where project_id = ? and user_name =?")
    fun getProjectUser(id:UUID, username: String) : String?

    @SqlUpdate("INSERT INTO issues values (:name, :issueId, :projectId, 'OPEN')")
    fun createIssue(projectId: UUID, issueId: UUID, name: String) : Boolean

    @SqlQuery("select al._value from project as proj  join allowed_labels as al on(proj.proj_id = al.proj_id) where proj.proj_id in (SELECT project_id from PROJECT_USERS where project_id = ? and user_name = ?)")
    fun getProjectLabels(id: UUID,username: String) : List<String>

    @SqlQuery("select iss.issue_id, iss._name , iss._state from project as proj join issues as iss on(proj.proj_id = iss.proj_id) where proj.proj_id in (SELECT project_id from PROJECT_USERS where project_id = :id and user_name = :username) order by iss._name limit :size offset :startNumber")
    @RegisterRowMapper(Issue.IssueMapper::class)
    fun getProjectIssues(id: UUID, username: String,startNumber: Int, size: Int) : List<Issue>

    @SqlQuery("select iss.issue_id, iss._name , iss._state from project as proj join issues as iss on(proj.proj_id = iss.proj_id) where proj.proj_id in (SELECT project_id from PROJECT_USERS where project_id = ? and user_name = ?)")
    @RegisterRowMapper(Issue.IssueMapper::class)
    fun getProjectIssues(id: UUID, username: String) : List<Issue>


    @SqlQuery("select com._comment, com.username, com._date, com.comment_id from issues as i join _comments as com on (i.issue_id = com.issue_id) where com.issue_id = :id order by com._date limit :size offset :startNumber")
    @RegisterRowMapper(Comment.CommentMapper::class)
    fun getIssueComments(id:UUID,startNumber: Int, size: Int) : List<Comment>

    @SqlQuery("select com._comment, com.username, com._date, com.comment_id from issues as i join _comments as com on (i.issue_id = com.issue_id) where com.issue_id = :id")
    @RegisterRowMapper(Comment.CommentMapper::class)
    fun getIssueComments(id:UUID) : List<Comment>

    @SqlQuery("select il._value from issues as i join issue_labels as il on (i.issue_id = il.issue_id) where i.proj_id = ? and i.issue_id = ?")
    fun getIssueLabels(projectId: UUID, issueId: UUID) : List<String>


    /**
     * Gets a [Project] from the DB
     * @param projectId the id of the [Project] to search
     * @return the [Project]
     */
    @SqlQuery("select * from project where proj_id in (SELECT project_id from PROJECT_USERS where user_name = :username and project_id = :id)")
    @RegisterRowMapper(Project.ProjectMapper::class)
    fun getProject(id: UUID,username: String): Project

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




    @SqlUpdate("update issues set _name = :name where proj_id in (SELECT project_id from PROJECT_USERS where project_id = :projectId and user_name = :username ) and issue_id = issueId)")
    fun changeIssueName(projectId: UUID, username: String,issueId : UUID, name: String) : Boolean

    @SqlUpdate("update issues set _state = :state where proj_id in (SELECT project_id from PROJECT_USERS where project_id = :projectId and user_name = :username ) and issue_id = :issueId ")
    fun changeIssueState(projectId: UUID,username: String, issueId: UUID, state: String) : Boolean

    @SqlUpdate(" delete from _comments where issue_id = ?")
    fun deleteIssueComment(issueId: UUID) : Boolean

    @SqlUpdate(" delete from issue_labels where issue_id = ?")
    fun deleteIssueLabels(issueId: UUID) : Boolean

    @SqlUpdate("delete from issue_labels as al where _value=? and al.issue_id in (select iss.issue_id from issues as iss where iss.proj_id in (SELECT project_id from PROJECT_USERS where project_id = ? and user_name = ? ) )")
    fun deleteLabel(label: String, projectId: UUID, username: String)

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
    @SqlUpdate("delete from allowed_labels where proj_id in (SELECT project_id from PROJECT_USERS where project_id = ? and user_name = ?) and _value = ?")
    fun deleteAllowedLabel(projectId: UUID, username: String,label: String): Boolean


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



    @SqlUpdate("insert into issue_labels values(:labelId,:issueId)")
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
    @SqlUpdate("update issues set _name = :name, _state = :state where proj_id in (SELECT project_id from PROJECT_USERS where project_id = :projectId and user_name = :username ) and issue_id = :issueId")
    fun updateIssue(projectId: UUID, username: String, issueId : UUID, name: String, state: String) : Boolean

    @SqlUpdate("insert into _comments values(?,?,?,?,?)")
    fun addCommentToIssue(comment : String, date : String, commentId: UUID, issueId: UUID, username: String): Boolean

    @SqlUpdate("delete from _comments where issue_id = ? and comment_id = ? and username = ?")
    fun deleteCommentInIssue( issueId: UUID, commentId: UUID,username: String): Boolean

    @SqlQuery("select issue_id, _state, _name from issues where proj_id in (SELECT project_id from PROJECT_USERS where project_id = ? and user_name = ?) and issue_id = ?")
    @RegisterRowMapper(Issue.IssueMapper::class)
    fun getIssue(projectId: UUID, username: String,issueId: UUID): Issue

    @SqlQuery("select _comment, username, _date, comment_id from _comments where issue_id=? and comment_id=? and username = ?" )
    @RegisterRowMapper(Comment.CommentMapper::class)
    fun getComment(issueId: UUID, commentId: UUID, username: String) : Comment

    @SqlUpdate("delete from project_users where project_id = ? and user_name = ?")
    fun deleteProjectUser(projectId: UUID, username: String): Boolean

    @SqlQuery("select username from _comments where comment_id = :commentId")
    fun getCommentOwner(commentId : UUID) : String
}