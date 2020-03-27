package Daw2020v.common

import org.springframework.http.ResponseEntity

/**
 * Object to send inside an [ResponseEntity] when a HTTP request finishes with success
 * */

data class SuccessResponse( val instance: String, val method: HttpMethod) {
    var details = when(method) {
        HttpMethod.POST -> "Project successfully created"
        HttpMethod.PUT -> "Project successfully updated"
        HttpMethod.DELETE -> "Project successfully deleted"
        else -> null
    }

    val title: String = "${method} finished with success"

}

