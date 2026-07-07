package net.dungeonhub.promptoverlay.config

import com.teamresourceful.resourcefulconfig.api.types.options.TranslatableValue
import com.teamresourceful.resourcefulconfigkt.api.ConfigKt
import net.dungeonhub.promptoverlay.PromptOverlay
import net.dungeonhub.promptoverlay.PromptOverlay.MOD_ID
import net.dungeonhub.promptoverlay.config.categories.DevCategory
import net.dungeonhub.promptoverlay.config.categories.FeaturesCategory
import net.dungeonhub.promptoverlay.config.categories.OverlayCategory
import net.minecraft.util.Util

object Config : ConfigKt("$MOD_ID/config") {
    override val name: TranslatableValue
        get() = Literal("Prompt Overlay ${PromptOverlay.version}")

    init {
        separator {
            title = "Thanks for using the Prompt Overlay Mod!"
            description = "Please note that you have to configure the keybindings through the vanilla Minecraft Controls menu."
        }

        button {
            title = "GitHub"
            description = "This is open source!"
            text = "Open"
            onClick {
                Util.getPlatform().openUri("https://github.com/dungeon-hub/prompt-overlay")
            }
        }

        button {
            title = "Connect with us"
            description = "For questions and support, check out our discord"
            text = "Join"
            onClick {
                Util.getPlatform().openUri("https://discord.dungeon-hub.net/")
            }
        }

        button {
            title = "Support us"
            description = "Support our development costs and keep the servers running"
            text = "Patreon"
            onClick {
                Util.getPlatform().openUri("https://www.patreon.com/dungeon_hub/")
            }
        }
    }

    var developer by boolean("developer", false) {
        name = Literal("Developer Mode")
        description = Literal("Reopen the config after updating this value.")
    }

    init {
        category(FeaturesCategory)
        category(OverlayCategory)
        category(DevCategory)
    }
}