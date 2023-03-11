package river.exertion.kcop.system.component

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.ai.msg.Telegram
import com.badlogic.gdx.ai.msg.Telegraph
import ktx.ashley.mapperFor
import river.exertion.kcop.narrative.structure.Narrative
import river.exertion.kcop.system.MessageChannel
import river.exertion.kcop.system.view.StatusViewMessage
import river.exertion.kcop.system.view.StatusViewMessageType
import river.exertion.kcop.system.view.ViewMessage
import river.exertion.kcop.simulation.view.ViewType
import river.exertion.kcop.system.profile.Profile

class ProfileComponent : Component, Telegraph {

    var profile : Profile? = null

    override fun handleMessage(msg: Telegram?): Boolean {

        if ( (msg != null) && (MessageChannel.NARRATIVE_PROMPT_BRIDGE.isType(msg.message))) {
            val promptMessage : ViewMessage = MessageChannel.NARRATIVE_PROMPT_BRIDGE.receiveMessage(msg.extraInfo)
        }
        return false
    }

    companion object {
        fun has(entity : Entity?) : Boolean = entity?.components?.firstOrNull{ it is ProfileComponent } != null
        fun getFor(entity : Entity?) : ProfileComponent? = if (has(entity)) entity?.components?.firstOrNull { it is ProfileComponent } as ProfileComponent else null
    }

}