package net.dungeonhub.promptoverlay.config.categories

import com.teamresourceful.resourcefulconfigkt.api.ObjectKt

object FeaturesToggle : ObjectKt() {
    val abiphoneCalls by boolean("show_abiphone_calls", true) {
        name = Literal("Abiphone Calls")
    }

    val catacombsRequeue by boolean("show_catacombs_requeue", true) {
        name = Literal("Catacombs Requeue")
    }

    val darkAuctionWarp by boolean("show_dark_auction_warp", true) {
        name = Literal("Dark Auction Warp")
        description = Literal("Show the Dark Auction Warp reminder.")
    }

    val duelInvite by boolean("show_duel_invite", true) {
        name = Literal("Duel Invite")
    }

    val friendRequest by boolean("show_friend_request", true) {
        name = Literal("Friend Request")
    }

    val guildInvite by boolean("show_guild_invite", true) {
        name = Literal("Guild Invite")
    }

    val npcOptionSelection by boolean("show_option_selects", true) {
        name = Literal("NPC Dialog Options")
        description = Literal("This currently only supports up to 2 options, so don't freak out if some dialogs aren't getting recognized")
    }

    val partyInvites by boolean("show_party_invites", true) {
        name = Literal("Party Invites")
    }

    val skyblockTrade by boolean("show_skyblock_trade", true) {
        name = Literal("Skyblock Trade Request")
    }

    val trapperHunt by boolean("show_trapper_hunt", true) {
        name = Literal("Trapper Hunt")
    }

    val trophyFishGg by boolean("show_trophy_fish_gg", true) {
        name = Literal("Trophy Fish GG")
    }
}