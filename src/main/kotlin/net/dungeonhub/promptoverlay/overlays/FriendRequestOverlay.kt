package net.dungeonhub.promptoverlay.overlays

import net.dungeonhub.promptoverlay.api.render.AcceptableOverlay
import net.dungeonhub.promptoverlay.api.render.DeniableOverlay
import net.dungeonhub.promptoverlay.api.render.TwoActionsOverlay
import net.dungeonhub.promptoverlay.config.categories.OverlayCategory
import net.dungeonhub.promptoverlay.service.KeyMappingService
import net.minecraft.client.Minecraft
import net.minecraft.network.chat.Component
import java.awt.Color

class FriendRequestOverlay(val from: String): AcceptableOverlay, DeniableOverlay, TwoActionsOverlay {
    override val borderColor: Color = Color(OverlayCategory.friendColor)
    override val message = Component.literal("Friend request from $from")

    override fun accept() {
        Minecraft.getInstance().execute {
            Minecraft.getInstance().player?.connection?.sendCommand("friend accept $from")
        }
    }

    override fun deny() {
        Minecraft.getInstance().execute {
            Minecraft.getInstance().player?.connection?.sendCommand("friend deny $from")
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