package Daw2020v.Issue

import Daw2020v.common.model.IssueState
import Daw2020v.common.model.Name
import com.fasterxml.jackson.annotation.JsonProperty

class IssueInputModel (@JsonProperty("name") val name: Name?,
                       @JsonProperty("state") val state : IssueState?)