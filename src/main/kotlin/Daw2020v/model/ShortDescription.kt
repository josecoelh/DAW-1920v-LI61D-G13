package Daw2020v.model

import com.fasterxml.jackson.annotation.JsonProperty
import java.lang.IllegalArgumentException

/**
 * Data type for representing Descriptions with a short length.
 */

class ShortDescription private constructor(@JsonProperty("value")var text: String) {

    companion object {

        private const val MAX: Int = 100
        private const val MIN: Int = 0

        /**
         * Factory method that returns a [ShortDescription] instance with the specified [value],
         * which length must be between 0 to 30 characters.
         *
         * @param   value   The Name in question
         * @return  The corresponding [ShortDescription] instance, or null if the [value] length is not within the admissible interval
         * @throws IllegalArgumentException When the length restrictions are not met
         */
        fun of(value: String): ShortDescription =
                if (value.length in MIN..MAX) ShortDescription(value)
                else throw IllegalArgumentException("must insert a String with length between ${MIN} and ${MAX}")

        /**
         * Overload of the function call operator to have the same behavior as the [of] function
         */
        operator fun invoke(value: String): ShortDescription = of(value)
    }

    /**
     * Appends the given value to the current Description value.
     *
     * @param   value  The value to append to the current instance.
     * @return  the resulting [ShortDescription] instance or null if the result is not within the admissible values.
     */
    fun append(value: String): ShortDescription? = of(this.text + value)
}