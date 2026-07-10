package net.dungeonhub.promptoverlay.overlays

import net.dungeonhub.promptoverlay.api.render.AcceptableOverlay
import net.dungeonhub.promptoverlay.api.render.OneActionOverlay
import net.dungeonhub.promptoverlay.config.categories.OverlayCategory
import net.dungeonhub.promptoverlay.service.KeyMappingService
import net.minecraft.client.Minecraft
import net.minecraft.network.chat.Component
import java.awt.Color

class TrophyFishGgOverlay: AcceptableOverlay, OneActionOverlay {
    override fun accept() {
        Minecraft.getInstance().execute {
            Minecraft.getInstance().player?.connection?.sendCommand("ac gg")
        }
    }

    override val borderColor: Color = Color(OverlayCategory.trophyFishColor)
    override val message = Component.literal("Congratulate for a rare drop")
    override val firstText: String
        get() {
            val acceptKeyName = KeyMappingService.acceptKey.translatedKeyMessage.string

            return "[$acceptKeyName] Send GG"
        }
}