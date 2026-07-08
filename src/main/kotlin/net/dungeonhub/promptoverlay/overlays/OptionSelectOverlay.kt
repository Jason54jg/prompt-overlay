package net.dungeonhub.promptoverlay.overlays

import net.dungeonhub.promptoverlay.api.render.AcceptableOverlay
import net.dungeonhub.promptoverlay.api.render.DeniableOverlay
import net.dungeonhub.promptoverlay.api.render.TwoActionsOverlay
import net.dungeonhub.promptoverlay.config.categories.OverlayCategory
import net.minecraft.client.Minecraft
import java.awt.Color

class OptionSelectOverlay(
    val firstOption: String,
    val firstCommand: String,
    val secondOption: String,
    val secondCommand: String,
    messageOverride: String? = null
): AcceptableOverlay, DeniableOverlay, TwoActionsOverlay {
    override val borderColor: Color = Color(OverlayCategory.optionSelectColor)
    override val message: String = messageOverride ?: "Select an option"

    override fun accept() {
        Minecraft.getInstance().execute {
            Minecraft.getInstance().player?.connection?.sendCommand(firstCommand)
        }
    }

    override fun deny() {
        Minecraft.getInstance().execute {
            Minecraft.getInstance().player?.connection?.sendCommand(secondCommand)
        }
    }

    override val firstText: String
        get() {
            return "[${acceptKey()}] $firstOption" // TODO also support buttons 1-9 for conversations
        }

    override val secondText: String
        get() {
            return "[${denyKey()}] $secondOption" // TODO also support buttons 1-9 for conversations
        }
}