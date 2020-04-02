package Daw2020v.common.model

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.TreeNode
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import java.lang.IllegalArgumentException



/**
 * Data type for representing Names in the context of this app.
 */
@JsonDeserialize(using = NameDeSerializer::class)
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
class NameDeSerializer: StdDeserializer<Name>(Name::class.java) {
    override fun deserialize(jp: JsonParser?, ctxt: DeserializationContext?): Name {
        val node = jp!!.codec.readTree<TreeNode>(jp)
        val value : String = node.toString().replace("\"","")
        return Name(value)
    }

}
