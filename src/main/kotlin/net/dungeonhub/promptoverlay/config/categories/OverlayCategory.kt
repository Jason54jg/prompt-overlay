package net.dungeonhub.promptoverlay.config.categories

import com.teamresourceful.resourcefulconfig.api.types.options.TranslatableValue
import com.teamresourceful.resourcefulconfigkt.api.CategoryKt
import net.dungeonhub.promptoverlay.PromptOverlay.MOD_ID
import java.awt.Color

object OverlayCategory : CategoryKt("$MOD_ID/overlay") {
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

    val partyColor by color("party_color", Color.BLUE.rgb) {
        name = Literal("Party Color")
        description = Literal("The color of the party invite overlay.")
    }

    val tradeColor by color("trade_color", Color(0x2BA801).rgb) {
        name = Literal("Trade Color")
        description = Literal("The color of the Skyblock trade overlay.")
    }

    val alwaysPrideMonth by boolean("always_pride_month", false) {
        name = Literal("Always Pride Month")
        description = Literal("Always assume that it's the Pride Month, giving you a special theme.")
    }
}