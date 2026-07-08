package net.dungeonhub.promptoverlay.config.categories

import com.teamresourceful.resourcefulconfig.api.types.options.TranslatableValue
import com.teamresourceful.resourcefulconfigkt.api.CategoryKt
import java.awt.Color

object OverlayCategory : CategoryKt("overlay") {
    override val name: TranslatableValue
        get() = Literal("Overlays")

    val overlayDisplayDuration by int("overlay_display_duration", 10) {
        name = Literal("Overlay Display Duration")
        description = Literal("How long (in seconds) the prompt is displayed before being automatically dismissed.")
        range = 3..45
        slider = true
    }

    val backgroundColor by color("background_color", 0xA0000000.toInt()) {
        name = Literal("Background Color")
        allowAlpha = true
    }

    val abiphoneColor by color("abiphone_call_color", Color.LIGHT_GRAY.rgb) {
        name = Literal("Abiphone Call Color")
        description = Literal("The color of the abiphone call overlay.")
    }

    val catacombsRequeueColor by color("catacombs_requeue_color", Color.DARK_GRAY.rgb) {
        name = Literal("Catacombs Requeue Color")
        description = Literal("The color of the catacombs requeue overlay.")
    }

    val darkAuctionColor by color("dark_auction_warp_color", Color.BLACK.rgb) {
        name = Literal("Dark Auction Color")
        description = Literal("The color of the dark auction warp overlay.")
    }

    val duelColor by color("duel_color", Color.RED.rgb) {
        name = Literal("Duel Color")
        description = Literal("The color of the duel invite overlay.")
    }

    val friendColor by color("friend_color", Color.PINK.rgb) {
        name = Literal("Friend Color")
        description = Literal("The color of the friend request overlay.")
    }

    val guildColor by color("guild_color", Color.GREEN.rgb) {
        name = Literal("Guild Color")
        description = Literal("The color of the guild request overlay.")
    }

    val optionSelectColor by color("option_select_color", Color.GRAY.rgb) {
        name = Literal("Option Select")
        description = Literal("The color of the generic option select - used in NPC dialogs such as conversations.")
    }

    val partyColor by color("party_color", Color.BLUE.rgb) {
        name = Literal("Party Color")
        description = Literal("The color of the party invite overlay.")
    }

    val tradeColor by color("trade_color", Color(0x2BA801).rgb) {
        name = Literal("Trade Color")
        description = Literal("The color of the Skyblock trade overlay.")
    }

    val trapperColor by color("trapper_color", Color(0xA52A2A).rgb) {
        name = Literal("Trapper Color")
        description = Literal("The color of the Trapper Hunt overlay.")
    }

    val trophyFishColor by color("trophy_fish_color", Color.YELLOW.rgb) {
        name = Literal("Trophy Fish Color")
        description = Literal("The color of the Trophy Fish GG overlay.")
    }

    val alwaysPrideMonth by boolean("always_pride_month", false) {
        name = Literal("Always Pride Month")
        description = Literal("Always assume that it's the Pride Month, giving you a special theme.")
    }
}