package net.dungeonhub.promptoverlay.overlays

import net.dungeonhub.promptoverlay.api.render.AcceptableOverlay
import net.dungeonhub.promptoverlay.api.render.OneActionOverlay
import net.dungeonhub.promptoverlay.config.categories.OverlayCategory
import net.dungeonhub.promptoverlay.service.KeyMappingService
import net.minecraft.ChatFormatting
import net.minecraft.client.Minecraft
import net.minecraft.network.chat.Component
import java.awt.Color

class SkyblockTradeOverlay(trader: String, val acceptCommand: String): AcceptableOverlay, OneActionOverlay {
    override fun accept() {
        val command = if(acceptCommand.startsWith("/")) acceptCommand.substring(1) else acceptCommand

        Minecraft.getInstance().execute {
            Minecraft.getInstance().player?.connection?.sendCommand(command)
        }
    }

    override val borderColor: Color get() = Color(OverlayCategory.tradeColor)
    override val message = Component.literal("${ChatFormatting.stripFormatting(trader)} sent you a trade request")

    override val firstText: String
        get() {
            val acceptKeyName = KeyMappingService.acceptKey.translatedKeyMessage.string

            return "[$acceptKeyName] Accept"
        }
}