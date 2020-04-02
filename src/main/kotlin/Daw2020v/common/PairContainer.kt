package Daw2020v.common

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.fasterxml.jackson.databind.ser.std.StdSerializer


@JsonSerialize(using = PairContainerSerializer::class)
class PairContainer(vararg pairs : Pair<String, String>) {
    val map:MutableMap<String,String> = mutableMapOf(*pairs)
}

class PairContainerSerializer : StdSerializer<PairContainer>(PairContainer::class.java){
    override fun serialize(value: PairContainer?, gen: JsonGenerator?, provider: SerializerProvider?) {
        gen!!.writeObject(value!!.map)
    }
}

