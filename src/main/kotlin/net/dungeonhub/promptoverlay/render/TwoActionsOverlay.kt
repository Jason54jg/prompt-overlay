package net.dungeonhub.promptoverlay.render

import net.dungeonhub.promptoverlay.service.KeyMappingService
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.Font
import net.minecraft.client.gui.GuiGraphicsExtractor
import kotlin.math.max

interface TwoActionsOverlay : Overlay {
    val firstText: String
    val secondText: String

    override fun getActionsHeight(width: Int): Int {
        val font = Minecraft.getInstance().font
        return font.lineHeight * 2 + 8 // Two lines with spacing
    }

    override fun getActionsWidth(font: Font): Int {
        val dismissKeyName = KeyMappingService.dismissKey.translatedKeyMessage.string
        val dismissText = "[$dismissKeyName] Dismiss"

        val firstLineWidth = font.width(firstText) + font.width(secondText) + 20
        val secondLineWidth = font.width(dismissText)

        return max(firstLineWidth, secondLineWidth)
    }

    override fun renderActions(graphics: GuiGraphicsExtractor, x: Int, y: Int, width: Int) {
        val font = Minecraft.getInstance().font

        val dismissKeyName = KeyMappingService.dismissKey.translatedKeyMessage.string
        val dismissText = "[$dismissKeyName] Dismiss"

        val textColor = 0xFFFFFFFF.toInt()

        // First line: two actions side-by-side
        val firstLineWidth = font.width(firstText) + font.width(secondText) + 20 // 20px spacing
        val firstLineStartX = x + (width - firstLineWidth) / 2

        graphics.text(font, firstText, firstLineStartX, y, textColor)
        graphics.text(font, secondText, firstLineStartX + font.width(firstText) + 20, y, textColor)

        // Second line: dismiss centered below
        val secondLineY = y + font.lineHeight + 4 // 4px spacing between lines
        val dismissWidth = font.width(dismissText)
        val dismissX = x + (width - dismissWidth) / 2

        graphics.text(font, dismissText, dismissX, secondLineY, textColor)
    }
}