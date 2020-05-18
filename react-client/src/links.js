
export default {
        base :`http://localhost:8080/githubPremium/`,
        logout : `http://localhost:8080/githubPremium/logout`,
        projects : `http://localhost:8080/githubPremium/projects`,
        issues : `http://localhost:8080/githubPremium/issues`,
        projectLabels : (projectId)=>`/githubPremium/projects/${projectId}/labels`,
        issueLabels : (projectId,issueId)=>`/githubPremium/projects/${projectId}/issues/${issueId}/labels`,
        issueComments : (projectId,issueId)=>`/githubPremium/projects/${projectId}/issues/${issueId}/comments` ,
        comment :  (projectId,issueId,commentId)=>`/githubPremium/projects/${projectId}/issues/${issueId}/comments/${commentId}`
}