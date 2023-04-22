package river.exertion.kcop.assets

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.jsonObject

@Serializable(with = DisplayViewLayoutCellSerializer::class)
sealed class DisplayViewLayoutCell {
        open var idx: String? = null
}

object DisplayViewLayoutCellSerializer : JsonContentPolymorphicSerializer<DisplayViewLayoutCell>(DisplayViewLayoutCell::class) {
        override fun selectDeserializer(element: JsonElement) = when {
                "idx" in element.jsonObject -> DisplayViewPane.serializer()
                "tableIdx" in element.jsonObject -> DisplayViewLayoutTable.serializer()
                else -> DisplayViewRow.serializer()
        }
}


