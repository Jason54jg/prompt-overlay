package net.dungeonhub.promptoverlay.config.categories

import com.teamresourceful.resourcefulconfig.api.client.ResourcefulConfigElementRenderer
import com.teamresourceful.resourcefulconfig.api.client.ResourcefulConfigUI
import com.teamresourceful.resourcefulconfig.api.client.options.ResourcefulConfigOptionUI
import com.teamresourceful.resourcefulconfig.api.types.ResourcefulConfigElement
import com.teamresourceful.resourcefulconfig.api.types.elements.ResourcefulConfigEntryElement
import com.teamresourceful.resourcefulconfig.api.types.entries.ResourcefulConfigValueEntry
import com.teamresourceful.resourcefulconfig.api.types.options.TranslatableValue
import com.teamresourceful.resourcefulconfigkt.api.CategoryKt
import net.dungeonhub.promptoverlay.PromptOverlay.MOD_ID
import net.minecraft.client.gui.components.AbstractWidget
import net.minecraft.network.chat.Component
import net.minecraft.resources.Identifier

object FeaturesCategory : CategoryKt("features") {
    override val name: TranslatableValue
        get() = Literal("Features")

    val showDarkAuctionWarp by boolean("show_dark_auction_warp", true) {
        name = Literal("Dark Auction Warp")
        description = Literal("Show the Dark Auction Warp reminder.")
    }

    val darkAuctionTime by int("dark_auction_notification_time", 15) {
        name = Literal("Notification time")
        description = Literal("Change how many seconds before the dark auction you'll be notified.")
        range = 5..60
        slider = true
    }

    var allSeenAbiphoneContacts by stringsWithId("all_seen_abiphone_contacts") {
        condition = { false }
    }

    private val _ignoredContacts by string("ignored_abiphone_contacts", "") {
        name = Literal("Ignored abiphone contacts")
        renderer = Identifier.fromNamespaceAndPath(MOD_ID, "ignored_abiphone_contacts_renderer")
    }

    private fun parseContacts(raw: String): List<String> = raw.split(";").filter { it.isNotEmpty() }
    private fun joinContacts(contacts: List<String?>): String = contacts.filterNotNull().joinToString(";")

    val ignoredContacts get() = parseContacts(_ignoredContacts)

    init {
        ResourcefulConfigUI.registerElementRenderer(Identifier.fromNamespaceAndPath(MOD_ID, "ignored_abiphone_contacts_renderer"), { IgnoredAbiphoneContactsRenderer(it) })
    }

    private data class IgnoredAbiphoneContactsRenderer(val element: ResourcefulConfigElement?) : ResourcefulConfigElementRenderer {
        override fun title(): Component {
            return Component.literal("Ignored abiphone contacts")
        }

        override fun description(): Component {
            return Component.literal("Select the abiphone contacts that you don't want to get prompts for.")
        }

        override fun widgets(): MutableList<AbstractWidget> {
            return mutableListOf(
                ResourcefulConfigOptionUI.select<String>(
                    Component.literal("Select..."),
                    allSeenAbiphoneContacts.map {
                        it
                    },
                    {
                        ((element as? ResourcefulConfigEntryElement)?.entry() as? ResourcefulConfigValueEntry)?.let { value ->
                            parseContacts(value.string)
                        } ?: mutableListOf<String?>()
                    },
                    { v ->
                        ((element as? ResourcefulConfigEntryElement)?.entry() as? ResourcefulConfigValueEntry)?.let { value ->
                            value.string = joinContacts(v)
                        }
                    }
                )
            )
        }
    }
}