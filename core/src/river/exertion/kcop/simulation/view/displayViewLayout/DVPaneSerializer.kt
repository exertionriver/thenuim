package river.exertion.kcop.simulation.view.displayViewLayout

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.jsonObject

object DVPaneSerializer : JsonContentPolymorphicSerializer<DVPane>(DVPane::class) {
    override fun selectDeserializer(element: JsonElement): DeserializationStrategy<out DVPane> = when {
        "type" in element.jsonObject -> DVTextPane.serializer()
        else -> DVImagePane.serializer()
    }
}