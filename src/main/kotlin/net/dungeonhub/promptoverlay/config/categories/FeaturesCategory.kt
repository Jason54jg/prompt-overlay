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
}