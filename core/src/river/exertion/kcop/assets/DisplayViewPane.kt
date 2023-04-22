package river.exertion.kcop.assets

import kotlinx.serialization.Serializable

@Serializable
data class DisplayViewPane(
    override var idx : String?,
    val type : String = "image",
    val width : String? = null,
    val height : String? = null,
    val refineX : String? = null,
    val refineY : String? = null
) : DisplayViewLayoutCell()


