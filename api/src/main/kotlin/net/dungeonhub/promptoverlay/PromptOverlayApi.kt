package net.dungeonhub.promptoverlay

import net.dungeonhub.promptoverlay.api.KeyMappingProvider
import net.dungeonhub.promptoverlay.api.OverlayHandler
import net.dungeonhub.promptoverlay.api.render.Overlay
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.ChatFormatting
import net.minecraft.client.Minecraft
import net.minecraft.network.chat.Component
import org.slf4j.LoggerFactory

/**
 * Central API registry for the Prompt Overlay system.
 * Mods using this API should register their [net.dungeonhub.promptoverlay.api.KeyMappingProvider] during initialization.
 */
object PromptOverlayApi {
    private val logger = LoggerFactory.getLogger(PromptOverlayApi::class.java)

    var promptOverlay: OverlayHandler? = null
        private set
    var keyMappingProvider: KeyMappingProvider? = null
        private set

    /**
     * Registers a [OverlayHandler] for the overlay system.
     * This should be called once during mod initialization.
     *
     * @param handler The handler to register
     * @throws IllegalStateException if a handler is already registered
     */
    fun registerOverlayHandler(handler: OverlayHandler) {
        if (promptOverlay != null) {
            throw IllegalStateException(
                "OverlayHandler is already registered. Only one handler can be registered per application instance."
            )
        }
        this.promptOverlay = handler
    }

    @JvmStatic
    fun setOverlay(overlay: Overlay): Boolean {
        if(FabricLoader.getInstance().isModLoaded("prompt-overlay")) {
            val overlayHandler = promptOverlay
            if(overlayHandler != null) {
                overlayHandler.setOverlay(overlay) // TODO add error handling (which then return false)
                return true
            } else {
                logger.error("The Prompt Overload mod is installed, but no overlay handler is registered. Please update your mod.")
                Minecraft.getInstance().execute {
                    Minecraft.getInstance().gui.hud.chat.addClientSystemMessage(Component.literal(
                        "An error occurred while a mod tried to set an overlay - no overlay handler is registered. Please update your mods."
                    ).withStyle(ChatFormatting.RED))
                }
                return false
            }
        } else {
            return false
        }
    }

    /**
     * Registers a [KeyMappingProvider] for the overlay system.
     * This should be called once during mod initialization.
     *
     * @param provider The provider to register
     * @throws IllegalStateException if a provider is already registered
     */
    fun registerKeyMappingProvider(provider: KeyMappingProvider) {
        if (keyMappingProvider != null) {
            throw IllegalStateException(
                "KeyMappingProvider is already registered. Only one provider can be registered per application instance."
            )
        }
        this.keyMappingProvider = provider
    }

    /**
     * Internal method to retrieve the registered [KeyMappingProvider].
     * Used by overlay implementations to access key mapping names.
     *
     * @return The registered provider
     * @throws IllegalStateException if no provider has been registered
     */
    internal fun getKeyMappingProvider(): KeyMappingProvider {
        return keyMappingProvider
            ?: throw IllegalStateException(
                "No KeyMappingProvider registered. Call PromptOverlayApi.registerKeyMappingProvider() during mod initialization."
            )
    }
}
