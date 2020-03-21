package Daw2020v.dao

import Daw2020v.model.Comment
import Daw2020v.model.Issue
import Daw2020v.model.Label
import Daw2020v.model.Project
import org.springframework.stereotype.Repository
import java.util.*
import kotlin.IllegalArgumentException


@Repository("mockProject")
class ProjectMock /*: ProjectDao */{
/*

    /**
     * Updates the [Label]s of a [Project]
     * @param issue List containing the [Issue] to add
     * @param projectId identifier of the [Project] to update
     * @return the updated [Project]
     * @throws IllegalArgumentException if the [Project] doesn't exist
     */
    override fun putIssue(projectId: UUID, issue: Issue): Boolean {
        val dbProject = getProject(projectId) ?: throw IllegalArgumentException("Project doesn't exist")
        dbProject.addIssue(issue)
        return dbProject
    }

    /**
     *  Deletes an [Issue] of a [Project]
     *  @param projectId identifier of the [Project] to update
     *  @param issueId identifier of the [Issue] to be removed
     *  @return the updated [Project]
     *  @throws IllegalArgumentException if the [Project] doesn't exist
     */
    override fun deletIssue(projectId: UUID, issueId: UUID): Boolean {
        val dbProject = getProject(projectId) ?: throw IllegalArgumentException("Project doesn't exist")
        dbProject.removeIssue(issueId)
        return dbProject
    }

    /**
     * Updates the [Label]s of a [Project]
     * @param labels List containing the [Label]s to add
     * @param projectId identifier of the [Project] to update
     * @return the updated [Project]
     * @throws IllegalArgumentException if the [Project] doesn't exist
     */
    override fun putLabels(projectId: UUID, labels: Array<Label>): Boolean {
        val dbProject = getProject(projectId) ?: throw IllegalArgumentException("Project doesn't exist")
        dbProject.addAllowedLabels(*labels)
        return dbProject
    }

    /**
     * Deletes a [Label] from the allowed [Label]s list in a [Project]
     * @param projectId identifier of the [Project] to update
     * @param labelId identifier of the label to be removed
     * @return the updated [Project]
     * @throws IllegalArgumentException if the [Project] doesn't exist
     */
    override fun deleteAllowedLabel(projectId: UUID, labelId: String): Boolean {
        val dbProject = getProject(projectId) ?: throw IllegalArgumentException("Project doesn't exist")
        dbProject.removeAllowedLabel(labelId)
        return dbProject
    }

    /**
     * Updates the name/description of a [Project] if such project doesn't already exists, it is inserted into the DB
     * @param projectId identifier of the [Project] to update
     * @param project container with info to update
     * @throws IllegalArgumentException if the [Project] doesn't exist
     * @return the updated [Project]
     */
    override fun putProject(projectId: UUID, project: Project): Boolean {
        val dbProject = getProject(projectId) ?: throw IllegalArgumentException("Project doesn't exist")
        if(project.name!=null) dbProject.updateName(project.name!!)
        if(project.shortDesc !=null) dbProject.updateDescription(project.shortDesc!!)
        return dbProject
    }


    /**
     * Gets a [Project] from the DB
     * @param projectId the id of the [Project] to search
     * @return the [Project]
     */
    override fun getProject(projectId: UUID): Boolean? {
        return DB.find { it.id == projectId }
    }

    companion object{
        val DB : MutableList<Project> = mutableListOf<Project>()
    }

    /**
     * Inserts a [Project] in the DB
     * @param project the [Project] to add
     * @return the project
     */
    override fun insertProject(project: Project): Boolean {
        DB.add(project)
        return true
    }

    /**
     * deletes a [Project] from the DB
     * @param projectId the id of the [Project] to delete
     * @return the removed [Project]
     */
    override fun deleteProject(projectId: UUID): Boolean? {
        val res = getProject(projectId)
        DB.remove(res)
        return res
    }

    /**
     * Puts a [Label] in a given [Issue]
     * @param projectId the project where the issue is found
     * @param issueId the [Issue] where the [Label] is going to be added
     * @param labelId the [Label] to be added
     * @return the updated project
     * @throws IllegalArgumentException if the [Project] doesn't exist or the [Issue] doesn't exist
     */
    override fun putLabelInIssue(projectId: UUID, issueId: UUID, labelId: String): Boolean {
        val dbIssue = getIssue(projectId,issueId)
        dbIssue.addLabel(Label(labelId))
        return dbIssue
    }

    /**
     * Deletes a [Label] in a given [Issue]
     * @param projectId the project where the [Issue] is found
     * @param issueId the [Issue] where the [Label] is going to be deleted
     * @param labelId the [Label] to be deleted
     * @return the updated project
     * @throws IllegalArgumentException if the [Project] doesn't exist or the [Issue] doesn't exist
     */
    override fun deleteLabelInIssue(projectId: UUID, issueId: UUID, labelId: String): Boolean {
        val dbIssue = getIssue(projectId,issueId)
        dbIssue.removeLabel(Label(labelId))
        return dbIssue
    }

    /**
     * Changes an [Issue] name and/or state
     * @param projectId the project where the issue is found
     * @param issueId the issue where the values in [Issue] are going to be applied
     * @param issue contains the values to be updated
     * @return the updated project
     * @throws IllegalArgumentException if the [Project] doesn't exist or the [Issue] doesn't exist
     */
    override fun updateIssue(projectId: UUID, issueId: UUID, issue: Issue): Boolean {
        val dbIssue = getIssue(projectId,issueId)
        if(issue.name != null) dbIssue.changeName(issue.name!!)
        if(issue.state != dbIssue.state) dbIssue.changeState(issue.state)
        return dbIssue
    }

    /**
     * Adds [Comment] to an [Issue]
     * @param projectId the project where the issue is found
     * @param issueId the issue where the [comment] is going to be added
     * @param comment contains the values to be added
     * @return the updated project
     * @throws IllegalArgumentException if the [Project] doesn't exist or the [Issue] doesn't exist
     */
    override fun addCommentToIssue(projectId: UUID, issueId: UUID, comment: Comment): Boolean {
        val dbIssue = getIssue(projectId,issueId)
        dbIssue.addComment(comment)
        return dbIssue
    }

    /**
     * Removes [Comment] in an [Issue]
     * @param projectId the project where the issue is found
     * @param issueId the issue where the [Comment] is going to be removed
     * @param commentId the id of the comment to be removed
     * @return the updated project
     * @throws IllegalArgumentException if the [Project] doesn't exist or the [Issue] doesn't exist
     */
    override fun deleteCommentInIssue(projectId: UUID, issueId: UUID, commentId: UUID): Boolean {
        val dbIssue = getIssue(projectId,issueId)
        dbIssue.removeComment(commentId)
        return dbIssue
    }

    override fun getIssue(projectId: UUID, issueId: UUID): Boolean {
        val dbProject = getProject(projectId) ?: throw IllegalArgumentException("Project doesn't exist")
        return dbProject.getIssue(issueId) ?: throw IllegalArgumentException("Issue doesn't exist")
    }

    */
}