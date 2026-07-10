package net.dungeonhub.promptoverlay.feature

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.toJavaLocalDateTime
import kotlinx.datetime.toKotlinLocalDateTime
import net.dungeonhub.promptoverlay.config.categories.FeaturesCategory
import net.dungeonhub.promptoverlay.overlays.DarkAuctionWarpOverlay
import net.minecraft.client.Minecraft
import java.util.concurrent.Executors
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds
import kotlin.time.toKotlinDuration

object ScheduleHandler {
    private val supervisor = SupervisorJob()
    private val dispatcher = Executors.newFixedThreadPool(2).asCoroutineDispatcher()

    private val scheduler = CoroutineScope(supervisor + dispatcher)

    fun init() {
        launchDarkAuctionPrompt()
    }

    private fun launchDarkAuctionPrompt() {
        scheduler.launch {
            delay(timeUntilDarkAuction() - FeaturesCategory.darkAuctionTime.seconds)

            if(Minecraft.getInstance().level != null && FeaturesCategory.showDarkAuctionWarp) {
                OverlayFeature.setOverlay(DarkAuctionWarpOverlay(FeaturesCategory.darkAuctionTime.seconds))
            }

            delay(FeaturesCategory.darkAuctionTime.seconds)

            launchDarkAuctionPrompt()
        }
    }

    private fun timeUntilDarkAuction(): Duration {
        return java.time.Duration.between(java.time.LocalDateTime.now(), nextDarkAuctionTime().toJavaLocalDateTime()).toKotlinDuration()
    }

    private fun nextDarkAuctionTime(): LocalDateTime {
        val now = java.time.LocalDateTime.now()
        return if(now.minute >= 55) {
            now.withMinute(55).withSecond(0).withNano(0).plusHours(1)
        } else {
            now.withMinute(55).withSecond(0).withNano(0)
        }.toKotlinLocalDateTime()
    }
}