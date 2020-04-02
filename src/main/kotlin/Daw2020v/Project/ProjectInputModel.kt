package Daw2020v.Project

import Daw2020v.common.model.Name
import Daw2020v.common.model.ShortDescription
import com.fasterxml.jackson.annotation.JsonProperty

class ProjectInputModel(@JsonProperty("name") val name : Name?,
                        @JsonProperty("description") val description : ShortDescription?)