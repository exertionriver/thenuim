package river.exertion.kcop.assets

import kotlinx.serialization.Serializable
import river.exertion.kcop.Id

@Serializable
data class DisplayViewLayout(

    override var id: String = Id.randomId(),
    val name : String,
    val layout : MutableList<DisplayViewRows> = mutableListOf()

) : Id { }
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
