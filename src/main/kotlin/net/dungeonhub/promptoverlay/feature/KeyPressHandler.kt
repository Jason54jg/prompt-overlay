package net.dungeonhub.promptoverlay.feature

import net.dungeonhub.promptoverlay.render.AcceptableOverlay
import net.dungeonhub.promptoverlay.render.DeniableOverlay
import net.dungeonhub.promptoverlay.enums.RemoveType

object KeyPressHandler {
    fun handleAccept() {
        val currentOverlay = OverlayFeature.currentOverlay as? AcceptableOverlay ?: return

        currentOverlay.accept()

        OverlayFeature.removeOverlay(RemoveType.Accept)
    }

    fun handleDeny() {
        val currentOverlay = OverlayFeature.currentOverlay as? DeniableOverlay ?: return

        currentOverlay.deny()

        OverlayFeature.removeOverlay(RemoveType.Deny)
    }

    fun handleDismiss() {
        val currentOverlay = OverlayFeature.currentOverlay ?: return

        currentOverlay.dismiss()

        OverlayFeature.removeOverlay(RemoveType.Dismiss)
    }
}