package Daw2020v.common.returnTypes

import Daw2020v.model.Name
import Daw2020v.model.ShortDescription
import java.util.*

class ProjectOutputModel(val id : UUID, val name : Name?, val description: ShortDescription?, val ref : String)