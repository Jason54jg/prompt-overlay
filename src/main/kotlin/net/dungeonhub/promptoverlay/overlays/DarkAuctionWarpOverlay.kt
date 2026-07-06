package net.dungeonhub.promptoverlay.overlays

import net.dungeonhub.promptoverlay.api.render.AcceptableOverlay
import net.dungeonhub.promptoverlay.api.render.OneActionOverlay
import net.dungeonhub.promptoverlay.config.categories.OverlayCategory
import net.minecraft.client.Minecraft
import java.awt.Color

class DarkAuctionWarpOverlay : AcceptableOverlay, OneActionOverlay {
    override fun accept() {
        Minecraft.getInstance().execute {
            Minecraft.getInstance().player?.connection?.sendCommand("warp da")
        }
    }

    override val borderColor get() = Color(OverlayCategory.darkAuctionColor)
    override val message get() = "The Dark Auction starts in 15 seconds"
    override val firstText get() = "[${acceptKey()}] Warp"
}