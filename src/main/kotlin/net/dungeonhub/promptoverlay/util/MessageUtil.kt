package net.dungeonhub.promptoverlay.util

import net.dungeonhub.promptoverlay.PromptOverlay
import net.dungeonhub.promptoverlay.config.categories.DevCategory
import net.minecraft.ChatFormatting
import net.minecraft.client.Minecraft
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.Style
import org.slf4j.Logger

object MessageUtil {
    fun Logger.sendDevError(message: String) {
        if (PromptOverlay.isDev) {
            throw RuntimeException(message)
        } else if (DevCategory.extendedDebug) {
            Minecraft.getInstance().execute {
                Minecraft.getInstance().gui.hud.chat.addClientSystemMessage(
                    Component.literal(message).setStyle(Style.EMPTY.withColor(ChatFormatting.RED))
                )
            }
        } else {
            error(message)
        }
    }

    fun Logger.sendDevDebug(message: String) {
        if (PromptOverlay.isDev || DevCategory.extendedDebug) {
            Minecraft.getInstance().execute {
                Minecraft.getInstance().gui.hud.chat.addClientSystemMessage(
                    Component.literal(message).setStyle(Style.EMPTY.withColor(ChatFormatting.YELLOW))
                )
            }
        } else {
            debug(message)
        }
    }

    fun Logger.sendDebug(message: String) {
        if (PromptOverlay.isDev || DevCategory.extendedDebug) {
            Minecraft.getInstance().execute {
                Minecraft.getInstance().gui.hud.chat.addClientSystemMessage(
                    Component.literal(message).setStyle(Style.EMPTY.withColor(ChatFormatting.YELLOW))
                )
            }
        }

        debug(message)
    }

    /**
     * Sends a chat message and optionally runs additional logic on the main thread.
     * Both the message display and the additionalLogic callback run inside Minecraft.execute {},
     * ensuring they execute on the main Minecraft thread.
     */
    fun Minecraft.sendMessage(component: Component, additionalLogic: () -> Unit = {}) {
        execute {
            gui.hud.chat.addClientSystemMessage(component)
            additionalLogic()
        }
    }
}