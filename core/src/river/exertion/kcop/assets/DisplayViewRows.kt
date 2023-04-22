package river.exertion.kcop.assets

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@Serializable(with = DisplayViewRowsSerializer::class)
data class DisplayViewRows(
        val panes : List<DisplayViewPane>
)

//https://github.com/Kotlin/kotlinx.serialization/issues/730
object DisplayViewRowsSerializer : KSerializer<DisplayViewRows> {

        val serializer = ListSerializer(DisplayViewPane.serializer())
        override val descriptor: SerialDescriptor = serializer.descriptor

        override fun serialize(encoder: Encoder, value: DisplayViewRows) {
                val rowValues = value.panes
                rowValues.let { rowValues }
                        .let { encoder.encodeSerializableValue(serializer, it) }
        }

        override fun deserialize(decoder: Decoder): DisplayViewRows {
                return DisplayViewRows(decoder.decodeSerializableValue(serializer))
        }
}

/* "layout" : [{"row" : [{"pane" : {"idx" : "0", "type" : "image", "width" : "large", "height" : "large", "refineX" : "1", "refineY" : "0" }}
          , {"pane" : {"idx" : "1", "type" : "text", "width" :  "title", "height" :  "large", "refineX" : "3", "refineY" : "0" }}]}
          , {"row" : [{"pane" : {"idx" :  "2", "type" :  "text", "width" : "full", "title" : "title", "refineX" : "4", "refineY" : "1" }}]}],*


 */
    /*
{"id" : "00786ad8-111e-4a10-8d8d-01b35344d6c5",
  "name" : "basicPictureNarrative",
  "layout" : [[{"idx" : "0", "type" : "image", "width" : "large", "height" : "large", "refineX" : "1", "refineY" : "0" }
          , {"idx" : "1", "type" : "text", "width" :  "title", "height" :  "large", "refineX" : "3", "refineY" : "0" }]
          , [{"idx" :  "2", "type" :  "text", "width" : "full", "title" : "title", "refineX" : "4", "refineY" : "1" }]],
  "textAdjacencyRows" : [
    { "fontsize" : "small", "idx" : "1", "allowRows" : "2"},
    { "fontsize" : "medium", "idx" : "1", "allowRows" : "1"},
    { "fontsize" : "large", "idx" : "1", "allowRows" : "1"}
  ],
  "textAdjacencyTopPad" : [
    { "fontsize" : "small", "idx" : "2", "yOffset" : "-9"},
    { "fontsize" : "medium", "idx" : "2", "yOffset" : "-15"},
    { "fontsize" : "large", "idx" : "2", "yOffset" : "7"}
  ]
*/

