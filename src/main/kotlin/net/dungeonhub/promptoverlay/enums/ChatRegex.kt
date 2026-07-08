package net.dungeonhub.promptoverlay.enums

import net.dungeonhub.promptoverlay.config.categories.FeaturesCategory
import net.dungeonhub.promptoverlay.feature.ChatHandler
import net.dungeonhub.promptoverlay.feature.OverlayFeature
import net.dungeonhub.promptoverlay.overlays.AbiphoneCallOverlay
import net.dungeonhub.promptoverlay.overlays.CatacombsRequeueOverlay
import net.dungeonhub.promptoverlay.overlays.DuelInviteOverlay
import net.dungeonhub.promptoverlay.overlays.FriendRequestOverlay
import net.dungeonhub.promptoverlay.overlays.GuildRequestOverlay
import net.dungeonhub.promptoverlay.overlays.OptionSelectOverlay
import net.dungeonhub.promptoverlay.overlays.PartyInviteOverlay
import net.dungeonhub.promptoverlay.overlays.SingleOptionSelectOverlay
import net.dungeonhub.promptoverlay.overlays.SkyblockTradeOverlay
import net.dungeonhub.promptoverlay.overlays.TrapperHuntOverlay
import net.dungeonhub.promptoverlay.overlays.TrophyFishGgOverlay
import net.minecraft.ChatFormatting
import net.minecraft.network.chat.ClickEvent
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.HoverEvent
import net.minecraft.network.chat.MutableComponent
import net.minecraft.network.chat.contents.PlainTextContents

