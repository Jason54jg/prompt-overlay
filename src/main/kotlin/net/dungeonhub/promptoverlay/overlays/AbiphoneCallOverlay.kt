package net.dungeonhub.promptoverlay.overlays

import net.dungeonhub.promptoverlay.api.render.AcceptableOverlay
import net.dungeonhub.promptoverlay.api.render.OneActionOverlay
import net.dungeonhub.promptoverlay.config.categories.OverlayCategory
import net.minecraft.client.Minecraft
import net.minecraft.network.chat.Component
import java.awt.Color

class AbiphoneCallOverlay(caller: String?, val acceptCommand: String) : AcceptableOverlay, OneActionOverlay {
    override fun accept() {
        val command = if(acceptCommand.startsWith("/")) acceptCommand.substring(1) else acceptCommand

        Minecraft.getInstance().execute {
            Minecraft.getInstance().player?.connection?.sendCommand(command)
        }
    }

    override val borderColor get() = Color(OverlayCategory.abiphoneColor)
    override val message = Component.literal("Incoming call${if (caller != null) " from $caller" else ""}")
    override val firstText get() = "[${acceptKey()}] Accept"
}