package net.dungeonhub.promptoverlay.overlays

import net.dungeonhub.promptoverlay.api.render.AcceptableOverlay
import net.dungeonhub.promptoverlay.api.render.OneActionOverlay
import net.dungeonhub.promptoverlay.config.categories.OverlayCategory
import net.dungeonhub.promptoverlay.util.MessageUtil.sendMessage
import net.minecraft.client.Minecraft
import net.minecraft.network.chat.Component
import java.awt.Color

class PartyCommandOverlay(val player: String, val partyCommand: PartyCommand) : AcceptableOverlay, OneActionOverlay {
    val ingameCommand: String = when (partyCommand) {
        PartyCommand.Warp -> "party warp"
        PartyCommand.PartyTransfer -> "party transfer $player"
        PartyCommand.AllInvite -> "party settings allinvite"
    }

    val buttonText: String = when (partyCommand) {
        PartyCommand.Warp -> "Warp"
        PartyCommand.PartyTransfer -> "Transfer"
        PartyCommand.AllInvite -> "Toggle"
    }

    val messageText: String = when (partyCommand) {
        PartyCommand.Warp -> "Warp your party?"
        PartyCommand.PartyTransfer -> "Transfer the party to $player?"
        PartyCommand.AllInvite -> "Toggle party allinvite?"
    }

    override fun accept() {
        if(partyCommand == PartyCommand.PartyTransfer && player == Minecraft.getInstance().player?.name?.string) {
            Minecraft.getInstance().sendMessage(Component.literal("§cWhy would you think that's possible, mr. smarty pants?"))
            return
        }

        Minecraft.getInstance().execute {
            Minecraft.getInstance().player?.connection?.sendCommand(ingameCommand)
        }
    }

    override val borderColor: Color get() = Color(OverlayCategory.partyColor)
    override val message = Component.literal(messageText)

    override val firstText: String
        get() {
            return "[${acceptKey()}] $buttonText"
        }

    enum class PartyCommand(val commands: List<String>) {
        Warp("warp"),
        PartyTransfer("ptme", "transfer"),
        AllInvite("allinvite", "allinv");

        constructor(vararg commands: String) : this(commands.toList())

        companion object {
            fun getCommand(command: String) = entries.firstOrNull { partyCommand ->
                partyCommand.commands.any { it == command.lowercase() }
            }
        }
    }
}