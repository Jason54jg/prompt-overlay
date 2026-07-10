package net.dungeonhub.promptoverlay.overlays

import net.dungeonhub.promptoverlay.api.render.AcceptableOverlay
import net.dungeonhub.promptoverlay.api.render.DeniableOverlay
import net.dungeonhub.promptoverlay.api.render.TwoActionsOverlay
import net.dungeonhub.promptoverlay.config.categories.OverlayCategory
import net.dungeonhub.promptoverlay.service.KeyMappingService
import net.minecraft.client.Minecraft
import net.minecraft.network.chat.Component
import java.awt.Color

class TrapperHuntOverlay(val acceptCommand: String, val denyCommand: String) : AcceptableOverlay, DeniableOverlay, TwoActionsOverlay {
    override val borderColor: Color = Color(OverlayCategory.trapperColor)
    override val message = Component.literal("Accept the Trapper's hunting task?")

    override fun accept() {
        val command = if(acceptCommand.startsWith("/")) acceptCommand.substring(1) else acceptCommand

        Minecraft.getInstance().execute {
            Minecraft.getInstance().player?.connection?.sendCommand(command)
        }
    }

    override fun deny() {
        val command = if(denyCommand.startsWith("/")) denyCommand.substring(1) else denyCommand

        Minecraft.getInstance().execute {
            Minecraft.getInstance().player?.connection?.sendCommand(command)
        }
    }

    override val firstText: String
        get() {
            val acceptKeyName = KeyMappingService.acceptKey.translatedKeyMessage.string

            return "[$acceptKeyName] Accept"
        }

    override val secondText: String
        get() {
            val denyKeyName = KeyMappingService.denyKey.translatedKeyMessage.string

            return "[$denyKeyName] Deny"
        }
}