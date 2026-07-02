package net.dungeonhub.promptoverlay.feature

import net.dungeonhub.promptoverlay.enums.ChatRegex
import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents
import net.minecraft.network.chat.Component

object ChatHandler {
    fun init() {
        ClientReceiveMessageEvents.GAME.register(ClientReceiveMessageEvents.Game { message, overlay ->
            if (!overlay) {
                handle(message)
            }
        })
    }

    fun handle(message: Component) {
        for(regex in ChatRegex.entries) {
            val result = regex.regex.find(message.string) ?: continue

            regex.action(result)
        }
    }
}