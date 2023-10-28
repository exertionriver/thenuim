package river.exertion.kcop.view.layout.displayViewLayout

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.jsonObject

object DVLayoutCellSerializer : JsonContentPolymorphicSerializer<DVLayoutCell>(DVLayoutCell::class) {
    override fun selectDeserializer(element: JsonElement): DeserializationStrategy<out DVLayoutCell> = when {
        "tag" in element.jsonObject -> DVPane.serializer()
        "tableTag" in element.jsonObject -> DVTable.serializer()
        else -> DVRow.serializer()
    }
}