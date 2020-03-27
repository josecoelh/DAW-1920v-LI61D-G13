package Daw2020v.dtos

import Daw2020v.model.IssueState
import Daw2020v.model.Name
import com.fasterxml.jackson.annotation.JsonProperty
import java.util.*

class IssueInputModel (@JsonProperty("name") val name: Name?,
                       @JsonProperty("state") val state : IssueState?)