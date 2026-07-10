package net.dungeonhub.promptoverlay.overlays

import net.dungeonhub.promptoverlay.api.render.AcceptableOverlay
import net.dungeonhub.promptoverlay.api.render.OneActionOverlay
import net.dungeonhub.promptoverlay.config.categories.OverlayCategory
import net.minecraft.client.Minecraft
import net.minecraft.network.chat.Component
import java.awt.Color
import kotlin.time.Duration

class DarkAuctionWarpOverlay(val duration: Duration) : AcceptableOverlay, OneActionOverlay {
    override fun accept() {
        Minecraft.getInstance().execute {
            Minecraft.getInstance().player?.connection?.sendCommand("warp da")
        }
    }

    override val borderColor get() = Color(OverlayCategory.darkAuctionColor)
    override val message get() = Component.literal("The Dark Auction starts in $duration")
    override val firstText get() = "[${acceptKey()}] Warp"
}