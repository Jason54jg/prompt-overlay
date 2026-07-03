package net.dungeonhub.promptoverlay.enums

import net.dungeonhub.promptoverlay.feature.OverlayFeature
import net.dungeonhub.promptoverlay.overlays.FriendRequestOverlay
import net.dungeonhub.promptoverlay.overlays.PartyInviteOverlay
import net.dungeonhub.promptoverlay.overlays.SkyblockTradeOverlay
import net.dungeonhub.promptoverlay.overlays.TrapperHuntOverlay
import net.dungeonhub.promptoverlay.overlays.TrophyFishGgOverlay
import net.minecraft.network.chat.ClickEvent
import net.minecraft.network.chat.Component

enum class ChatRegex(val regex: Regex, val action: (message: Component, result: MatchResult) -> Unit) {
    FriendRequest(Regex("Friend request from ((?<rank>\\[.+] )?(?<player>\\S{1,16})).*"), action={ _, result ->
        val player = result.groups["player"]?.value

        if(player != null) OverlayFeature.setOverlay(FriendRequestOverlay(player))
    }),
    PartyInvite(Regex("(?:\\[.*] )?(?<player>\\S{1,16}) has invited you to join (?:their|(?:\\[.*] ?)?\\w{1,16}'s)? party!"), action={ _, result ->
        val player = result.groups["player"]?.value

        if(player != null) OverlayFeature.setOverlay(PartyInviteOverlay(player))
    }),
    SkyblockTrade(Regex("(?<player>\\S{1,16}) (?:§.)?has sent you a trade request"), action={ message, result ->
        val player = result.groups["player"]?.value
        val acceptCommand = findClickCommand(message) { it.startsWith("/tradeaccept") }

        if(player != null && acceptCommand != null) {
            OverlayFeature.setOverlay(SkyblockTradeOverlay(player, acceptCommand))
        }
    }),
    TrapperHunt(Regex("Accept the trapper's task to hunt the animal?"), action={ message, _ ->
        val acceptCommand = findClickCommand(message) { it.contains("[YES]") }
        val denyCommand = findClickCommand(message) { it.contains("[NO]") }

        if (acceptCommand != null && denyCommand != null) {
            OverlayFeature.setOverlay(TrapperHuntOverlay(acceptCommand, denyCommand))
        }
    }),
    TrophyFishGg(Regex("§6§lCLICK HERE §eto say §6gg§e!"), action={ _, _ -> OverlayFeature.setOverlay(TrophyFishGgOverlay()) });

    companion object {
        /**
         * Traverses a Component tree to find a click command that matches the given predicate.
         *
         * @param component The root component to traverse
         * @param predicate A function that tests either the command string or the component's text
         * @return The matching command string, or null if not found
         */
        private fun findClickCommand(component: Component, predicate: (String) -> Boolean): String? {
            val style = component.style
            val clickEvent = style.clickEvent as? ClickEvent.RunCommand

            if (clickEvent != null) {
                val command = clickEvent.command
                val text = component.string

                // Check if either the command or text matches the predicate
                if (predicate(command) || predicate(text)) {
                    return command
                }
            }

            // Traverse siblings
            for (sibling in component.siblings) {
                val result = findClickCommand(sibling, predicate)
                if (result != null) return result
            }

            return null
        }
    }
}