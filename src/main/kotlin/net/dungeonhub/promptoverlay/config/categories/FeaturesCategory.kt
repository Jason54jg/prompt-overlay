package net.dungeonhub.promptoverlay.config.categories

import com.teamresourceful.resourcefulconfig.api.types.options.TranslatableValue
import com.teamresourceful.resourcefulconfigkt.api.CategoryKt

object FeaturesCategory : CategoryKt("features") {
    override val name: TranslatableValue
        get() = Literal("Features")

    val showDarkAuctionWarp by boolean("show_dark_auction_warp", false) {
        name = Literal("Dark Auction Warp")
        description = Literal("Show the Dark Auction Warp reminder.")
    }

    val darkAuctionTime by int("dark_auction_notification_time", 15) {
        name = Literal("Notification time")
        description = Literal("Change how many seconds before the dark auction you'll be notified.")
        range = 5..60
        slider = true
    }
}