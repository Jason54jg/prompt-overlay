package net.dungeonhub.promptoverlay.overlays

import net.dungeonhub.promptoverlay.render.AcceptableOverlay
import net.dungeonhub.promptoverlay.render.OneActionOverlay
import net.dungeonhub.promptoverlay.service.KeyMappingService
import net.minecraft.client.Minecraft
import java.awt.Color

class GuildRequestOverlay(val inviter: String) : AcceptableOverlay, OneActionOverlay {
    override fun accept() {
        Minecraft.getInstance().execute {
            Minecraft.getInstance().player?.connection?.sendCommand("guild accept $inviter")
        }
    }

    override val borderColor: Color = Color.GREEN
    override val message: String = "$inviter invited you into their guild"

    override val firstText: String
        get() {
            val acceptKeyName = KeyMappingService.acceptKey.translatedKeyMessage.string

            return "[$acceptKeyName] Accept"
        }
}