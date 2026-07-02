package net.dungeonhub.promptoverlay.enums

import net.dungeonhub.promptoverlay.feature.OverlayFeature
import net.dungeonhub.promptoverlay.overlays.FriendRequestOverlay
import net.dungeonhub.promptoverlay.overlays.TrophyFishGgOverlay

enum class ChatRegex(val regex: Regex, val action: (MatchResult) -> Unit) {
    FriendRequest(Regex("Friend request from ((?<rank>\\[.+] )?(?<player>\\S{1,16})).*"), action={ result ->
        val player = result.groups["player"]?.value

        if(player != null) OverlayFeature.setOverlay(FriendRequestOverlay(player))
    }),
    TrophyFishGg(Regex("CLICK HERE to say gg!"), action={ OverlayFeature.setOverlay(TrophyFishGgOverlay()) })
}