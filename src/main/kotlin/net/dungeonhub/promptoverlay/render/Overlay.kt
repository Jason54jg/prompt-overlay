package net.dungeonhub.promptoverlay.render

import net.minecraft.client.gui.Font
import net.minecraft.client.gui.GuiGraphicsExtractor
import java.awt.Color

interface Overlay {
    val borderColor: Color
    val message: String

    fun getActionsHeight(width: Int): Int
    fun getActionsWidth(font: Font): Int
    fun renderActions(graphics: GuiGraphicsExtractor, x: Int, y: Int, width: Int)

    fun dismiss() {  }
}