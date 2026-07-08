package net.dungeonhub.promptoverlay.overlays

import net.dungeonhub.promptoverlay.api.render.AcceptableOverlay
import net.dungeonhub.promptoverlay.api.render.OneActionOverlay
import net.dungeonhub.promptoverlay.config.categories.OverlayCategory
import net.minecraft.client.Minecraft
import java.awt.Color

class SingleOptionSelectOverlay(
    val firstOption: String,
    val firstCommand: String,
    messageOverride: String? = null
): AcceptableOverlay, OneActionOverlay {
    override val borderColor: Color = Color(OverlayCategory.optionSelectColor)
    override val message: String = messageOverride ?: "Select an option"

    override fun accept() {
        Minecraft.getInstance().execute {
            Minecraft.getInstance().player?.connection?.sendCommand(firstCommand)
        }
    }

    override val firstText: String
        get() {
            return "[${acceptKey()}] $firstOption" // TODO also support buttons 1-9 for conversations
        }
}