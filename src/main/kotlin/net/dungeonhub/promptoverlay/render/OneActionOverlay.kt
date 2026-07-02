package net.dungeonhub.promptoverlay.render

import net.dungeonhub.promptoverlay.service.KeyMappingService
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.Font
import net.minecraft.client.gui.GuiGraphicsExtractor

interface OneActionOverlay : Overlay {
    val firstText: String

    override fun getActionsHeight(width: Int): Int {
        return Minecraft.getInstance().font.lineHeight + 4
    }

    override fun getActionsWidth(font: Font): Int {
        val dismissKeyName = KeyMappingService.dismissKey.translatedKeyMessage.string
        val dismissText = "[$dismissKeyName] Dismiss"
        return font.width(firstText) + font.width(dismissText) + 20 // 20px spacing
    }

    override fun renderActions(graphics: GuiGraphicsExtractor, x: Int, y: Int, width: Int) {
        val font = Minecraft.getInstance().font

        val dismissKeyName = KeyMappingService.dismissKey.translatedKeyMessage.string
        val dismissText = "[$dismissKeyName] Dismiss"

        val textColor = 0xFFFFFFFF.toInt()

        // Calculate center position for side-by-side layout
        val totalWidth = font.width(firstText) + font.width(dismissText) + 20 // 20px spacing
        val startX = x + (width - totalWidth) / 2

        graphics.text(font, firstText, startX, y, textColor)
        graphics.text(font, dismissText, startX + font.width(firstText) + 20, y, textColor)
    }
}