package Daw2020v.common

import com.fasterxml.jackson.annotation.JsonInclude
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

/**
 * Class used for error models, based on the [Problem Json spec](https://tools.ietf.org/html/rfc7807)
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
data class ProblemJson(
        val type: String,
        val title: String,
        val detail: String,
        val status: Int
)
@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Bad arguments for project")
class BadProjectException : Exception()

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Bad arguments for issue")
class BadIssueException : Exception()

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "This label is not on  allowed on the project where this issue is found")
class LabelNotAllowedException : Exception()

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Repeated label")
class LabelRepeatedException : Exception()