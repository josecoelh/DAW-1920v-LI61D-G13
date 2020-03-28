package Daw2020v.dtos

import Daw2020v.model.Name
import Daw2020v.model.ShortDescription
import com.fasterxml.jackson.annotation.JsonProperty

class ProjectInputModel(@JsonProperty("name") val name : Name?,
                        @JsonProperty("description") val description : ShortDescription?)