package net.dungeonhub.promptoverlay.service

import com.mojang.blaze3d.platform.InputConstants
import net.dungeonhub.promptoverlay.PromptOverlay
import net.dungeonhub.promptoverlay.feature.KeyPressHandler
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper
import net.minecraft.client.KeyMapping
import net.minecraft.client.Minecraft
import net.minecraft.resources.Identifier
import org.lwjgl.glfw.GLFW

object KeyMappingService {
    private val category = KeyMapping.Category(Identifier.fromNamespaceAndPath(PromptOverlay.MOD_ID, "keybinds"))

    private val heldKeys = mutableMapOf<String, Boolean>()

    val acceptKey = KeyMapping(
        "key.prompt-overlay.accept",
        InputConstants.Type.KEYSYM,
        GLFW.GLFW_KEY_Z,
        category
    )

    val denyKey = KeyMapping(
        "key.prompt-overlay.deny",
        InputConstants.Type.KEYSYM,
        GLFW.GLFW_KEY_N,
        category
    )

    val dismissKey = KeyMapping(
        "key.prompt-overlay.dismiss",
        InputConstants.Type.KEYSYM,
        GLFW.GLFW_KEY_X,
        category
    )

    fun init() {
        KeyMappingHelper.registerKeyMapping(acceptKey)
        KeyMappingHelper.registerKeyMapping(denyKey)
        KeyMappingHelper.registerKeyMapping(dismissKey)

        initListeners()
    }

    private fun initListeners() {
        ClientTickEvents.END_CLIENT_TICK.register(ClientTickEvents.EndTick { _: Minecraft ->
            handleKeyDown(acceptKey) {
                KeyPressHandler.handleAccept()
            }

            handleKeyDown(denyKey) {
                KeyPressHandler.handleDeny()
            }

            handleKeyDown(dismissKey) {
                KeyPressHandler.handleDismiss()
            }
        })
    }

    private fun handleKeyDown(keyMapping: KeyMapping, action: () -> Unit) {
        val state = heldKeys[keyMapping.name]

        if(keyMapping.isDown) {
            if(state == null || !state) {
                heldKeys[keyMapping.name] = true
                action()
            }
        } else {
            heldKeys[keyMapping.name] = false
        }
    }
}
