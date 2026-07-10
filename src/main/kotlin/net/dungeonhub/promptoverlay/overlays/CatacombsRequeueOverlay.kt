package net.dungeonhub.promptoverlay.overlays

import net.dungeonhub.promptoverlay.api.render.AcceptableOverlay
import net.dungeonhub.promptoverlay.api.render.OneActionOverlay
import net.dungeonhub.promptoverlay.config.categories.OverlayCategory
import net.minecraft.client.Minecraft
import net.minecraft.network.chat.Component
import java.awt.Color

class CatacombsRequeueOverlay(val floor: String) : AcceptableOverlay, OneActionOverlay {
    override fun accept() {
        Minecraft.getInstance().execute {
            // TODO: check if the player is dead - as a spectator, /instancerequeue can't be used
            Minecraft.getInstance().player?.connection?.sendCommand("instancerequeue")
        }
    }

    override val borderColor get() = Color(OverlayCategory.catacombsRequeueColor)
    override val message get() = Component.literal("Requeue into $floor?")
    override val firstText get() = "[${acceptKey()}] Requeue"
}