enum class ChatRegex(val regex: Regex, val action: (message: Component, result: MatchResult) -> Unit) {
    AbiphoneCall(Regex("✆ RING... RING... RING..."), action={ message, _ ->
        val command = findClickCommand(message) { it.contains("[PICK UP]") }
        val caller = extractAbiphoneCaller()?.let(this::formatMessageWithColor)

        if(caller != null) {
            if(!FeaturesCategory.allSeenAbiphoneContacts.contains(caller)) {
                val newSeen = FeaturesCategory.allSeenAbiphoneContacts.toMutableList()
                newSeen += caller
                FeaturesCategory.allSeenAbiphoneContacts = newSeen.toTypedArray()
            }
        }

        if(command != null && !FeaturesCategory.ignoredContacts.contains(caller)) {
            OverlayFeature.setOverlay(AbiphoneCallOverlay(caller, command))
        }
    }),
    CatacombsRequeue(Regex("Click §e§lHERE §7to re-queue into (§c§lMM§c |§c§a)The Catacombs"), action={ message, _ ->
        val info = extractCatacombsInfo(message)

        if (info != null) {
            val (type, floor) = info
            OverlayFeature.setOverlay(CatacombsRequeueOverlay("$type $floor"))
        }
    }),
    DuelInvite(Regex("(\\[.*] )?(?<player>\\S{1,16}) has invited you to (?<duel>\\S+)!"), action={ _, result ->
        val player = result.groups["player"]?.value
        val duel = result.groups["duel"]?.value

        if(player != null && duel != null) OverlayFeature.setOverlay(DuelInviteOverlay(player, duel))
    }),
    FriendRequest(Regex("Friend request from ((?<rank>\\[.+] )?(?<player>\\S{1,16})).*"), action={ _, result ->
        val player = result.groups["player"]?.value

        if(player != null) OverlayFeature.setOverlay(FriendRequestOverlay(player))
    }),
    GuildInvite(Regex("Click here to accept or type (?<command>/guild accept (?<name>\\w+))!"), action={ message, result ->
        val inviter = result.groups["name"]?.value

        // Optionally extract guild name from the full message
        val guildName = extractGuildName(message)

        if(inviter != null) {
            OverlayFeature.setOverlay(GuildRequestOverlay(inviter, guildName))
        }
    }),
    OptionSelect(Regex("§eSelect an option: "), action={ message, _ ->
        val optionComponent = findComponent(message) { ChatFormatting.stripFormatting(((it as? MutableComponent)?.contents as? PlainTextContents.LiteralContents)?.text)?.trim() == "Select an option:" }

        if(optionComponent != null) {
            val responses = optionComponent.siblings

            if(responses.all { (it.style.clickEvent as? ClickEvent.RunCommand)?.command?.startsWith("/selectnpcoption ") == true }) {
                val texts = responses.mapNotNull { it.string.trim().replace("[", "").replace("]", "") }
                val commands = responses.mapNotNull { (it.style.clickEvent as? ClickEvent.RunCommand)?.command }.map { if(it.startsWith("/")) it.substring(1) else it }

                if(responses.size == texts.size && responses.size == commands.size) {
                    if(responses.size == 2) { // TODO support more than two
                        if(ChatFormatting.stripFormatting(texts[0]) == "Yes" && ChatFormatting.stripFormatting(texts[1]) == "No" && isHoppityOptionAccept()) {
                            // This is the hoppity call
                            OverlayFeature.setOverlay(OptionSelectOverlay(texts[0], commands[0], texts[1], commands[1], "Accept Hoppity's Chocolate Rabbit?"))
                        } else {
                            OverlayFeature.setOverlay(OptionSelectOverlay(texts[0], commands[0], texts[1], commands[1]))
                        }
                    } else if(responses.size == 1) {
                        OverlayFeature.setOverlay(SingleOptionSelectOverlay(texts[0], commands[0]))
                    }
                }
            }
        }
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
        private fun findComponent(component: Component, predicate: (Component) -> Boolean): Component? {
            if(predicate(component)) return component

            // Traverse siblings
            for (sibling in component.siblings) {
                val result = findComponent(sibling, predicate)
                if (result != null) return result
            }

            return null
        }

        private fun isHoppityOptionAccept(): Boolean {
            val callerPattern = Regex("\\[NPC] Hoppity: ✆ I just got a new Chocolate Rabbit and was wondering if you wanted to buy it\\.")
            return ChatHandler.findInHistory(5) { message ->
                callerPattern.containsMatchIn(ChatFormatting.stripFormatting(message) ?: "")
            } != null
        }

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

        /**
         * Extracts the guild name from a guild invite message.
         * Looks for the pattern "has invited you to join their guild, <guildName>!"
         *
         * @param message The full message component
         * @return The guild name, or null if not found
         */
        private fun extractGuildName(message: Component): String? {
            val fullText = message.string
            val guildPattern = Regex("has invited you to join their guild, (.+)!")
            val match = guildPattern.find(fullText)
            return match?.groups?.get(1)?.value
        }

        /**
         * Extracts the type (MM or Catacombs) and floor from a catacombs requeue message.
         * Parses the hover event to find "MM The Catacombs" or "The Catacombs" and "Floor I" through "Floor VII"
         *
         * @param message The full message component
         * @return A pair of (type, floor), or null if not found
         */
        private fun extractCatacombsInfo(message: Component): Pair<String, String>? {
            val hoverText = extractHoverText(message)
            if (hoverText != null) {
                // Extract type (MM or Catacombs)
                val type = if (hoverText.contains("MM The Catacombs")) {
                    "MM"
                } else if (hoverText.contains("The Catacombs")) {
                    "Catacombs"
                } else {
                    return null
                }

                // Extract floor (Floor I through Floor VII)
                val floorPattern = Regex("Floor (I{1,3}|IV|VI{0,2})")
                val floorMatch = floorPattern.find(hoverText)
                val floor = floorMatch?.value ?: return null

                return Pair(type, floor)
            }
            return null
        }

        /**
         * Recursively extracts hover text from a component tree.
         *
         * @param component The root component to traverse
         * @return The concatenated hover text, or null if not found
         */
        private fun extractHoverText(component: Component): String? {
            val style = component.style
            val hoverEvent = style.hoverEvent as? HoverEvent.ShowText

            if (hoverEvent != null) {
                return ChatFormatting.stripFormatting(hoverEvent.value.string)
            }

            // Traverse siblings
            for (sibling in component.siblings) {
                val result = extractHoverText(sibling)
                if (result != null) return result
            }

            return null
        }

        /**
         * Extracts the caller name from recent Abiphone call messages.
         * Searches the last 5 messages for the pattern "§e✆ Fann ✆ §7" or "§e✆ §dSuus §e✆"
         *
         * @return The caller's name, or null if not found
         */
        private fun extractAbiphoneCaller(): Component? {
            val callerPattern = Regex("✆ (.+) (§e)?✆")
            val recentMessage = ChatHandler.findInHistory(25) { message ->
                callerPattern.containsMatchIn(message)
            } ?: return null

            val siblings = recentMessage.siblings.filter { it.siblings.isEmpty() }

            val callerName = siblings.firstOrNull { it.string.replace("✆", "").isNotBlank() }

            return callerName?.let {
                Component.literal(it.string.replace("✆", "").trim()).withStyle(callerName.style)
            } ?: recentMessage.let { msg ->
                callerPattern.find(msg.string)?.groups?.get(1)?.value?.let(Component::literal)
            }
        }

        private fun formatMessageWithColor(component: Component): String {
            val color = component.style.color?.serialize()
            val legacyFormatting = ChatFormatting.getByName(color)

            return (legacyFormatting?.toString() ?: "") + component.string
        }
    }
}