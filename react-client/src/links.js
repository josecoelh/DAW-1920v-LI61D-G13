
export default {
        base :`http://localhost:8080/githubPremium/`,
        logout : `http://localhost:8080/githubPremium/logout`,
        projects : `http://localhost:8080/githubPremium/projects`,
        issue : (projectId)=>`http://localhost:8080/githubPremium/projects/${projectId}/issues`,
        projectLabels : (projectId)=>`http://localhost:8080/githubPremium/projects/${projectId}/labels`,
        issueLabels : (projectId,issueId)=>`http://localhost:8080/githubPremium/projects/${projectId}/issues/${issueId}/labels`,
        issueComments : (projectId,issueId)=>`http://localhost:8080/githubPremium/projects/${projectId}/issues/${issueId}/comments`,
        comment :  (projectId,issueId,commentId)=>`/githubPremium/projects/${projectId}/issues/${issueId}/comments/${commentId}`
}