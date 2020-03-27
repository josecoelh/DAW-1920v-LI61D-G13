package Daw2020v.common

import java.util.*

const val WIKI_PATH = "https://github.com/josecoelh/daw/wiki/API-Endpoints"


/*data */class Links/* private constructor(val instance: String)*/{
    /**
     * forces the use of the respective function in relation to the wanted path
     * guarantees good behavior and achieves uniformity
     */
    companion object{
        private const val PROJECT_PATH = "/githubPremium/projects/%s"
        private const val ISSUE_PATH = "$PROJECT_PATH/issues/%s"
        private const val ALL_ISSUES = "$PROJECT_PATH/issues"
        private const val ALL_LABELS = "$PROJECT_PATH/labels"
        private const val COMMENTS_FROM_ISSUE = "$ISSUE_PATH/comments"
        private const val ALL_LABELS_FROM_ISSUES = "$ISSUE_PATH/labels"



        fun projectPath(projectId: UUID) = String.format(PROJECT_PATH,projectId)
        fun issuePath(projectId: UUID, issueId: UUID) = String.format(ISSUE_PATH,projectId, issueId)
        fun allIssues(projectId: UUID) = String.format(ALL_ISSUES,projectId)
        fun allLabels(projectId: UUID) = String.format(ALL_LABELS,projectId)
        fun allCommentsFromIssue(projectId : UUID, issueId:UUID) = String.format(COMMENTS_FROM_ISSUE,projectId, issueId)
        fun allLabelsFromIssues(projectId: UUID, issueId: UUID): String = String.format(ALL_LABELS_FROM_ISSUES,projectId, issueId)

    }
}