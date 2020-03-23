package Daw2020v.common

import java.util.*

data class Links private constructor(val instance: String){
    /**
     * forces the use of the respective function in relation to the wanted path
     * guarantees good behavior and achieves uniformity
     */
    companion object{
        const val PROJECT_PATH = "/githubPremium/project/"
        const val ISSUE_PATH = "/issue/"


        fun projectPath(projectId: UUID) = Links(PROJECT_PATH + projectId)
        fun issuePath(projectId: UUID, issueId: UUID) = Links(PROJECT_PATH + projectId + ISSUE_PATH + issueId)
    }
}