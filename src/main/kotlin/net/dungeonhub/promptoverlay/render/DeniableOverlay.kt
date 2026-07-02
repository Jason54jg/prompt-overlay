package net.dungeonhub.promptoverlay.render

interface DeniableOverlay : Overlay {
    fun deny()
}