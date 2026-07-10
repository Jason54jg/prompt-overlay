package net.dungeonhub.promptoverlay.overlays

import net.dungeonhub.promptoverlay.api.render.AcceptableOverlay
import net.dungeonhub.promptoverlay.api.render.OneActionOverlay
import net.dungeonhub.promptoverlay.config.categories.OverlayCategory
import net.minecraft.client.Minecraft
import net.minecraft.network.chat.Component
import java.awt.Color

class TrapperRestartOverlay: AcceptableOverlay, OneActionOverlay {
    override val borderColor: Color = Color(OverlayCategory.trapperColor)
    override val message = Component.literal("Start a new Trapper hunt?")

    override fun accept() {
        Minecraft.getInstance().execute {
            Minecraft.getInstance().player?.connection?.sendCommand("call Trevor")
        }
    }

    override val firstText: String
        get() {
            return "[${acceptKey()}] Accept"
        }
}