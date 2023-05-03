package river.exertion.kcop.sim.narrative.view

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.jsonObject

object DVLayoutCellSerializer : JsonContentPolymorphicSerializer<DVLayoutCell>(DVLayoutCell::class) {
    override fun selectDeserializer(element: JsonElement): DeserializationStrategy<out DVLayoutCell> = when {
        "idx" in element.jsonObject -> DVPane.serializer()
        "tableIdx" in element.jsonObject -> DVTable.serializer()
        else -> DVRow.serializer()
    }
}