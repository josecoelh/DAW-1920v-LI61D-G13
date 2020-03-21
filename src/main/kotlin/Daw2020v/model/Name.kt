package Daw2020v.model

import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.stereotype.Component
import java.lang.IllegalArgumentException



/**
 * Data type for representing Names in the context of this app.
 */

class Name private constructor(@JsonProperty("value")val value: String) {

    companion object {

        val MAX: Int = 30
        val MIN: Int = 3

        /**
         * Factory method that returns a [Name] instance with the specified [value], which length must be between 3 to 18 characters.
         *
         * @param   value   The Name in question
         * @return  The corresponding [Name] instance, or null if the [value] length is not within the admissible interval
         * @throws IllegalArgumentException When the length restrictions are not met
         */
        fun of(value: String): Name =
                if (value.length in MIN..MAX) Name(value)
                else throw IllegalArgumentException("must insert a String with length between ${MIN} and ${MAX}")

        /**
         * Overload of the function call operator to have the same behavior as the [of] function
         */
        operator fun invoke(value: String): Name = of(value)
    }
}

