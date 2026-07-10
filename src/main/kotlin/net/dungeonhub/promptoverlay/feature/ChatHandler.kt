package net.dungeonhub.promptoverlay.feature

import net.dungeonhub.promptoverlay.enums.ChatRegex
import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents
import net.minecraft.network.chat.Component
import java.util.LinkedList

object ChatHandler {
    private const val MAX_HISTORY_SIZE = 100
    private val messageHistory = LinkedList<Component>()

    fun init() {
        ClientReceiveMessageEvents.GAME.register(ClientReceiveMessageEvents.Game { message, overlay ->
            if (!overlay) {
                handle(message)
            }
        })
    }

    fun handle(message: Component) {
        addToHistory(message)

        for(regex in ChatRegex.entries.filter { it.enabled() }) {
            val result = regex.regex.find(message.string) ?: continue

            regex.action(message, result)
        }
    }

    /**
     * Adds a message to the history buffer, maintaining the maximum size.
     */
    private fun addToHistory(message: Component) {
        messageHistory.addFirst(message)
        if (messageHistory.size > MAX_HISTORY_SIZE) {
            messageHistory.removeLast()
        }
    }

    /**
     * Retrieves the last N messages from chat history, excluding the current message.
     *
     * @param count The number of messages to retrieve
     * @return A list of messages, with the most recent first (not including the current message)
     */
    fun getLastMessages(count: Int): List<Component> {
        // Skip the first message (current) and return the next N
        return messageHistory.drop(1).take(count.coerceAtLeast(0))
    }

    /**
     * Searches through the last N messages for one that matches the given predicate.
     *
     * @param count The number of messages to search through
     * @param predicate A function to test each message's string content
     * @return The first matching message component, or null if not found
     */
    fun findInHistory(count: Int, predicate: (String) -> Boolean): Component? {
        return getLastMessages(count).firstOrNull { predicate(it.string) }
    }
}