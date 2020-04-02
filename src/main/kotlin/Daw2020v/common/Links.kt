package Daw2020v.common

import java.util.*

const val WIKI_PATH = "https://github.com/josecoelh/DAW-1920v-LI61D-G13/wiki"
const val PROJECT_ENDPOINT = "/githubPremium/projects/"
const val ISSUE_ENDPOINT = "$PROJECT_ENDPOINT{projectId}/issues/"
const val COMMENT_ENDPOINT ="$ISSUE_ENDPOINT{issueId}/comments/"


/*data */class Links/* private constructor(val instance: String)*/{
    /**
     * forces the use of the respective function in relation to the wanted path
     * guarantees good behavior and achieves uniformity
     */
    companion object{
        public const val ALL_PROJECTS = "/githubPremium/projects"
        private const val PROJECT_PATH = "/githubPremium/projects/%s"
        private const val ISSUE_PATH = "$PROJECT_PATH/issues/%s"
        private const val ALL_ISSUES = "$PROJECT_PATH/issues"
        private const val ALL_LABELS = "$PROJECT_PATH/labels"
        private const val PROJECT_LABEL = "$PROJECT_PATH/labels/%s"
        private const val COMMENTS_FROM_ISSUE = "$ISSUE_PATH/comments"
        private const val ALL_LABELS_FROM_ISSUES = "$ISSUE_PATH/labels"
        private const val LABEL_FROM_ISSUE = "$ISSUE_PATH/labels/%s"
        private const val COMMENT_PATH = "$COMMENTS_FROM_ISSUE/%s"


        fun projectPath(projectId: UUID) = String.format(PROJECT_PATH,projectId)
        fun issuePath(projectId: UUID, issueId: UUID) = String.format(ISSUE_PATH,projectId, issueId)
        fun allIssues(projectId: UUID) = String.format(ALL_ISSUES,projectId)
        fun allLabels(projectId: UUID) = String.format(ALL_LABELS,projectId)
        fun labelFromProject(projectId: UUID, label : String) = String.format(PROJECT_LABEL,projectId, label)
        fun allCommentsFromIssue(projectId : UUID, issueId:UUID) = String.format(COMMENTS_FROM_ISSUE,projectId, issueId)
        fun allLabelsFromIssues(projectId: UUID, issueId: UUID): String = String.format(ALL_LABELS_FROM_ISSUES,projectId, issueId)
        fun labelFromIssue(projectId: UUID, issueId: UUID, label: String): String = String.format(LABEL_FROM_ISSUE,projectId,issueId, label)
        fun commentFromIssue(projectId: UUID, issueId: UUID, commentId :UUID): String = String.format(COMMENT_PATH,projectId,issueId, commentId)
    }
}